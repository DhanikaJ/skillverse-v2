package com.skillverse.dto;

import jakarta.validation.constraints.*;
import java.util.Date;

public class CertificateDTO {
    private Integer id;

    @NotNull(message = "User ID cannot be null")
    @Positive(message = "User ID must be a positive number")
    private Integer userId;


    @NotNull(message = "Course ID cannot be null")
    @Positive(message = "Course ID must be a positive number")
    private Integer courseId;

    private String courseTitle;

    @NotNull(message = "Issue date cannot be null")
    private Date issueDate;

    private String certificatePath;

    public CertificateDTO() {
    }

    public CertificateDTO(Integer id, Integer userId, Integer courseId, String courseTitle, Date issueDate, String certificatePath) {
        this.id = id;
        this.userId = userId;
        this.courseId = courseId;
        this.courseTitle = courseTitle;
        this.issueDate = issueDate;
        this.certificatePath = certificatePath;
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

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public String getCertificatePath() {
        return certificatePath;
    }

    public void setCertificatePath(String certificatePath) {
        this.certificatePath = certificatePath;
    }
}

