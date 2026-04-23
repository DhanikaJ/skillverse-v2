package com.skillverse.model;

import jakarta.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(
        name = "enrollment",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "course_id"})
)
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "enrolled_at", nullable = false)
    private Date enrolled_at;

    @Column(nullable = false)
    private double progress;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private Status status;

    @PrePersist
    public void prePersist() {
        if (enrolled_at == null) enrolled_at = new Date();
    }

    public Enrollment() {
    }

    public Enrollment(Integer id, Users user, Course course, Date enrolled_at, double progress, Status status) {
        this.id = id;
        this.user = user;
        this.course = course;
        this.enrolled_at = enrolled_at;
        this.progress = progress;
        this.status = status;
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Enrollment that = (Enrollment) o;
        return Double.compare(progress, that.progress) == 0 && Objects.equals(id, that.id) && Objects.equals(user, that.user) && Objects.equals(course, that.course) && Objects.equals(enrolled_at, that.enrolled_at) && Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, course, enrolled_at, progress, status);
    }
}
