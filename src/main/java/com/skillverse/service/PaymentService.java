package com.skillverse.service;

import com.skillverse.exception.ResourceNotFoundException;
import com.skillverse.exception.ValidationException;
import com.skillverse.model.*;
import com.skillverse.repository.PaymentRepository;
import com.skillverse.repository.PaymentMethodRepository;
import com.skillverse.repository.EnrollmentRepository;
import com.skillverse.repository.UsersRepository;
import com.skillverse.repository.CourseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service class for managing Payment operations.
 * Payments are created for the authenticated user at the course's server-side price and are
 * confirmed only through verified provider notifications.
 */
@Service
public class PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);
    private static final String DEFAULT_PAYMENT_METHOD = "PayHere";
    private static final double AMOUNT_TOLERANCE = 0.005;

    private final PaymentRepository paymentRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final UsersRepository usersRepository;
    private final CourseRepository courseRepository;

    public PaymentService(
            PaymentRepository paymentRepository,
            PaymentMethodRepository paymentMethodRepository,
            EnrollmentRepository enrollmentRepository,
            UsersRepository usersRepository,
            CourseRepository courseRepository
    ) {
        this.paymentRepository = paymentRepository;
        this.paymentMethodRepository = paymentMethodRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.usersRepository = usersRepository;
        this.courseRepository = courseRepository;
    }

    /**
     * Creates a pending payment for a user and course.
     * The amount is always taken from the course price, never from the client.
     *
     * @param user the authenticated user making the payment
     * @param courseId the ID of the course being purchased
     * @param paymentMethodName an existing payment method name (defaults to PayHere when blank)
     * @return the created Payment entity
     * @throws ResourceNotFoundException if the course does not exist
     * @throws ValidationException if the course is free, already owned, or the method is unsupported
     */
    @Transactional
    public Payment createPayment(Users user, Integer courseId, String paymentMethodName) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with ID: " + courseId));

        if (enrollmentRepository.existsByUser_IdAndCourse_Id(user.getId(), course.getId())) {
            throw new ValidationException("Already enrolled in this course");
        }
        if (course.getPrice() <= 0) {
            throw new ValidationException("This course is free; enrol directly instead of paying");
        }

        String methodName = (paymentMethodName == null || paymentMethodName.isBlank())
                ? DEFAULT_PAYMENT_METHOD
                : paymentMethodName.trim();
        PaymentMethod paymentMethod = paymentMethodRepository.findByMethod(methodName);
        if (paymentMethod == null) {
            throw new ValidationException("Unsupported payment method: " + methodName);
        }

        Payment payment = new Payment();
        payment.setUser(user);
        payment.setCourse(course);
        payment.setAmount(course.getPrice());
        payment.setPaymentMethod(paymentMethod);
        payment.setStatus("PENDING");
        payment.setTxnReference("SV-" + UUID.randomUUID());
        payment.setCreated_at(new Date());

        return paymentRepository.save(payment);
    }

    /**
     * Retrieves a payment by its transaction reference.
     *
     * @param txnReference the transaction reference
     * @return an Optional containing the Payment if found
     */
    @Transactional(readOnly = true)
    public Optional<Payment> getPaymentByTxnReference(String txnReference) {
        return paymentRepository.findByTxnReference(txnReference);
    }

    /**
     * Applies a verified provider notification to a payment.
     *
     * The notification is idempotent: a payment that already succeeded is never changed again,
     * so retries and duplicate deliveries cannot downgrade it or enrol twice. The notified
     * amount must match the stored amount.
     *
     * @param txnReference the transaction reference (PayHere {@code order_id})
     * @param status the new status
     * @param notifiedAmount the amount reported by the provider
     * @return the resulting Payment entity
     * @throws ResourceNotFoundException if the payment does not exist
     * @throws ValidationException if the notified amount does not match
     */
    @Transactional
    public Payment confirmPayment(String txnReference, String status, double notifiedAmount) {
        Payment payment = paymentRepository.findByTxnReference(txnReference)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found for reference: " + txnReference));

        if (isPaymentSuccessful(payment.getStatus())) {
            logger.info("Payment {} already completed; ignoring duplicate notification", payment.getId());
            return payment;
        }
        if (Math.abs(payment.getAmount() - notifiedAmount) > AMOUNT_TOLERANCE) {
            logger.warn("Amount mismatch for payment {}: expected {}, notified {}",
                    payment.getId(), payment.getAmount(), notifiedAmount);
            throw new ValidationException("Notified amount does not match the payment amount");
        }

        payment.setStatus(status);
        if (isPaymentSuccessful(status)) {
            payment.setPaid_at(new Date());
            enrollStudent(payment.getUser(), payment.getCourse());
        }

        return paymentRepository.save(payment);
    }

    /**
     * Retrieves all payments for a specific user.
     *
     * @param userId the ID of the user
     * @return a list of Payment entities
     * @throws ResourceNotFoundException if the user is not found
     */
    @Transactional(readOnly = true)
    public List<Payment> getUserPayments(Integer userId) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
        return paymentRepository.findByUser(user);
    }

    /**
     * Retrieves a payment by its ID.
     *
     * @param id the payment ID
     * @return an Optional containing the Payment if found
     */
    @Transactional(readOnly = true)
    public Optional<Payment> getPaymentById(Integer id) {
        return paymentRepository.findById(id);
    }

    /**
     * Retrieves all payments.
     *
     * @return a list of all Payment entities
     */
    @Transactional(readOnly = true)
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    private void enrollStudent(Users user, Course course) {
        if (!enrollmentRepository.existsByUser_IdAndCourse_Id(user.getId(), course.getId())) {
            Enrollment enrollment = new Enrollment();
            enrollment.setUser(user);
            enrollment.setCourse(course);
            enrollment.setEnrolled_at(new Date());
            enrollment.setProgress(0.0);

            enrollmentRepository.save(enrollment);
        }
    }

    private boolean isPaymentSuccessful(String status) {
        return "SUCCESS".equals(status) || "COMPLETED".equals(status);
    }
}
