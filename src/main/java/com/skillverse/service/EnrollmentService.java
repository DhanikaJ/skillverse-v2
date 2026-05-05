package com.skillverse.service;

import com.skillverse.dto.EnrollmentDTO;
import com.skillverse.exception.ResourceNotFoundException;
import com.skillverse.exception.ValidationException;
import com.skillverse.mapper.EntityMapper;
import com.skillverse.model.Course;
import com.skillverse.model.Enrollment;
import com.skillverse.model.Users;
import com.skillverse.repository.CourseRepository;
import com.skillverse.repository.EnrollmentRepository;
import com.skillverse.repository.UsersRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * Service class for managing Enrollment operations.
 * Provides functionality for enrolling students in courses and retrieving enrollments.
 */
@Service
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final UsersRepository usersRepository;
    private final CourseRepository courseRepository;
    private final EntityMapper entityMapper;

    public EnrollmentService(
            EnrollmentRepository enrollmentRepository,
            UsersRepository usersRepository,
            CourseRepository courseRepository,
            EntityMapper entityMapper
    ) {
        this.enrollmentRepository = enrollmentRepository;
        this.usersRepository = usersRepository;
        this.courseRepository = courseRepository;
        this.entityMapper = entityMapper;
    }

    /**
     * Enrolls a student in a course.
     *
     * @param studentId the ID of the student
     * @param courseId the ID of the course
     * @return the EnrollmentDTO object
     * @throws ValidationException if the student is already enrolled in this course
     * @throws ResourceNotFoundException if the user or course is not found
     */
    @Transactional
    public EnrollmentDTO enrollStudent(Integer studentId, Integer courseId) {
        if (enrollmentRepository.existsByUser_IdAndCourse_Id(studentId, courseId)) {
            throw new ValidationException("Student already enrolled in this course");
        }

        Users user = findUserById(studentId);
        Course course = findCourseById(courseId);

        Enrollment enrollment = new Enrollment();
        enrollment.setUser(user);
        enrollment.setCourse(course);
        enrollment.setProgress(0.0);

        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);
        return entityMapper.toEnrollmentDTO(savedEnrollment);
    }

    /**
     * Retrieves all enrollments for a given user.
     *
     * @param userId the ID of the user
     * @return a list of EnrollmentDTO objects
     * @throws ResourceNotFoundException if the user is not found
     */
    public List<EnrollmentDTO> getEnrollmentsByUserId(Integer userId) {
        findUserById(userId);
        List<Enrollment> enrollments = enrollmentRepository.findByUser_Id(userId);
        return entityMapper.toEnrollmentDTOList(enrollments);
    }

    /**
     * Retrieves all enrollments for a given course.
     *
     * @param courseId the ID of the course
     * @return a list of EnrollmentDTO objects
     * @throws ResourceNotFoundException if the course is not found
     */
    public List<EnrollmentDTO> getEnrollmentsByCourseId(Integer courseId) {
        findCourseById(courseId);
        List<Enrollment> enrollments = enrollmentRepository.findByCourse_Id(courseId);
        return entityMapper.toEnrollmentDTOList(enrollments);
    }

    /**
     * Finds a user by ID.
     *
     * @param userId the user ID
     * @return the Users entity
     * @throws ResourceNotFoundException if the user is not found
     */
    private Users findUserById(Integer userId) {
        return usersRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
    }

    /**
     * Finds a course by ID.
     *
     * @param courseId the course ID
     * @return the Course entity
     * @throws ResourceNotFoundException if the course is not found
     */
    private Course findCourseById(Integer courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with ID: " + courseId));
    }
}

