package com.skillverse.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "quiz_question")
public class QuizQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "quiz_id", nullable = false)
    @JsonIgnoreProperties({"questions", "course"})
    private Quiz quiz;

    @Column(name = "question_text")
    private String questionText;

    @Column(name = "option_a")
    private String optionA;

    @Column(name = "option_b")
    private String optionB;

    @Column(name = "option_c")
    private String optionC;

    @Column(name = "option_d")
    private String optionD;

    @Column(name = "correct_option")
    private String correctOption;

    public QuizQuestion() {
    }

    public QuizQuestion(Integer id, Quiz quiz, String questionText, String optionA, String optionB, String optionC, String optionD, String correctOption) {
        this.id = id;
        this.quiz = quiz;
        this.questionText = questionText;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correctOption = correctOption;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getOptionA() {
        return optionA;
    }

    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public void setOptionC(String optionC) {
        this.optionC = optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public void setOptionD(String optionD) {
        this.optionD = optionD;
    }

    public String getCorrectOption() {
        return correctOption;
    }

    public void setCorrectOption(String correctOption) {
        this.correctOption = correctOption;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        QuizQuestion that = (QuizQuestion) o;
        return Objects.equals(id, that.id) && Objects.equals(quiz, that.quiz) && Objects.equals(questionText, that.questionText) && Objects.equals(optionA, that.optionA) && Objects.equals(optionB, that.optionB) && Objects.equals(optionC, that.optionC) && Objects.equals(optionD, that.optionD) && Objects.equals(correctOption, that.correctOption);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quiz, questionText, optionA, optionB, optionC, optionD, correctOption);
    }
}

