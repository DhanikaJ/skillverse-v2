package com.skillverse.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "lesson")
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "course_id", nullable = false)
    @JsonIgnoreProperties({"lessons", "enrollments"})
    private Course course;

    private String title;

    @Column(name = "video_url")
    private String videoUrl;

    @Column(name = "order_index")
    private Integer orderIndex;

    @Column(name = "resource_file")
    private String resourceFile;

    public Lesson() {
    }

    public Lesson(Integer id, Course course, String title, String videoUrl, Integer orderIndex, String resourceFile) {
        this.id = id;
        this.course = course;
        this.title = title;
        this.videoUrl = videoUrl;
        this.orderIndex = orderIndex;
        this.resourceFile = resourceFile;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public Integer getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }

    public String getResourceFile() {
        return resourceFile;
    }

    public void setResourceFile(String resourceFile) {
        this.resourceFile = resourceFile;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Lesson lesson = (Lesson) o;
        return Objects.equals(id, lesson.id) && Objects.equals(course, lesson.course) && Objects.equals(title, lesson.title) && Objects.equals(videoUrl, lesson.videoUrl) && Objects.equals(orderIndex, lesson.orderIndex) && Objects.equals(resourceFile, lesson.resourceFile);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, course, title, videoUrl, orderIndex, resourceFile);
    }
}

