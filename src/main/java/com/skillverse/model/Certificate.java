package com.skillverse.model;

import jakarta.persistence.*;
import java.util.Date;
import java.util.Objects;

/**
 * Entity class representing a course completion certificate.
 * Contains Many-to-One relationships with Users and Course.
 */
@Entity
@Table(name = "certificate")
public class Certificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Many-to-One relationship with User
     * EAGER loaded since certificate always associated with a user
     */
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    /**
     * Many-to-One relationship with Course
     * EAGER loaded since certificate always associated with a course
     */
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "issue_date", nullable = false)
    private Date issueDate;

    @Column(name = "certificate_path", length = 500)
    private String certificatePath;

    public Certificate() {
    }

    public Certificate(Integer id, Users user, Course course, Date issueDate, String certificatePath) {
        this.id = id;
        this.user = user;
        this.course = course;
        this.issueDate = issueDate;
        this.certificatePath = certificatePath;
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Certificate that = (Certificate) o;
        return Objects.equals(id, that.id) && Objects.equals(user, that.user) && Objects.equals(course, that.course) && Objects.equals(issueDate, that.issueDate) && Objects.equals(certificatePath, that.certificatePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, course, issueDate, certificatePath);
    }
}

