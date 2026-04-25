package com.skillverse.model;

import jakarta.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    private Double amount;

    @ManyToOne
    @JoinColumn(name = "payment_method_id")
    private PaymentMethod paymentMethod;

    private String status;

    @Column(name = "txn_reference")
    private String txnReference;

    @Temporal(TemporalType.TIMESTAMP)
    private Date paid_at;

    @Temporal(TemporalType.TIMESTAMP)
    private Date created_at;

    public Payment() {
    }

    public Payment(Integer id, Users user, Course course, Double amount, PaymentMethod paymentMethod, String status, String txnReference, Date paid_at, Date created_at) {
        this.id = id;
        this.user = user;
        this.course = course;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.txnReference = txnReference;
        this.paid_at = paid_at;
        this.created_at = created_at;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
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

    public Date getPaid_at() {
        return paid_at;
    }

    public void setPaid_at(Date paid_at) {
        this.paid_at = paid_at;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Payment payment = (Payment) o;
        return Objects.equals(id, payment.id) && Objects.equals(user, payment.user) && Objects.equals(course, payment.course) && Objects.equals(amount, payment.amount) && Objects.equals(paymentMethod, payment.paymentMethod) && Objects.equals(status, payment.status) && Objects.equals(txnReference, payment.txnReference) && Objects.equals(paid_at, payment.paid_at) && Objects.equals(created_at, payment.created_at);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, course, amount, paymentMethod, status, txnReference, paid_at, created_at);
    }
}




