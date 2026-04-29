package com.skillverse.controller;

import com.skillverse.model.Payment;
import com.skillverse.service.PaymentService;
import com.skillverse.service.OrdersService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/payments")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "Payments", description = "Payment processing endpoints")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private OrdersService ordersService;

    /**
     * Initiate payment - student clicks Pay
     * Creates an order and payment record
     * Body: { "userId": 1, "courseId": 2, "amount": 99.99, "paymentMethod": "PayHere" }
     */
    @PostMapping("/initiate")
    public ResponseEntity<Map<String, Object>> initiatePayment(@RequestBody Map<String, Object> request) {
        try {
            Integer userId = ((Number) request.get("userId")).intValue();
            Integer courseId = ((Number) request.get("courseId")).intValue();
            Double amount = ((Number) request.get("amount")).doubleValue();
            String paymentMethod = (String) request.get("paymentMethod");

            // Create payment record
            Payment payment = paymentService.createPayment(userId, courseId, amount, paymentMethod);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Payment initiated successfully");
            response.put("paymentId", payment.getId());
            response.put("txnReference", payment.getTxnReference());
            response.put("amount", payment.getAmount());
            response.put("status", payment.getStatus());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Error initiating payment: " + e.getMessage()
            ));
        }
    }

    /**
     * PayHere Webhook Endpoint
     * Receives payment confirmation from PayHere
     * PayHere will POST to this endpoint with payment details
     */
    @PostMapping("/webhook/payhere")
    public ResponseEntity<String> handlePayHereWebhook(@RequestBody Map<String, Object> webhookData) {
        try {
            // Extract payment details from PayHere webhook
            String txnReference = (String) webhookData.get("merchant_reference_id");
            String status = (String) webhookData.get("status_code");
            Double amount = ((Number) webhookData.get("amount")).doubleValue();

            // Validate webhook signature (in production, verify PayHere signature)
            boolean isValid = validatePayHereSignature(webhookData);
            if (!isValid) {
                return ResponseEntity.badRequest().body("Invalid webhook signature");
            }

            // Map PayHere status codes to our status
            String paymentStatus = mapPayHereStatus(status);

            // Update payment with transaction reference and status
            Payment payment = paymentService.confirmPayment(txnReference, paymentStatus);

            // Log webhook event
            logWebhookEvent(txnReference, paymentStatus, amount);

            return ResponseEntity.ok("OK");
        } catch (Exception e) {
            logWebhookError(webhookData, e.getMessage());
            return ResponseEntity.status(500).body("Error processing webhook: " + e.getMessage());
        }
    }

    /**
     * Alternative webhook format - with more detailed PayHere fields
     */
    @PostMapping("/webhook/payhere-detailed")
    public ResponseEntity<String> handlePayHereWebhookDetailed(@RequestBody Map<String, Object> webhookData) {
        try {
            String order_id = (String) webhookData.get("order_id");
            String payment_id = (String) webhookData.get("payment_id");
            String merchant_id = (String) webhookData.get("merchant_id");
            String status_code = (String) webhookData.get("status_code");
            String custom_1 = (String) webhookData.get("custom_1");
            Double amount = ((Number) webhookData.get("amount")).doubleValue();

            // Verify signature
            boolean isValid = verifyPayHereSignature(webhookData);
            if (!isValid) {
                return ResponseEntity.badRequest().body("Invalid signature");
            }

            // Map status codes:
            // 0 = Initiated
            // 2 = Completed
            // -1 = Failed
            // -2 = Cancelled
            String paymentStatus = "PENDING";
            if ("2".equals(status_code)) {
                paymentStatus = "SUCCESS";
            } else if ("-1".equals(status_code)) {
                paymentStatus = "FAILED";
            } else if ("-2".equals(status_code)) {
                paymentStatus = "CANCELLED";
            }

            // Update payment in database
            Payment payment = paymentService.confirmPayment(payment_id, paymentStatus);

            System.out.println("Webhook received - Payment ID: " + payment_id + ", Status: " + paymentStatus + ", Amount: " + amount);

            return ResponseEntity.ok("OK");
        } catch (Exception e) {
            System.err.println("Webhook error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    /**
     * Get payment by ID
     */
    @GetMapping("/{paymentId}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable Integer paymentId) {
        try {
            Optional<Payment> payment = paymentService.getPaymentById(paymentId);
            if (payment.isPresent()) {
                return ResponseEntity.ok(payment.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get all payments for a user
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Payment>> getUserPayments(@PathVariable Integer userId) {
        try {
            List<Payment> payments = paymentService.getUserPayments(userId);
            return ResponseEntity.ok(payments);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get payment by transaction reference
     */
    @GetMapping("/txn/{txnReference}")
    public ResponseEntity<Payment> getPaymentByTxn(@PathVariable String txnReference) {
        try {
            Optional<Payment> payment = paymentService.getPaymentByTxnReference(txnReference);
            if (payment.isPresent()) {
                return ResponseEntity.ok(payment.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get all payments (admin)
     */
    @GetMapping
    public ResponseEntity<List<Payment>> getAllPayments() {
        try {
            List<Payment> payments = paymentService.getAllPayments();
            return ResponseEntity.ok(payments);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Helper methods

    private boolean validatePayHereSignature(Map<String, Object> webhookData) {
        // TODO: Implement PayHere signature validation
        // PayHere sends an "md5sig" field in webhook that should be verified
        // For now, returning true (implement proper validation in production)
        return true;
    }

    private boolean verifyPayHereSignature(Map<String, Object> webhookData) {
        // TODO: Implement signature verification
        // Concatenate merchant_id, order_id, amount, status_code, md5(merchant_secret)
        // and verify against provided md5sig
        return true;
    }

    private String mapPayHereStatus(String payHereStatus) {
        if ("2".equals(payHereStatus)) {
            return "SUCCESS";
        } else if ("-1".equals(payHereStatus)) {
            return "FAILED";
        } else if ("-2".equals(payHereStatus)) {
            return "CANCELLED";
        } else if ("0".equals(payHereStatus)) {
            return "INITIATED";
        }
        return "PENDING";
    }

    private void logWebhookEvent(String txnReference, String status, Double amount) {
        System.out.println("[WEBHOOK] TXN: " + txnReference + " | Status: " + status + " | Amount: " + amount);
    }

    private void logWebhookError(Map<String, Object> webhookData, String error) {
        System.err.println("[WEBHOOK ERROR] Data: " + webhookData + " | Error: " + error);
    }
}


