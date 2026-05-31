package com.skillverse.model;

import jakarta.persistence.*;
import java.util.Date;
import java.util.Objects;

/**
 * Entity class representing a user review for a course.
 * Contains Many-to-One relationships with Users and Course.
 */
@Entity
@Table(name = "review")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Many-to-One relationship with User
     * EAGER loaded since review always associated with a user
     */
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    /**
     * Many-to-One relationship with Course
     * EAGER loaded since review always related to a course
     */
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(name = "rating", nullable = false)
    private Integer rating;

    @Column(name = "comment", length = 1000)
    private String comment;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "reviewed_at", nullable = false)
    private Date reviewedAt;

    @PrePersist
    public void prePersist() {
        if (reviewedAt == null) {
            reviewedAt = new Date();
        }
    }

    public Review() {
    }

    public Review(Integer id, Users user, Course course, Integer rating, String comment, Date reviewedAt) {
        this.id = id;
        this.user = user;
        this.course = course;
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

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Review review = (Review) o;
        return Objects.equals(id, review.id) && Objects.equals(user, review.user) && Objects.equals(course, review.course) && Objects.equals(rating, review.rating) && Objects.equals(comment, review.comment) && Objects.equals(reviewedAt, review.reviewedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, course, rating, comment, reviewedAt);
    }
}

