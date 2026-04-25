package com.skillverse.repository;

import com.skillverse.model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {
    boolean existsByUser_IdAndCourse_Id(Integer userId, Integer courseId);
    List<Enrollment> findByUser_Id(Integer userId);
}