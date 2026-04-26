package com.skillverse.controller;

import com.skillverse.dto.CourseDTO;
import com.skillverse.model.Course;
import com.skillverse.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/courses")
public class CourseController {
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public Page<CourseDTO> getCourses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        Pageable pageable = PageRequest.of(page, size);
        return courseService.getCourses(pageable);
    }

    @PostMapping
    public void addNewCourse(@Valid @RequestBody Course course){
        courseService.insertCourse(course);
    }

    @GetMapping("{id}")
    public CourseDTO getCourseById(@PathVariable Integer id){
        return courseService.getCoursesById(id);
    }
}




