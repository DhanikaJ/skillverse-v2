package com.skillverse.repository;

import com.skillverse.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LessonRepository extends JpaRepository<Lesson, Integer> {
    List<Lesson> findByCourse_IdOrderByOrderIndexAsc(Integer courseId);

    Optional<Lesson> findByIdAndCourse_Id(Integer id, Integer courseId);

    boolean existsByIdAndCourse_Id(Integer id, Integer courseId);
}

