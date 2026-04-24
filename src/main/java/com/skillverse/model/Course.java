package com.skillverse.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "course")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String description;
    private String pricelevel;
    private String difficulty;
    // private Category category;
    private double price;
    private String thumbnail;
    //private Status status;

    @ManyToOne
    @JoinColumn(name = "users_id")
    private Users users;
    private Date created_at;
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Enrollment> enrollments = new ArrayList<>();
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({"course"})
    private List<Lesson> lessons = new ArrayList<>();
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({"course"})
    private List<Quiz> quizzes = new ArrayList<>();

    public Course() {
    }

    public Course(Integer id, String title, String description, String pricelevel, String difficulty, double price, String thumbnail, Users user, Date created_at) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.pricelevel = pricelevel;
        this.difficulty = difficulty;
        this.price = price;
        this.thumbnail = thumbnail;
        this.users = user;
        this.created_at = created_at;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPricelevel() {
        return pricelevel;
    }

    public void setPricelevel(String pricelevel) {
        this.pricelevel = pricelevel;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Users getUsers() {
        return users;
    }

    public void setUser(Users users) {
        this.users = users;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }

    public void addLesson(Lesson lesson) {
        this.lessons.add(lesson);
        lesson.setCourse(this);
    }

    public void removeLesson(Lesson lesson) {
        this.lessons.remove(lesson);
        lesson.setCourse(null);
    }

    public List<Quiz> getQuizzes() {
        return quizzes;
    }

    public void setQuizzes(List<Quiz> quizzes) {
        this.quizzes = quizzes;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return Objects.equals(id, course.id) && Double.compare(price, course.price) == 0 && Objects.equals(title, course.title) && Objects.equals(description, course.description) && Objects.equals(pricelevel, course.pricelevel) && Objects.equals(difficulty, course.difficulty) && Objects.equals(thumbnail, course.thumbnail) && Objects.equals(users, course.users) && Objects.equals(created_at, course.created_at);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, pricelevel, difficulty, price, thumbnail, users, created_at);
    }
}
