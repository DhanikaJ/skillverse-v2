package com.skillverse.controller;

import com.skillverse.dto.EnrollmentDTO;
import com.skillverse.exception.ResourceNotFoundException;
import com.skillverse.model.Users;
import com.skillverse.repository.UsersRepository;
import com.skillverse.service.EnrollmentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * REST Controller for managing student enrollments.
 * Handles enrollment operations between students and courses.
 */
@RestController
@RequestMapping("/api/v1")
@Tag(name = "Enrollments", description = "Enrollment management endpoints")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;
    private final UsersRepository usersRepository;

    public EnrollmentController(EnrollmentService enrollmentService, UsersRepository usersRepository) {
        this.enrollmentService = enrollmentService;
        this.usersRepository = usersRepository;
    }

    /**
     * Enrolls the currently authenticated student in a course.
     * Handles POST /api/v1/enroll/{courseId}
     *
     * @param courseId the ID of the course
     * @return ResponseEntity with enrollment data (201 Created) or error
     */
    @PostMapping("/enroll/{courseId}")
    @Operation(summary = "Enroll current student in a course")
    @ApiResponse(responseCode = "201", description = "Student enrolled successfully")
    @ApiResponse(responseCode = "400", description = "Already enrolled or invalid data")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "404", description = "Course not found")
    public ResponseEntity<EnrollmentDTO> enrollCurrentStudent(@PathVariable Integer courseId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Users user = usersRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        
        EnrollmentDTO enrollment = enrollmentService.enrollStudent(user.getId(), courseId);
        return ResponseEntity.status(HttpStatus.CREATED).body(enrollment);
    }

    /**
     * Enrolls a student in a course by ID.
     * Handles POST /api/v1/enrollments/enroll/{studentId}/{courseId}
     *
     * @param studentId the ID of the student
     * @param courseId the ID of the course
     * @return ResponseEntity with enrollment data (201 Created) or error
     */
    @PostMapping("/enrollments/enroll/{studentId}/{courseId}")
    @Operation(summary = "Enroll a student in a course by ID")
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
    @GetMapping("/enrollments/user/{userId}")
    @Operation(summary = "Get all enrollments for a user")
    @ApiResponse(responseCode = "200", description = "Enrollments retrieved successfully")
    @ApiResponse(responseCode = "404", description = "User not found")
    public ResponseEntity<List<EnrollmentDTO>> getUserEnrollments(@PathVariable Integer userId) {
        List<EnrollmentDTO> enrollments = enrollmentService.getEnrollmentsByUserId(userId);
        return ResponseEntity.ok(enrollments);
    }

    /**
     * Retrieves all enrollments for a course.
     *
     * @param courseId the ID of the course
     * @return ResponseEntity with list of enrollment data (200 OK)
     */
    @GetMapping("/enrollments/course/{courseId}")
    @Operation(summary = "Get all enrollments for a course")
    @ApiResponse(responseCode = "200", description = "Enrollments retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Course not found")
    public ResponseEntity<List<EnrollmentDTO>> getCourseEnrollments(@PathVariable Integer courseId) {
        List<EnrollmentDTO> enrollments = enrollmentService.getEnrollmentsByCourseId(courseId);
        return ResponseEntity.ok(enrollments);
    }

    /**
     * Retrieves all enrollments for the currently authenticated user.
     * Handles GET /api/v1/users/me/enrollments
     *
     * @return ResponseEntity with list of enrollment data (200 OK)
     */
    @GetMapping("/users/me/enrollments")
    @Operation(summary = "Get all enrollments for the current user")
    @ApiResponse(responseCode = "200", description = "Enrollments retrieved successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public ResponseEntity<List<EnrollmentDTO>> getCurrentUserEnrollments() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Users user = usersRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        
        List<EnrollmentDTO> enrollments = enrollmentService.getEnrollmentsByUserId(user.getId());
        return ResponseEntity.ok(enrollments);
    }
}