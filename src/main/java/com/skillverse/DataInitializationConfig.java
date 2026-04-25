package com.skillverse;

import com.skillverse.model.PaymentMethod;
import com.skillverse.repository.PaymentMethodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class DataInitializationConfig {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializationConfig.class);

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Bean
    public ApplicationRunner initializeData() {
        return args -> {
            try {
                // Initialize Payment Methods
                initializePaymentMethods();
                logger.info("Data initialization completed successfully");
            } catch (Exception e) {
                logger.error("Error during data initialization", e);
            }
        };
    }

    private void initializePaymentMethods() {
        String[] methods = {"PayHere", "Credit Card", "Debit Card", "PayPal", "Bank Transfer"};

        for (String method : methods) {
            if (paymentMethodRepository.findByMethod(method) == null) {
                PaymentMethod paymentMethod = new PaymentMethod();
                paymentMethod.setMethod(method);
                paymentMethodRepository.save(paymentMethod);
                logger.info("Created payment method: {}", method);
            }
        }
    }
}

