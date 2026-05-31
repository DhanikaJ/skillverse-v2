package com.skillverse.dto;

import jakarta.validation.constraints.*;
import java.util.Date;

public class CartDTO {
    private Integer id;

    @NotNull(message = "User ID cannot be null")
    @Positive(message = "User ID must be a positive number")
    private Integer userId;

    private String sessionId;

    @NotNull(message = "Course ID cannot be null")
    @Positive(message = "Course ID must be a positive number")
    private Integer courseId;

    private String courseTitle;
    private Date addedAt;

    public CartDTO() {
    }

    public CartDTO(Integer id, Integer userId, String sessionId, Integer courseId, String courseTitle, Date addedAt) {
        this.id = id;
        this.userId = userId;
        this.sessionId = sessionId;
        this.courseId = courseId;
        this.courseTitle = courseTitle;
        this.addedAt = addedAt;
    }

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

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public Date getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(Date addedAt) {
        this.addedAt = addedAt;
    }
}

