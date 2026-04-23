package com.skillverse.service;

import com.skillverse.model.Course;
import com.skillverse.model.Enrollment;
import com.skillverse.model.Users;
import com.skillverse.repository.CourseRepository;
import com.skillverse.repository.EnrollmentRepository;
import com.skillverse.repository.UsersRepository;
import org.springframework.stereotype.Service;

@Service
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final UsersRepository usersRepository;
    private final CourseRepository courseRepository;

    public EnrollmentService(
            EnrollmentRepository enrollmentRepository,
            UsersRepository usersRepository,
            CourseRepository courseRepository
    ) {
        this.enrollmentRepository = enrollmentRepository;
        this.usersRepository = usersRepository;
        this.courseRepository = courseRepository;
    }

    public Enrollment enroll(Integer studentId, Integer courseId) {
        if (enrollmentRepository.existsByUser_IdAndCourse_Id(studentId, courseId)) {
            throw new IllegalStateException("Student already enrolled in this course");
        }

        Users user = usersRepository.findById(studentId)
                .orElseThrow(() -> new IllegalStateException("User not found: " + studentId));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalStateException("Course not found: " + courseId));

        Enrollment enrollment = new Enrollment();
        enrollment.setUser(user);
        enrollment.setCourse(course);
        enrollment.setProgress(0.0); // default progress
        // enrollment.setStatus(...); // set if you already manage enrollment statuses

        return enrollmentRepository.save(enrollment);
    }
}