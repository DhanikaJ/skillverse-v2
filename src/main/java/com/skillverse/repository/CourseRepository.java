package com.skillverse.repository;

import com.skillverse.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course,Integer> {
    Optional<Course> findByTitle(String title);
}
