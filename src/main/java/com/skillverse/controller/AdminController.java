package com.skillverse.controller;

import com.skillverse.dto.CourseStatsResponse;
import com.skillverse.dto.UserDTO;
import com.skillverse.service.AdminService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@Tag(name = "Admin", description = "Admin management endpoints")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/users")
    public List<UserDTO> getAllUsers() {
        return adminService.getAllUsers();
    }

    @GetMapping("/courses/stats")
    public CourseStatsResponse getCourseStats() {
        return adminService.getCourseStats();
    }
}

