package com.skillverse.controller;

import com.skillverse.dto.CourseDTO;
import com.skillverse.dto.CourseRequestDTO;
import com.skillverse.model.Course;
import com.skillverse.service.CourseService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for managing courses.
 * Handles CRUD operations and query endpoints for courses.
 */
@RestController
@RequestMapping("/api/v1/courses")
@Tag(name = "Courses", description = "Course management endpoints")
public class CourseController {
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    /**
     * Retrieves all courses with pagination.
     *
     * @param page the page number (default: 0)
     * @param size the page size (default: 10)
     * @return ResponseEntity with paginated course data (200 OK)
     */
    @GetMapping
    @Operation(summary = "Get all courses with pagination")
    @ApiResponse(responseCode = "200", description = "Courses retrieved successfully")
    public ResponseEntity<Page<CourseDTO>> getCourses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(courseService.getCoursesWithPagination(pageable));
    }

    /**
     * Creates a new course.
     *
     * @param courseRequestDTO the course data to create (validated)
     * @return ResponseEntity with created course data (201 Created)
     */
    @PostMapping
    @Operation(summary = "Create a new course")
    @ApiResponse(responseCode = "201", description = "Course created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid course data")
    public ResponseEntity<CourseDTO> createCourse(@Valid @RequestBody CourseRequestDTO courseRequestDTO) {
        Course course = new Course();
        course.setTitle(courseRequestDTO.getTitle());
        course.setDescription(courseRequestDTO.getDescription());
        course.setPricelevel(courseRequestDTO.getPricelevel());
        course.setDifficulty(courseRequestDTO.getDifficulty());
        course.setPrice(courseRequestDTO.getPrice());
        course.setThumbnail(courseRequestDTO.getThumbnail());
        courseService.createCourse(course);
        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.getCourseById(course.getId()));
    }

    /**
     * Retrieves a course by its ID.
     *
     * @param id the course ID
     * @return ResponseEntity with course data (200 OK) or (404 Not Found)
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get course by ID")
    @ApiResponse(responseCode = "200", description = "Course found")
    @ApiResponse(responseCode = "404", description = "Course not found")
    public ResponseEntity<CourseDTO> getCourseById(@PathVariable Integer id) {
        return ResponseEntity.ok(courseService.getCourseById(id));
    }

}
