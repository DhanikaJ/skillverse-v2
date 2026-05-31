package com.skillverse.model;

import jakarta.persistence.*;
import java.util.Date;
import java.util.Objects;

/**
 * Entity class representing a quiz attempt by a user.
 * Contains Many-to-One relationships with Quiz and Users.
 */
@Entity
@Table(name = "quiz_attempt")
public class QuizAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Many-to-One relationship with Quiz
     * EAGER loaded since attempt always related to a quiz
     */
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    /**
     * Many-to-One relationship with User
     * EAGER loaded since attempt always associated with a user
     */
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @Column(name = "score", nullable = false)
    private double score;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "attempted_at", nullable = false)
    private Date attemptedAt;

    @PrePersist
    public void prePersist() {
        if (attemptedAt == null) {
            attemptedAt = new Date();
        }
    }

    public QuizAttempt() {
    }

    public QuizAttempt(Integer id, Quiz quiz, Users user, double score, Date attemptedAt) {
        this.id = id;
        this.quiz = quiz;
        this.user = user;
        this.score = score;
        this.attemptedAt = attemptedAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        QuizAttempt that = (QuizAttempt) o;
        return Double.compare(score, that.score) == 0 && Objects.equals(id, that.id) && Objects.equals(quiz, that.quiz) && Objects.equals(user, that.user) && Objects.equals(attemptedAt, that.attemptedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quiz, user, score, attemptedAt);
    }
}

