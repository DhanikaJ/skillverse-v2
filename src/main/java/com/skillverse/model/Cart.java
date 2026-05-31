package com.skillverse.model;

import jakarta.persistence.*;
import java.util.Date;
import java.util.Objects;

/**
 * Entity class representing a shopping cart item.
 * Contains Many-to-One relationships with Users and Course.
 */
@Entity
@Table(name = "cart")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Many-to-One relationship with User
     * EAGER loaded since cart always need user info
     */
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @Column(name = "session_id", length = 255)
    private String sessionId;

    /**
     * Many-to-One relationship with Course
     * EAGER loaded since cart item always related to a course
     */
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "added_at", nullable = false)
    private Date addedAt;

    @PrePersist
    public void prePersist() {
        if (addedAt == null) {
            addedAt = new Date();
        }
    }

    public Cart() {
    }

    public Cart(Integer id, Users user, String sessionId, Course course, Date addedAt) {
        this.id = id;
        this.user = user;
        this.sessionId = sessionId;
        this.course = course;
        this.addedAt = addedAt;
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

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Date getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(Date addedAt) {
        this.addedAt = addedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Cart cart = (Cart) o;
        return Objects.equals(id, cart.id) && Objects.equals(user, cart.user) && Objects.equals(sessionId, cart.sessionId) && Objects.equals(course, cart.course) && Objects.equals(addedAt, cart.addedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, sessionId, course, addedAt);
    }
}

