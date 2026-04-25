package com.skillverse.dto;

public class CourseStatsResponse {
    private long totalCourses;
    private long totalEnrollments;

    public CourseStatsResponse(long totalCourses, long totalEnrollments) {
        this.totalCourses = totalCourses;
        this.totalEnrollments = totalEnrollments;
    }

    public long getTotalCourses() {
        return totalCourses;
    }

    public void setTotalCourses(long totalCourses) {
        this.totalCourses = totalCourses;
    }

    public long getTotalEnrollments() {
        return totalEnrollments;
    }

    public void setTotalEnrollments(long totalEnrollments) {
        this.totalEnrollments = totalEnrollments;
    }
}

