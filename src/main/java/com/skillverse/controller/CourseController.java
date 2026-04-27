package com.skillverse.controller;

import com.skillverse.dto.CourseDTO;
import com.skillverse.dto.CourseRequestDTO;
import com.skillverse.model.Course;
import com.skillverse.service.CourseService;
import jakarta.validation.Valid;
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
    public List<CourseDTO> getCourses(){
        return courseService.getCourses();
    }

    @PostMapping
    public void addNewCourse(@Valid @RequestBody CourseRequestDTO courseRequestDTO){
        Course course = new Course();
        course.setTitle(courseRequestDTO.getTitle());
        course.setDescription(courseRequestDTO.getDescription());
        course.setPricelevel(courseRequestDTO.getPricelevel());
        course.setDifficulty(courseRequestDTO.getDifficulty());
        course.setPrice(courseRequestDTO.getPrice());
        course.setThumbnail(courseRequestDTO.getThumbnail());
        courseService.insertCourse(course);
    }

    @GetMapping("{id}")
    public CourseDTO getCourseById(@PathVariable Integer id){
        return courseService.getCoursesById(id);
    }

}
