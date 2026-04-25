package com.skillverse.repository;

import com.skillverse.model.Payment;
import com.skillverse.model.Users;
import com.skillverse.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    List<Payment> findByUser(Users user);
    List<Payment> findByStatus(String status);
    Optional<Payment> findByTxnReference(String txnReference);
    List<Payment> findByUserAndCourse(Users user, Course course);
}




