package com.skillverse.service;

import com.skillverse.dto.CourseStatsResponse;
import com.skillverse.model.Users;
import com.skillverse.repository.CourseRepository;
import com.skillverse.repository.EnrollmentRepository;
import com.skillverse.repository.UsersRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AdminService {

    private final UsersRepository usersRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;

    public AdminService(
            UsersRepository usersRepository,
            CourseRepository courseRepository,
            EnrollmentRepository enrollmentRepository
    ) {
        this.usersRepository = usersRepository;
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    public List<Users> getAllUsers() {
        return usersRepository.findAll();
    }

    public CourseStatsResponse getCourseStats() {
        long totalCourses = courseRepository.count();
        long totalEnrollments = enrollmentRepository.count();
        return new CourseStatsResponse(totalCourses, totalEnrollments);
    }
}

