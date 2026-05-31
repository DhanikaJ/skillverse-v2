package com.skillverse.dto;

import jakarta.validation.constraints.*;
import java.util.Date;

public class QuizAttemptDTO {
    private Integer id;

    @NotNull(message = "Quiz ID cannot be null")
    @Positive(message = "Quiz ID must be a positive number")
    private Integer quizId;

    private String quizTitle;

    @NotNull(message = "User ID cannot be null")
    @Positive(message = "User ID must be a positive number")
    private Integer userId;

    @NotNull(message = "Score cannot be null")
    @DecimalMin(value = "0.0", message = "Score cannot be negative")
    private double score;

    private Date attemptedAt;

    public QuizAttemptDTO() {
    }

    public QuizAttemptDTO(Integer id, Integer quizId, String quizTitle, Integer userId, double score, Date attemptedAt) {
        this.id = id;
        this.quizId = quizId;
        this.quizTitle = quizTitle;
        this.userId = userId;
        this.score = score;
        this.attemptedAt = attemptedAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getQuizId() {
        return quizId;
    }

    public void setQuizId(Integer quizId) {
        this.quizId = quizId;
    }

    public String getQuizTitle() {
        return quizTitle;
    }

    public void setQuizTitle(String quizTitle) {
        this.quizTitle = quizTitle;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public Date getAttemptedAt() {
        return attemptedAt;
    }

    public void setAttemptedAt(Date attemptedAt) {
        this.attemptedAt = attemptedAt;
    }
}

