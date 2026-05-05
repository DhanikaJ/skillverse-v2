package com.skillverse.dto;

import com.skillverse.model.Orders;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object for Orders responses
 * Converts Orders entity to JSON-safe format without Hibernate proxies
 */
public class OrderDTO {

    private Integer id;
    private Integer userId;
    private String userName;
    private Double total;
    private String status;
    private Long createdAt;
    private List<OrderItemDTO> orderItems;

    public OrderDTO() {
        this.orderItems = new ArrayList<>();
    }

    public OrderDTO(Orders orders) {
        this.id = orders.getId();
        this.userId = orders.getUser() != null ? orders.getUser().getId() : null;
        this.userName = orders.getUser() != null ? orders.getUser().getFname() + " " + orders.getUser().getLname() : null;
        this.total = orders.getTotal();
        this.status = orders.getStatus();
        this.createdAt = orders.getCreated_at() != null ? orders.getCreated_at().getTime() : null;
        this.orderItems = new ArrayList<>();

        // Convert order items
        if (orders.getOrderItems() != null) {
            for (var item : orders.getOrderItems()) {
                this.orderItems.add(new OrderItemDTO(item));
            }
        }
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

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public List<OrderItemDTO> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemDTO> orderItems) {
        this.orderItems = orderItems;
    }
}

