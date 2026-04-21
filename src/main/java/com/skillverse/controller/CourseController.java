package com.skillverse.controller;

import com.skillverse.model.Course;
import com.skillverse.service.CourseService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/courses")
public class CourseController {
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public List<Course> getCourses(){
        return courseService.getCourses();
    }

    @PostMapping
    public void addNewCourse(@RequestBody Course course){
        courseService.insertCourse(course);
    }

    @GetMapping("{id}")
    public Course getCourseById(@PathVariable Integer id){
        return courseService.getCoursesById(id);
    }

}
