package com.skillverse.service;

import com.skillverse.model.Course;
import com.skillverse.repository.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<Course> getCourses() {
        return courseRepository.findAll();
    }

    public void insertCourse(Course course) {
        courseRepository.save(course);
    }

    public Course getCoursesById(Integer id) {
        return courseRepository.findById(id)
                .orElseThrow(()-> new IllegalStateException(id + "Course not found"));
    }
}
