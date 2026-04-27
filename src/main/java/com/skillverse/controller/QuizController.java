package com.skillverse.controller;

import com.skillverse.model.Quiz;
import com.skillverse.model.QuizQuestion;
import com.skillverse.service.QuizService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/quizzes")
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @PostMapping
    public Quiz createQuiz(@Valid @RequestBody CreateQuizRequest request) {
        return quizService.createQuiz(request.getCourseId(), request.getTitle());
    }

    @GetMapping("/{id}/questions")
    public List<QuizQuestion> getQuestions(@PathVariable Integer id) {
        return quizService.getQuestionsByQuizId(id);
    }

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

