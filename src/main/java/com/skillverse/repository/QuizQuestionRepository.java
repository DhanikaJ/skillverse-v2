package com.skillverse.repository;

import com.skillverse.model.QuizQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizQuestionRepository extends JpaRepository<QuizQuestion, Integer> {
    List<QuizQuestion> findByQuiz_IdOrderByIdAsc(Integer quizId);
}

