package com.skillverse.controller;

import com.skillverse.dto.CourseStatsResponse;
import com.skillverse.dto.UserDTO;
import com.skillverse.service.AdminService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * REST Controller for administrative operations.
 * Handles admin endpoints for viewing users and platform statistics.
 */
@RestController
@RequestMapping("/api/v1/admin")
@Tag(name = "Admin", description = "Admin management endpoints")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    /**
     * Retrieves all users in the system.
     *
     * @return ResponseEntity with list of all users (200 OK)
     */
    @GetMapping("/users")
    @Operation(summary = "Get all users (admin)")
    @ApiResponse(responseCode = "200", description = "Users retrieved successfully")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    /**
     * Retrieves course statistics including total courses and enrollments.
     *
     * @return ResponseEntity with course statistics (200 OK)
     */
    @GetMapping("/courses/stats")
    @Operation(summary = "Get course statistics")
    @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully")
    public ResponseEntity<CourseStatsResponse> getCourseStats() {
        return ResponseEntity.ok(adminService.getCourseStats());
    }
}

