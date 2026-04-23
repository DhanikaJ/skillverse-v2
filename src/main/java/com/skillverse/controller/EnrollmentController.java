package com.skillverse.controller;

import com.skillverse.model.Enrollment;
import com.skillverse.service.EnrollmentService;
import org.springframework.web.bind.annotation.*;

@RestController
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @PostMapping("/enroll/{studentId}/{courseId}")
    public Enrollment enroll(
            @PathVariable Integer studentId,
            @PathVariable Integer courseId
    ) {
        return enrollmentService.enroll(studentId, courseId);
    }
}