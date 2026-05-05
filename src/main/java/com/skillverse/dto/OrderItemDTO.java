package com.skillverse.dto;

import com.skillverse.model.OrderItem;

/**
 * Data Transfer Object for OrderItem responses
 * Converts OrderItem entity to JSON-safe format without Hibernate proxies
 */
public class OrderItemDTO {

    private Integer id;
    private Integer courseId;
    private String courseName;
    private String courseDescription;
    private Double coursePrice;
    private Double itemPrice;

    public OrderItemDTO() {
    }

    public OrderItemDTO(OrderItem orderItem) {
        this.id = orderItem.getId();
        this.courseId = orderItem.getCourse() != null ? orderItem.getCourse().getId() : null;
        this.courseName = orderItem.getCourse() != null ? orderItem.getCourse().getTitle() : null;
        this.courseDescription = orderItem.getCourse() != null ? orderItem.getCourse().getDescription() : null;
        this.coursePrice = orderItem.getCourse() != null ? orderItem.getCourse().getPrice() : null;
        this.itemPrice = orderItem.getPrice();
    }

    // Getters and Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getCourseDescription() {
        return courseDescription;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }

    public Double getCoursePrice() {
        return coursePrice;
    }

    public void setCoursePrice(Double coursePrice) {
        this.coursePrice = coursePrice;
    }

    public Double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(Double itemPrice) {
        this.itemPrice = itemPrice;
    }
}

