package com.skillverse.service;

import com.skillverse.exception.ResourceNotFoundException;
import com.skillverse.model.Course;
import com.skillverse.model.Lesson;
import com.skillverse.repository.CourseRepository;
import com.skillverse.repository.LessonRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service class for managing Lesson operations.
 * Provides functionality for creating, retrieving, updating, and deleting lessons.
 */
@Service
public class LessonService {

    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;

    public LessonService(LessonRepository lessonRepository, CourseRepository courseRepository) {
        this.lessonRepository = lessonRepository;
        this.courseRepository = courseRepository;
    }

    /**
     * Creates a new lesson for a course.
     *
     * @param courseId the ID of the course
     * @param lesson the lesson entity to create
     * @return the created Lesson entity
     * @throws ResourceNotFoundException if the course is not found
     */
    @Transactional
    public Lesson createLesson(Integer courseId, Lesson lesson) {
        Course course = getCourse(courseId);
        lesson.setId(null);
        lesson.setCourse(course);
        return lessonRepository.save(lesson);
    }

    /**
     * Retrieves all lessons for a course, ordered by order index.
     *
     * @param courseId the ID of the course
     * @return a list of Lesson entities
     * @throws ResourceNotFoundException if the course is not found
     */
    public List<Lesson> getLessonsByCourse(Integer courseId) {
        getCourse(courseId);
        return lessonRepository.findByCourse_IdOrderByOrderIndexAsc(courseId);
    }

    /**
     * Retrieves a specific lesson for a course.
     *
     * @param courseId the ID of the course
     * @param lessonId the ID of the lesson
     * @return the Lesson entity
     * @throws ResourceNotFoundException if the lesson or course is not found
     */
    public Lesson getLesson(Integer courseId, Integer lessonId) {
        return lessonRepository.findByIdAndCourse_Id(lessonId, courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found with ID: " + lessonId + " for course: " + courseId));
    }

    /**
     * Updates an existing lesson.
     *
     * @param courseId the ID of the course
     * @param lessonId the ID of the lesson to update
     * @param lessonRequest the updated lesson data
     * @return the updated Lesson entity
     * @throws ResourceNotFoundException if the lesson or course is not found
     */
    @Transactional
    public Lesson updateLesson(Integer courseId, Integer lessonId, Lesson lessonRequest) {
        Lesson existingLesson = getLesson(courseId, lessonId);

        existingLesson.setTitle(lessonRequest.getTitle());
        existingLesson.setVideoUrl(lessonRequest.getVideoUrl());
        existingLesson.setOrderIndex(lessonRequest.getOrderIndex());
        existingLesson.setResourceFile(lessonRequest.getResourceFile());

        return lessonRepository.save(existingLesson);
    }

    /**
     * Deletes a lesson from a course.
     *
     * @param courseId the ID of the course
     * @param lessonId the ID of the lesson to delete
     * @throws ResourceNotFoundException if the lesson or course is not found
     */
    @Transactional
    public void deleteLesson(Integer courseId, Integer lessonId) {
        Lesson lesson = getLesson(courseId, lessonId);
        lessonRepository.delete(lesson);
    }

    /**
     * Finds a course by ID.
     *
     * @param courseId the course ID
     * @return the Course entity
     * @throws ResourceNotFoundException if the course is not found
     */
    private Course getCourse(Integer courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with ID: " + courseId));
    }
}

