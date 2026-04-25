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
import java.util.List;

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

    public EnrollmentDTO enroll(Integer studentId, Integer courseId) {
        if (enrollmentRepository.existsByUser_IdAndCourse_Id(studentId, courseId)) {
            throw new ValidationException("Student already enrolled in this course");
        }

        Users user = usersRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + studentId));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with ID: " + courseId));

        Enrollment enrollment = new Enrollment();
        enrollment.setUser(user);
        enrollment.setCourse(course);
        enrollment.setProgress(0.0);

        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);
        return entityMapper.toEnrollmentDTO(savedEnrollment);
    }

    public List<EnrollmentDTO> getEnrollmentsByUserId(Integer userId) {
        usersRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
        List<Enrollment> enrollments = enrollmentRepository.findByUser_Id(userId);
        return entityMapper.toEnrollmentDTOList(enrollments);
    }
}

