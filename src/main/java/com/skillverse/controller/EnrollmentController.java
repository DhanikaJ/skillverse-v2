package com.skillverse.controller;

import com.skillverse.dto.EnrollmentDTO;
import com.skillverse.service.EnrollmentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * REST Controller for managing student enrollments.
 * Handles enrollment operations between students and courses.
 */
@RestController
@RequestMapping("/api/v1/enrollments")
@Tag(name = "Enrollments", description = "Enrollment management endpoints")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    /**
     * Enrolls a student in a course.
     *
     * @param studentId the ID of the student
     * @param courseId the ID of the course
     * @return ResponseEntity with enrollment data (201 Created) or error
     */
    @PostMapping("/enroll/{studentId}/{courseId}")
    @Operation(summary = "Enroll a student in a course")
    @ApiResponse(responseCode = "201", description = "Student enrolled successfully")
    @ApiResponse(responseCode = "400", description = "Already enrolled or invalid data")
    @ApiResponse(responseCode = "404", description = "Student or course not found")
    public ResponseEntity<EnrollmentDTO> enrollStudent(
            @PathVariable Integer studentId,
            @PathVariable Integer courseId
    ) {
        EnrollmentDTO enrollment = enrollmentService.enrollStudent(studentId, courseId);
        return ResponseEntity.status(HttpStatus.CREATED).body(enrollment);
    }

    /**
     * Retrieves all enrollments for a user.
     *
     * @param userId the ID of the user
     * @return ResponseEntity with list of enrollment data (200 OK)
     */
    @GetMapping("/user/{userId}")
    @Operation(summary = "Get all enrollments for a user")
    @ApiResponse(responseCode = "200", description = "Enrollments retrieved successfully")
    @ApiResponse(responseCode = "404", description = "User not found")
    public ResponseEntity<List<EnrollmentDTO>> getUserEnrollments(@PathVariable Integer userId) {
        List<EnrollmentDTO> enrollments = enrollmentService.getEnrollmentsByUserId(userId);
        return ResponseEntity.ok(enrollments);
    }
}