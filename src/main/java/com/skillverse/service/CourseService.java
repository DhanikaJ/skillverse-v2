package com.skillverse.service;

import com.skillverse.dto.CourseDTO;
import com.skillverse.exception.ResourceNotFoundException;
import com.skillverse.mapper.EntityMapper;
import com.skillverse.model.Course;
import com.skillverse.repository.CourseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final EntityMapper entityMapper;

    public CourseService(CourseRepository courseRepository, EntityMapper entityMapper) {
        this.courseRepository = courseRepository;
        this.entityMapper = entityMapper;
    }

    public Page<CourseDTO> getCourses(Pageable pageable) {
        Page<Course> courses = courseRepository.findAll(pageable);
        return courses.map(entityMapper::toCourseDTO);
    }

    public List<CourseDTO> getAllCourses() {
        return entityMapper.toCourseDTOList(courseRepository.findAll());
    }

    public void insertCourse(Course course) {
        courseRepository.save(course);
    }

    public CourseDTO getCoursesById(Integer id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Course not found with ID: " + id));
        return entityMapper.toCourseDTO(course);
    }
}
