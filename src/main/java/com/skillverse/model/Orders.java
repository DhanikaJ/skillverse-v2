package com.skillverse.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Entity class representing an e-commerce order.
 * Contains relationships to Users (one-to-many) and OrderItems (one-to-many).
 */
@Entity
@Table(name = "orders")
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Many-to-One relationship with User - EAGER loaded since every order needs a user
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private Users user;

    private Double total;

    private String status;

    @Temporal(TemporalType.TIMESTAMP)
    private Date created_at;

    /**
     * One-to-Many relationship with OrderItems - LAZY loaded (only when needed)
     */
    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems = new ArrayList<>();

    public Orders() {
    }

    public Orders(Integer id, Users user, Double total, String status, Date created_at) {
        this.id = id;
        this.user = user;
        this.total = total;
        this.status = status;
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

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public void addOrderItem(OrderItem item) {
        this.orderItems.add(item);
        item.setOrders(this);
    }

    public void removeOrderItem(OrderItem item) {
        this.orderItems.remove(item);
        item.setOrders(null);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Orders orders = (Orders) o;
        return Objects.equals(id, orders.id) && Objects.equals(user, orders.user) && Objects.equals(total, orders.total) && Objects.equals(status, orders.status) && Objects.equals(created_at, orders.created_at);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, total, status, created_at);
    }
}

