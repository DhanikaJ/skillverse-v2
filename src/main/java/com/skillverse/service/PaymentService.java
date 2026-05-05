package com.skillverse.service;

import com.skillverse.model.*;
import com.skillverse.repository.PaymentRepository;
import com.skillverse.repository.PaymentMethodRepository;
import com.skillverse.repository.EnrollmentRepository;
import com.skillverse.repository.UsersRepository;
import com.skillverse.repository.CourseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Service class for managing Payment operations.
 * Provides functionality for creating payments, confirming payments, and managing payment records.
 */
@Service
public class PaymentService {

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
     * Creates a new payment record.
     *
     * @param userId the ID of the user making the payment
     * @param courseId the ID of the course being purchased
     * @param amount the payment amount
     * @param paymentMethodName the name of the payment method
     * @return the created Payment entity
     * @throws RuntimeException if the user or course is not found
     */
    @Transactional
    public Payment createPayment(Integer userId, Integer courseId, Double amount, String paymentMethodName) {
        Users user = findUserById(userId);
        Course course = findCourseById(courseId);
        PaymentMethod paymentMethod = findOrCreatePaymentMethod(paymentMethodName);

        Payment payment = new Payment();
        payment.setUser(user);
        payment.setCourse(course);
        payment.setAmount(amount);
        payment.setPaymentMethod(paymentMethod);
        payment.setStatus("PENDING");
        payment.setCreated_at(new Date());

        Payment savedPayment = paymentRepository.save(payment);
        savedPayment.setTxnReference(savedPayment.getId().toString());
        return paymentRepository.save(savedPayment);
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
     * Confirms a payment and enrolls the student in the course if payment is successful.
     *
     * @param txnReference the transaction reference
     * @param status the payment status
     * @return the updated Payment entity
     * @throws RuntimeException if the payment is not found
     */
    @Transactional
    public Payment confirmPayment(String txnReference, String status) {
        Payment payment = paymentRepository.findByTxnReference(txnReference)
                .orElseThrow(() -> new RuntimeException("Payment not found with txn_reference: " + txnReference));

        payment.setStatus(status);
        payment.setPaid_at(new Date());

        if (isPaymentSuccessful(status)) {
            enrollStudent(payment.getUser(), payment.getCourse());
        }

        return paymentRepository.save(payment);
    }

    /**
     * Retrieves all payments for a specific user.
     *
     * @param userId the ID of the user
     * @return a list of Payment entities
     * @throws RuntimeException if the user is not found
     */
    @Transactional(readOnly = true)
    public List<Payment> getUserPayments(Integer userId) {
        Users user = findUserById(userId);
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

    /**
     * Enrolls a student in a course after successful payment.
     *
     * @param user the user to enroll
     * @param course the course to enroll in
     */
    private void enrollStudent(Users user, Course course) {
        if (!enrollmentRepository.existsByUser_IdAndCourse_Id(user.getId(), course.getId())) {
            Enrollment enrollment = new Enrollment();
            enrollment.setUser(user);
            enrollment.setCourse(course);
            enrollment.setEnrolled_at(new Date());
            enrollment.setProgress(0.0);

            Status activeStatus = new Status();
            activeStatus.setId(1);
            activeStatus.setType("active");
            enrollment.setStatus(activeStatus);

            enrollmentRepository.save(enrollment);
        }
    }

    /**
     * Finds or creates a payment method.
     *
     * @param methodName the name of the payment method
     * @return the PaymentMethod entity
     */
    private PaymentMethod findOrCreatePaymentMethod(String methodName) {
        PaymentMethod paymentMethod = paymentMethodRepository.findByMethod(methodName);
        if (paymentMethod == null) {
            paymentMethod = new PaymentMethod();
            paymentMethod.setMethod(methodName);
            paymentMethod = paymentMethodRepository.save(paymentMethod);
        }
        return paymentMethod;
    }

    /**
     * Checks if a payment status indicates a successful payment.
     *
     * @param status the payment status
     * @return true if the payment is successful, false otherwise
     */
    private boolean isPaymentSuccessful(String status) {
        return "SUCCESS".equals(status) || "COMPLETED".equals(status);
    }

    /**
     * Finds a user by ID.
     *
     * @param userId the user ID
     * @return the Users entity
     * @throws RuntimeException if the user is not found
     */
    private Users findUserById(Integer userId) {
        return usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
    }

    /**
     * Finds a course by ID.
     *
     * @param courseId the course ID
     * @return the Course entity
     * @throws RuntimeException if the course is not found
     */
    private Course findCourseById(Integer courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with ID: " + courseId));
    }
}



