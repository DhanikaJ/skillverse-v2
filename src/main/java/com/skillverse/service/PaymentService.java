package com.skillverse.service;

import com.skillverse.model.*;
import com.skillverse.repository.PaymentRepository;
import com.skillverse.repository.PaymentMethodRepository;
import com.skillverse.repository.EnrollmentRepository;
import com.skillverse.repository.UsersRepository;
import com.skillverse.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private CourseRepository courseRepository;

    // Create a payment record
    public Payment createPayment(Integer userId, Integer courseId, Double amount, String paymentMethodName) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        PaymentMethod paymentMethod = paymentMethodRepository.findByMethod(paymentMethodName);

        if (paymentMethod == null) {
            paymentMethod = new PaymentMethod();
            paymentMethod.setMethod(paymentMethodName);
            paymentMethod = paymentMethodRepository.save(paymentMethod);
        }

        Payment payment = new Payment();
        payment.setUser(user);
        payment.setCourse(course);
        payment.setAmount(amount);
        payment.setPaymentMethod(paymentMethod);
        payment.setStatus("PENDING");
        payment.setCreated_at(new Date());

        // Save first to get the ID
        Payment savedPayment = paymentRepository.save(payment);

        // Set transaction reference to the payment ID for webhook lookup
        savedPayment.setTxnReference(savedPayment.getId().toString());

        return paymentRepository.save(savedPayment);
    }

    // Get payment by transaction reference
    public Optional<Payment> getPaymentByTxnReference(String txnReference) {
        return paymentRepository.findByTxnReference(txnReference);
    }

    // Confirm payment and enroll student
    public Payment confirmPayment(String txnReference, String status) {
        Optional<Payment> paymentOpt = paymentRepository.findByTxnReference(txnReference);

        if (!paymentOpt.isPresent()) {
            throw new RuntimeException("Payment not found with txn_reference: " + txnReference);
        }

        Payment payment = paymentOpt.get();
        payment.setStatus(status);
        payment.setPaid_at(new Date());

        // If payment is successful, enroll the student
        if ("SUCCESS".equals(status) || "COMPLETED".equals(status)) {
            enrollStudent(payment.getUser(), payment.getCourse());
        }

        return paymentRepository.save(payment);
    }

    // Enroll student in course
    private void enrollStudent(Users user, Course course) {
        // Check if already enrolled
        if (!enrollmentRepository.existsByUser_IdAndCourse_Id(user.getId(), course.getId())) {
            Enrollment enrollment = new Enrollment();
            enrollment.setUser(user);
            enrollment.setCourse(course);
            enrollment.setEnrolled_at(new Date());
            enrollment.setProgress(0.0);

            // Set status to active
            Status activeStatus = new Status();
            activeStatus.setId(1);
            activeStatus.setType("active");
            enrollment.setStatus(activeStatus);

            enrollmentRepository.save(enrollment);
        }
    }

    // Get all payments for a user
    public List<Payment> getUserPayments(Integer userId) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return paymentRepository.findByUser(user);
    }

    // Get payment by ID
    public Optional<Payment> getPaymentById(Integer id) {
        return paymentRepository.findById(id);
    }

    // Get all payments
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }
}



