package com.skillverse.service;

import com.skillverse.dto.CourseStatsResponse;
import com.skillverse.dto.UserDTO;
import com.skillverse.mapper.EntityMapper;
import com.skillverse.repository.CourseRepository;
import com.skillverse.repository.EnrollmentRepository;
import com.skillverse.repository.UsersRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * Service class for administrative operations.
 * Provides functionality for managing users and retrieving platform statistics.
 */
@Service
public class AdminService {

    private final UsersRepository usersRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final EntityMapper entityMapper;

    public AdminService(
            UsersRepository usersRepository,
            CourseRepository courseRepository,
            EnrollmentRepository enrollmentRepository,
            EntityMapper entityMapper
    ) {
        this.usersRepository = usersRepository;
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.entityMapper = entityMapper;
    }

    /**
     * Retrieves all users in the system.
     *
     * @return a list of all UserDTO objects
     */
    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers() {
        return entityMapper.toUserDTOList(usersRepository.findAll());
    }

    /**
     * Retrieves course statistics including total courses and enrollments.
     *
     * @return a CourseStatsResponse containing platform statistics
     */
    @Transactional(readOnly = true)
    public CourseStatsResponse getCourseStats() {
        long totalCourses = courseRepository.count();
        long totalEnrollments = enrollmentRepository.count();
        return new CourseStatsResponse(totalCourses, totalEnrollments);
    }
}

