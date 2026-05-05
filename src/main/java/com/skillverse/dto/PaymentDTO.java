package com.skillverse.dto;

import com.skillverse.model.Payment;

/**
 * Data Transfer Object for Payment responses
 * Converts Payment entity to JSON-safe format without Hibernate proxies
 */
public class PaymentDTO {

    private Integer id;
    private Integer userId;
    private String userName;
    private Integer courseId;
    private String courseName;
    private Double amount;
    private String status;
    private String txnReference;
    private String paymentMethod;
    private Long paidAt;
    private Long createdAt;

    public PaymentDTO() {
    }

    public PaymentDTO(Payment payment) {
        this.id = payment.getId();
        this.userId = payment.getUser() != null ? payment.getUser().getId() : null;
        this.userName = payment.getUser() != null ? payment.getUser().getFname() + " " + payment.getUser().getLname() : null;
        this.courseId = payment.getCourse() != null ? payment.getCourse().getId() : null;
        this.courseName = payment.getCourse() != null ? payment.getCourse().getTitle() : null;
        this.amount = payment.getAmount();
        this.status = payment.getStatus();
        this.txnReference = payment.getTxnReference();
        this.paymentMethod = payment.getPaymentMethod() != null ? payment.getPaymentMethod().getMethod() : null;
        this.paidAt = payment.getPaid_at() != null ? payment.getPaid_at().getTime() : null;
        this.createdAt = payment.getCreated_at() != null ? payment.getCreated_at().getTime() : null;
    }

    // Getters and Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTxnReference() {
        return txnReference;
    }

    public void setTxnReference(String txnReference) {
        this.txnReference = txnReference;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Long getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(Long paidAt) {
        this.paidAt = paidAt;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }
}


