package com.skillverse;

import com.skillverse.model.PaymentMethod;
import com.skillverse.repository.PaymentMethodRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Configuration class for initializing application data.
 * Ensures required payment methods are available in the database on startup.
 */
@Configuration
public class DataInitializationConfig {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializationConfig.class);
    private static final String[] PAYMENT_METHODS = {"PayHere", "Credit Card", "Debit Card", "PayPal", "Bank Transfer"};

    private final PaymentMethodRepository paymentMethodRepository;

    public DataInitializationConfig(PaymentMethodRepository paymentMethodRepository) {
        this.paymentMethodRepository = paymentMethodRepository;
    }

    /**
     * Initializes application data on startup.
     * Creates default payment methods if they don't already exist.
     *
     * @return an ApplicationRunner that performs data initialization
     */
    @Bean
    public ApplicationRunner initializeData() {
        return args -> {
            try {
                initializePaymentMethods();
                logger.info("Data initialization completed successfully");
            } catch (Exception e) {
                logger.error("Error during data initialization", e);
            }
        };
    }

    /**
     * Initializes default payment methods in the database.
     * Skips methods that already exist.
     */
    private void initializePaymentMethods() {
        for (String method : PAYMENT_METHODS) {
            if (paymentMethodRepository.findByMethod(method) == null) {
                PaymentMethod paymentMethod = new PaymentMethod();
                paymentMethod.setMethod(method);
                paymentMethodRepository.save(paymentMethod);
                logger.info("Created payment method: {}", method);
            }
        }
    }
}

