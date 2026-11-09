package com.skillverse.service;

import com.skillverse.exception.ResourceNotFoundException;
import com.skillverse.model.Course;
import com.skillverse.model.Lesson;
import com.skillverse.repository.CourseRepository;
import com.skillverse.repository.LessonRepository;
import com.skillverse.security.AccessPolicy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service class for managing Lesson operations.
 * Reading lessons requires enrollment in the course (or being its instructor / an admin);
 * creating, updating and deleting lessons is limited to the course instructor or an admin.
 */
@Service
public class LessonService {

    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;
    private final AccessPolicy accessPolicy;

    public LessonService(LessonRepository lessonRepository, CourseRepository courseRepository, AccessPolicy accessPolicy) {
        this.lessonRepository = lessonRepository;
        this.courseRepository = courseRepository;
        this.accessPolicy = accessPolicy;
    }

    /**
     * Creates a new lesson for a course (course instructor or admin).
     *
     * @throws ResourceNotFoundException if the course is not found
     */
    @Transactional
    public Lesson createLesson(Integer courseId, Lesson lesson) {
        Course course = getCourse(courseId);
        accessPolicy.requireCourseManager(course);
        lesson.setId(null);
        lesson.setCourse(course);
        return lessonRepository.save(lesson);
    }

    /**
     * Retrieves all lessons for a course, ordered by order index (enrolled learners, instructor, admin).
     *
     * @throws ResourceNotFoundException if the course is not found
     */
    @Transactional(readOnly = true)
    public List<Lesson> getLessonsByCourse(Integer courseId) {
        Course course = getCourse(courseId);
        accessPolicy.requireCourseAccess(course);
        return lessonRepository.findByCourse_IdOrderByOrderIndexAsc(courseId);
    }

    /**
     * Retrieves a specific lesson (enrolled learners, instructor, admin).
     *
     * @throws ResourceNotFoundException if the lesson or course is not found
     */
    @Transactional(readOnly = true)
    public Lesson getLesson(Integer courseId, Integer lessonId) {
        Lesson lesson = findLesson(courseId, lessonId);
        accessPolicy.requireCourseAccess(lesson.getCourse());
        return lesson;
    }

    /**
     * Updates an existing lesson (course instructor or admin).
     *
     * @throws ResourceNotFoundException if the lesson or course is not found
     */
    @Transactional
    public Lesson updateLesson(Integer courseId, Integer lessonId, Lesson lessonRequest) {
        Lesson existingLesson = findLesson(courseId, lessonId);
        accessPolicy.requireCourseManager(existingLesson.getCourse());

        existingLesson.setTitle(lessonRequest.getTitle());
        existingLesson.setVideoUrl(lessonRequest.getVideoUrl());
        existingLesson.setOrderIndex(lessonRequest.getOrderIndex());
        existingLesson.setResourceFile(lessonRequest.getResourceFile());

        return lessonRepository.save(existingLesson);
    }

    /**
     * Deletes a lesson from a course (course instructor or admin).
     *
     * @throws ResourceNotFoundException if the lesson or course is not found
     */
    @Transactional
    public void deleteLesson(Integer courseId, Integer lessonId) {
        Lesson lesson = findLesson(courseId, lessonId);
        accessPolicy.requireCourseManager(lesson.getCourse());
        lessonRepository.delete(lesson);
    }

    private Lesson findLesson(Integer courseId, Integer lessonId) {
        return lessonRepository.findByIdAndCourse_Id(lessonId, courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found with ID: " + lessonId + " for course: " + courseId));
    }

    private Course getCourse(Integer courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with ID: " + courseId));
    }
}
