package com.skillverse.controller;

import com.skillverse.model.Quiz;
import com.skillverse.model.QuizQuestion;
import com.skillverse.service.QuizService;
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
    public Quiz createQuiz(@RequestBody CreateQuizRequest request) {
        return quizService.createQuiz(request.getCourseId(), request.getTitle());
    }

    @GetMapping("/{id}/questions")
    public List<QuizQuestion> getQuestions(@PathVariable Integer id) {
        return quizService.getQuestionsByQuizId(id);
    }

    public static class CreateQuizRequest {
        private Integer courseId;
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

