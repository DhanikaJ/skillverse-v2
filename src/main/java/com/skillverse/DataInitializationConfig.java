package com.skillverse;

import com.skillverse.model.PaymentMethod;
import com.skillverse.repository.PaymentMethodRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Initializes reference data required in every environment.
 *
 * Only payment methods are created here. Sample users and courses live in
 * {@link DataSeeder}, which runs in the {@code dev} and {@code test} profiles only.
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
     * Creates the supported payment methods if they do not already exist.
     *
     * @return an ApplicationRunner that performs the initialization
     */
    @Bean
    public ApplicationRunner initializeData() {
        return args -> {
            try {
                initializePaymentMethods();
                logger.info("Reference data initialization completed");
            } catch (Exception e) {
                logger.error("Error during reference data initialization", e);
            }
        };
    }

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
