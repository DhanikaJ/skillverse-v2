package com.skillverse.service;

import com.skillverse.model.Course;
import com.skillverse.model.Quiz;
import com.skillverse.model.QuizQuestion;
import com.skillverse.repository.CourseRepository;
import com.skillverse.repository.QuizQuestionRepository;
import com.skillverse.repository.QuizRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuizService {

    private final QuizRepository quizRepository;
    private final QuizQuestionRepository quizQuestionRepository;
    private final CourseRepository courseRepository;

    public QuizService(QuizRepository quizRepository, QuizQuestionRepository quizQuestionRepository, CourseRepository courseRepository) {
        this.quizRepository = quizRepository;
        this.quizQuestionRepository = quizQuestionRepository;
        this.courseRepository = courseRepository;
    }

    public Quiz createQuiz(Integer courseId, String title) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalStateException("Course not found: " + courseId));

        Quiz quiz = new Quiz();
        quiz.setCourse(course);
        quiz.setTitle(title);

        return quizRepository.save(quiz);
    }

    public List<QuizQuestion> getQuestionsByQuizId(Integer quizId) {
        quizRepository.findById(quizId)
                .orElseThrow(() -> new IllegalStateException("Quiz not found: " + quizId));

        return quizQuestionRepository.findByQuiz_IdOrderByIdAsc(quizId);
    }
}

