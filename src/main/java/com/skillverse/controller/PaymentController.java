package com.skillverse.controller;

import com.skillverse.dto.PaymentDTO;
import com.skillverse.dto.PaymentInitiateRequest;
import com.skillverse.exception.ResourceNotFoundException;
import com.skillverse.model.Payment;
import com.skillverse.model.Users;
import com.skillverse.security.AccessPolicy;
import com.skillverse.service.PayHereSignatureService;
import com.skillverse.service.PaymentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/payments")
@Tag(name = "Payments", description = "Payment processing endpoints")
public class PaymentController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    private final PaymentService paymentService;
    private final PayHereSignatureService signatureService;
    private final AccessPolicy accessPolicy;

    public PaymentController(PaymentService paymentService,
                             PayHereSignatureService signatureService,
                             AccessPolicy accessPolicy) {
        this.paymentService = paymentService;
        this.signatureService = signatureService;
        this.accessPolicy = accessPolicy;
    }

    /**
     * Initiate a payment for the authenticated user.
     * POST /api/v1/payments/initiate
     * Body: { "courseId": 2, "paymentMethod": "PayHere" }
     * The user comes from the JWT and the amount from the course price.
     */
    @PostMapping("/initiate")
    public ResponseEntity<Map<String, Object>> initiatePayment(@Valid @RequestBody PaymentInitiateRequest request) {
        Users user = accessPolicy.currentUser();
        Payment payment = paymentService.createPayment(user, request.getCourseId(), request.getPaymentMethod());

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Payment initiated successfully");
        response.put("paymentId", payment.getId());
        response.put("txnReference", payment.getTxnReference());
        response.put("amount", payment.getAmount());
        response.put("status", payment.getStatus());
        return ResponseEntity.ok(response);
    }

    /**
     * PayHere server-to-server notification (notify_url).
     *
     * This route is public because PayHere calls it without a JWT; trust comes from the
     * {@code md5sig} checksum, which is verified against the configured merchant secret.
     * PayHere posts {@code application/x-www-form-urlencoded} fields:
     * merchant_id, order_id, payhere_amount, payhere_currency, status_code, md5sig.
     * {@code order_id} must be the {@code txnReference} returned by {@code /initiate}.
     */
    @PostMapping(
            value = {"/webhook/payhere", "/webhook/payhere-detailed"},
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<String> handlePayHereNotification(@RequestParam Map<String, String> params) {
        if (!signatureService.isConfigured()) {
            logger.error("PayHere notification received but merchant credentials are not configured");
            return ResponseEntity.status(503).body("Payment notifications are not configured");
        }

        String orderId = params.get("order_id");
        String amount = params.get("payhere_amount");
        String statusCode = params.get("status_code");

        boolean validSignature = signatureService.verify(
                params.get("merchant_id"), orderId, amount,
                params.get("payhere_currency"), statusCode, params.get("md5sig"));
        if (!validSignature) {
            logger.warn("Rejected PayHere notification with invalid signature (order_id={})", orderId);
            return ResponseEntity.badRequest().body("Invalid signature");
        }

        double notifiedAmount;
        try {
            notifiedAmount = Double.parseDouble(amount);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Invalid amount");
        }

        String status = mapPayHereStatus(statusCode);
        Payment payment = paymentService.confirmPayment(orderId, status, notifiedAmount);
        logger.info("PayHere notification applied: payment={}, status={}", payment.getId(), payment.getStatus());
        return ResponseEntity.ok("OK");
    }

    /**
     * Get payment by ID (owner or admin).
     */
    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentDTO> getPaymentById(@PathVariable Integer paymentId) {
        Payment payment = paymentService.getPaymentById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with ID: " + paymentId));
        requireOwnerOrAdmin(payment);
        return ResponseEntity.ok(new PaymentDTO(payment));
    }

    /**
     * Get all payments for a user (that user or admin).
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PaymentDTO>> getUserPayments(@PathVariable Integer userId) {
        accessPolicy.requireSelfOrAdmin(userId);
        List<PaymentDTO> payments = paymentService.getUserPayments(userId).stream()
                .map(PaymentDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(payments);
    }

    /**
     * Get payment by transaction reference (owner or admin).
     */
    @GetMapping("/txn/{txnReference}")
    public ResponseEntity<PaymentDTO> getPaymentByTxn(@PathVariable String txnReference) {
        Payment payment = paymentService.getPaymentByTxnReference(txnReference)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found for reference: " + txnReference));
        requireOwnerOrAdmin(payment);
        return ResponseEntity.ok(new PaymentDTO(payment));
    }

    /**
     * Get all payments (admin only).
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<PaymentDTO>> getAllPayments() {
        List<PaymentDTO> payments = paymentService.getAllPayments().stream()
                .map(PaymentDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(payments);
    }

    private void requireOwnerOrAdmin(Payment payment) {
        Integer ownerId = payment.getUser() != null ? payment.getUser().getId() : null;
        accessPolicy.requireSelfOrAdmin(ownerId);
    }

    /**
     * Maps PayHere status codes: 2 success, 0 pending, -1 cancelled, -2 failed, -3 charged back.
     */
    private String mapPayHereStatus(String payHereStatus) {
        if ("2".equals(payHereStatus)) {
            return "SUCCESS";
        } else if ("-1".equals(payHereStatus)) {
            return "CANCELLED";
        } else if ("-2".equals(payHereStatus)) {
            return "FAILED";
        } else if ("-3".equals(payHereStatus)) {
            return "CHARGED_BACK";
        }
        return "PENDING";
    }
}
