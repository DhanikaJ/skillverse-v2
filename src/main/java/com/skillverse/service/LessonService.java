package com.skillverse.service;

import com.skillverse.model.Course;
import com.skillverse.model.Lesson;
import com.skillverse.repository.CourseRepository;
import com.skillverse.repository.LessonRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LessonService {

    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;

    public LessonService(LessonRepository lessonRepository, CourseRepository courseRepository) {
        this.lessonRepository = lessonRepository;
        this.courseRepository = courseRepository;
    }

    public Lesson createLesson(Integer courseId, Lesson lesson) {
        Course course = getCourse(courseId);
        lesson.setId(null);
        lesson.setCourse(course);
        return lessonRepository.save(lesson);
    }

    public List<Lesson> getLessons(Integer courseId) {
        getCourse(courseId);
        return lessonRepository.findByCourse_IdOrderByOrderIndexAsc(courseId);
    }

    public Lesson getLesson(Integer courseId, Integer lessonId) {
        return lessonRepository.findByIdAndCourse_Id(lessonId, courseId)
                .orElseThrow(() -> new IllegalStateException("Lesson not found: " + lessonId + " for course: " + courseId));
    }

    public Lesson updateLesson(Integer courseId, Integer lessonId, Lesson lessonRequest) {
        Lesson existingLesson = getLesson(courseId, lessonId);

        existingLesson.setTitle(lessonRequest.getTitle());
        existingLesson.setVideoUrl(lessonRequest.getVideoUrl());
        existingLesson.setOrderIndex(lessonRequest.getOrderIndex());
        existingLesson.setResourceFile(lessonRequest.getResourceFile());

        return lessonRepository.save(existingLesson);
    }

    public void deleteLesson(Integer courseId, Integer lessonId) {
        Lesson lesson = getLesson(courseId, lessonId);
        lessonRepository.delete(lesson);
    }

    private Course getCourse(Integer courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalStateException("Course not found: " + courseId));
    }
}

