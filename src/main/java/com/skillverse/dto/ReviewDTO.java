package com.skillverse.dto;

import jakarta.validation.constraints.*;
import java.util.Date;

public class ReviewDTO {
    private Integer id;

    @NotNull(message = "User ID cannot be null")
    @Positive(message = "User ID must be a positive number")
    private Integer userId;

    @NotNull(message = "Course ID cannot be null")
    @Positive(message = "Course ID must be a positive number")
    private Integer courseId;

    private String courseTitle;

    @NotNull(message = "Rating cannot be null")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating cannot exceed 5")
    private Integer rating;

    @Size(max = 1000, message = "Comment cannot exceed 1000 characters")
    private String comment;

    private Date reviewedAt;

    public ReviewDTO() {
    }

    public ReviewDTO(Integer id, Integer userId, Integer courseId, String courseTitle, Integer rating, String comment, Date reviewedAt) {
        this.id = id;
        this.userId = userId;
        this.courseId = courseId;
        this.courseTitle = courseTitle;
        this.rating = rating;
        this.comment = comment;
        this.reviewedAt = reviewedAt;
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

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getReviewedAt() {
        return reviewedAt;
    }

    public void setReviewedAt(Date reviewedAt) {
        this.reviewedAt = reviewedAt;
    }
}

