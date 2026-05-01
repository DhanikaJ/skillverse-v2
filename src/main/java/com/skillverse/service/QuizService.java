package com.skillverse.service;

import com.skillverse.model.Course;
import com.skillverse.model.Quiz;
import com.skillverse.model.QuizQuestion;
import com.skillverse.repository.CourseRepository;
import com.skillverse.repository.QuizQuestionRepository;
import com.skillverse.repository.QuizRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service class for managing Quiz operations.
 * Provides functionality for creating quizzes and retrieving quiz questions.
 */
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

    /**
     * Creates a new quiz for a course.
     *
     * @param courseId the ID of the course
     * @param title the title of the quiz
     * @return the created Quiz entity
     * @throws IllegalStateException if the course is not found
     */
    @Transactional
    public Quiz createQuiz(Integer courseId, String title) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalStateException("Course not found with ID: " + courseId));

        Quiz quiz = new Quiz();
        quiz.setCourse(course);
        quiz.setTitle(title);

        return quizRepository.save(quiz);
    }

    /**
     * Retrieves all questions for a quiz, ordered by ID.
     *
     * @param quizId the ID of the quiz
     * @return a list of QuizQuestion entities
     * @throws IllegalStateException if the quiz is not found
     */
    @Transactional(readOnly = true)
    public List<QuizQuestion> getQuestionsByQuizId(Integer quizId) {
        quizRepository.findById(quizId)
                .orElseThrow(() -> new IllegalStateException("Quiz not found with ID: " + quizId));

        return quizQuestionRepository.findByQuiz_IdOrderByIdAsc(quizId);
    }
}

