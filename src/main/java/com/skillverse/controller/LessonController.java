package com.skillverse.controller;

import com.skillverse.model.Lesson;
import com.skillverse.service.LessonService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/courses/{courseId}/lessons")
public class LessonController {

    private final LessonService lessonService;

    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @PostMapping
    public Lesson createLesson(@PathVariable Integer courseId, @RequestBody Lesson lesson) {
        return lessonService.createLesson(courseId, lesson);
    }

    @GetMapping
    public List<Lesson> getLessons(@PathVariable Integer courseId) {
        return lessonService.getLessons(courseId);
    }

    @GetMapping("/{lessonId}")
    public Lesson getLesson(@PathVariable Integer courseId, @PathVariable Integer lessonId) {
        return lessonService.getLesson(courseId, lessonId);
    }

    @PutMapping("/{lessonId}")
    public Lesson updateLesson(@PathVariable Integer courseId, @PathVariable Integer lessonId, @RequestBody Lesson lesson) {
        return lessonService.updateLesson(courseId, lessonId, lesson);
    }

    @DeleteMapping("/{lessonId}")
    public void deleteLesson(@PathVariable Integer courseId, @PathVariable Integer lessonId) {
        lessonService.deleteLesson(courseId, lessonId);
    }
}

