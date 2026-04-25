package com.skillverse.model;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "payment_method")
public class PaymentMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private String method;

    public PaymentMethod() {
    }

    public PaymentMethod(Integer id, String method) {
        this.id = id;
        this.method = method;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PaymentMethod that = (PaymentMethod) o;
        return Objects.equals(id, that.id) && Objects.equals(method, that.method);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, method);
    }
}

