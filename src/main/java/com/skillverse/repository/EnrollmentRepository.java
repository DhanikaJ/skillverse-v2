package com.skillverse.repository;

import com.skillverse.model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {
    boolean existsByUser_IdAndCourse_Id(Integer userId, Integer courseId);
}