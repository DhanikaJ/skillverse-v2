package com.skillverse.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Request body for starting a payment.
 *
 * Only the course and (optionally) the payment method are accepted. The paying user comes from
 * the JWT and the amount from the course price; any {@code userId} or {@code amount} sent by
 * older clients is ignored.
 */
public class PaymentInitiateRequest {

    @NotNull(message = "courseId is required")
    @Positive(message = "courseId must be a positive number")
    private Integer courseId;

    private String paymentMethod;

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
