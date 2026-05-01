package com.skillverse.controller;

import com.skillverse.dto.LessonRequestDTO;
import com.skillverse.model.Lesson;
import com.skillverse.service.LessonService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for managing lessons within courses.
 * Handles CRUD operations for lessons.
 */
@RestController
@RequestMapping("/api/v1")
@Tag(name = "Lessons", description = "Lesson management endpoints")
public class LessonController {

    private final LessonService lessonService;

    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    /**
     * Creates a new lesson for a course.
     *
     * @param courseId the ID of the course
     * @param lessonRequestDTO the lesson data (validated)
     * @return ResponseEntity with created lesson (201 Created)
     */
    @PostMapping("/courses/{courseId}/lessons")
    @Operation(summary = "Create a new lesson")
    @ApiResponse(responseCode = "201", description = "Lesson created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid lesson data")
    @ApiResponse(responseCode = "404", description = "Course not found")
    public ResponseEntity<Lesson> createLesson(
            @PathVariable Integer courseId,
            @Valid @RequestBody LessonRequestDTO lessonRequestDTO
    ) {
        Lesson lesson = new Lesson();
        lesson.setTitle(lessonRequestDTO.getTitle());
        lesson.setVideoUrl(lessonRequestDTO.getVideo_url());
        lesson.setOrderIndex(lessonRequestDTO.getOrder_index());
        lesson.setResourceFile(lessonRequestDTO.getResource_file());
        Lesson createdLesson = lessonService.createLesson(courseId, lesson);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdLesson);
    }

    /**
     * Retrieves all lessons for a course.
     *
     * @param courseId the ID of the course
     * @return ResponseEntity with list of lessons (200 OK)
     */
    @GetMapping("/courses/{courseId}/lessons")
    @Operation(summary = "Get all lessons for a course")
    @ApiResponse(responseCode = "200", description = "Lessons retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Course not found")
    public ResponseEntity<List<Lesson>> getLessonsByCourse(@PathVariable Integer courseId) {
        List<Lesson> lessons = lessonService.getLessonsByCourse(courseId);
        return ResponseEntity.ok(lessons);
    }

    /**
     * Retrieves a specific lesson.
     *
     * @param courseId the ID of the course
     * @param lessonId the ID of the lesson
     * @return ResponseEntity with lesson data (200 OK)
     */
    @GetMapping("/courses/{courseId}/lessons/{lessonId}")
    @Operation(summary = "Get a specific lesson")
    @ApiResponse(responseCode = "200", description = "Lesson found")
    @ApiResponse(responseCode = "404", description = "Lesson or course not found")
    public ResponseEntity<Lesson> getLesson(
            @PathVariable Integer courseId,
            @PathVariable Integer lessonId
    ) {
        Lesson lesson = lessonService.getLesson(courseId, lessonId);
        return ResponseEntity.ok(lesson);
    }

    /**
     * Updates an existing lesson.
     *
     * @param courseId the ID of the course
     * @param lessonId the ID of the lesson to update
     * @param lessonRequestDTO the updated lesson data (validated)
     * @return ResponseEntity with updated lesson (200 OK)
     */
    @PutMapping("/courses/{courseId}/lessons/{lessonId}")
    @Operation(summary = "Update a lesson")
    @ApiResponse(responseCode = "200", description = "Lesson updated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid lesson data")
    @ApiResponse(responseCode = "404", description = "Lesson or course not found")
    public ResponseEntity<Lesson> updateLesson(
            @PathVariable Integer courseId,
            @PathVariable Integer lessonId,
            @Valid @RequestBody LessonRequestDTO lessonRequestDTO
    ) {
        Lesson lesson = new Lesson();
        lesson.setTitle(lessonRequestDTO.getTitle());
        lesson.setVideoUrl(lessonRequestDTO.getVideo_url());
        lesson.setOrderIndex(lessonRequestDTO.getOrder_index());
        lesson.setResourceFile(lessonRequestDTO.getResource_file());
        Lesson updatedLesson = lessonService.updateLesson(courseId, lessonId, lesson);
        return ResponseEntity.ok(updatedLesson);
    }

    /**
     * Deletes a lesson.
     *
     * @param courseId the ID of the course
     * @param lessonId the ID of the lesson to delete
     * @return ResponseEntity with no content (204 No Content)
     */
    @DeleteMapping("/courses/{courseId}/lessons/{lessonId}")
    @Operation(summary = "Delete a lesson")
    @ApiResponse(responseCode = "204", description = "Lesson deleted successfully")
    @ApiResponse(responseCode = "404", description = "Lesson or course not found")
    public ResponseEntity<Void> deleteLesson(
            @PathVariable Integer courseId,
            @PathVariable Integer lessonId
    ) {
        lessonService.deleteLesson(courseId, lessonId);
        return ResponseEntity.noContent().build();
    }
}

