package com.skillverse.dto;

import java.util.Date;

public class EnrollmentDTO {
    private Integer id;
    private Integer userId;
    private String userName;
    private Integer courseId;
    private String courseTitle;
    private Date enrolled_at;
    private double progress;
    private String statusType;

    public EnrollmentDTO() {
    }

    public EnrollmentDTO(Integer id, Integer userId, String userName, Integer courseId, String courseTitle, Date enrolled_at, double progress, String statusType) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.courseId = courseId;
        this.courseTitle = courseTitle;
        this.enrolled_at = enrolled_at;
        this.progress = progress;
        this.statusType = statusType;
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

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public Date getEnrolled_at() {
        return enrolled_at;
    }

    public void setEnrolled_at(Date enrolled_at) {
        this.enrolled_at = enrolled_at;
    }

    public double getProgress() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }

    public String getStatusType() {
        return statusType;
    }

    public void setStatusType(String statusType) {
        this.statusType = statusType;
    }
}

