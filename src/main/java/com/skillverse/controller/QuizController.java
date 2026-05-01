package com.skillverse.controller;

import com.skillverse.model.Quiz;
import com.skillverse.model.QuizQuestion;
import com.skillverse.service.QuizService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for managing quizzes.
 * Handles quiz creation and question retrieval endpoints.
 */
@RestController
@RequestMapping("/api/v1/quizzes")
@Tag(name = "Quizzes", description = "Quiz management endpoints")
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    /**
     * Creates a new quiz for a course.
     *
     * @param request the quiz creation request with courseId and title (validated)
     * @return ResponseEntity with created quiz (201 Created)
     */
    @PostMapping
    @Operation(summary = "Create a new quiz")
    @ApiResponse(responseCode = "201", description = "Quiz created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid quiz data")
    @ApiResponse(responseCode = "404", description = "Course not found")
    public ResponseEntity<Quiz> createQuiz(@Valid @RequestBody CreateQuizRequest request) {
        Quiz quiz = quizService.createQuiz(request.getCourseId(), request.getTitle());
        return ResponseEntity.status(HttpStatus.CREATED).body(quiz);
    }

    /**
     * Retrieves all questions for a quiz.
     *
     * @param id the quiz ID
     * @return ResponseEntity with list of quiz questions (200 OK)
     */
    @GetMapping("/{id}/questions")
    @Operation(summary = "Get all questions for a quiz")
    @ApiResponse(responseCode = "200", description = "Questions retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Quiz not found")
    public ResponseEntity<List<QuizQuestion>> getQuestions(@PathVariable Integer id) {
        List<QuizQuestion> questions = quizService.getQuestionsByQuizId(id);
        return ResponseEntity.ok(questions);
    }

    /**
     * Request DTO for creating a quiz.
     */
    public static class CreateQuizRequest {
        @jakarta.validation.constraints.NotNull(message = "Course ID cannot be null")
        @jakarta.validation.constraints.Positive(message = "Course ID must be a positive number")
        private Integer courseId;
        
        @jakarta.validation.constraints.NotBlank(message = "Title cannot be blank")
        @jakarta.validation.constraints.Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
        private String title;

        public Integer getCourseId() {
            return courseId;
        }

        public void setCourseId(Integer courseId) {
            this.courseId = courseId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}

