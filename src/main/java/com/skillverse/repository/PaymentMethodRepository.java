package com.skillverse.repository;

import com.skillverse.model.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Integer> {
    PaymentMethod findByMethod(String method);
}

