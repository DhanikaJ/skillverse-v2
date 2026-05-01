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

/**
 * Service class for managing Course operations.
 * Provides functionality for retrieving, creating, updating, and managing courses.
 */
@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final EntityMapper entityMapper;

    public CourseService(CourseRepository courseRepository, EntityMapper entityMapper) {
        this.courseRepository = courseRepository;
        this.entityMapper = entityMapper;
    }

    /**
     * Retrieves all courses.
     *
     * @return a list of all CourseDTO objects
     */
    public List<CourseDTO> getCourses() {
        return entityMapper.toCourseDTOList(courseRepository.findAll());
    }

    /**
     * Retrieves courses with pagination support.
     *
     * @param pageable the pagination configuration
     * @return a page of CourseDTO objects
     */
    public Page<CourseDTO> getCoursesWithPagination(Pageable pageable) {
        return courseRepository.findAll(pageable)
                .map(entityMapper::toCourseDTO);
    }

    /**
     * Creates and saves a new course.
     *
     * @param course the course entity to create
     */
    public void createCourse(Course course) {
        courseRepository.save(course);
    }

    /**
     * Retrieves a course by its ID.
     *
     * @param id the course ID
     * @return the CourseDTO object
     * @throws ResourceNotFoundException if the course is not found
     */
    public CourseDTO getCourseById(Integer id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with ID: " + id));
        return entityMapper.toCourseDTO(course);
    }

    /**
     * Updates an existing course.
     *
     * @param id the course ID to update
     * @param courseRequest the updated course data
     * @return the updated CourseDTO object
     * @throws ResourceNotFoundException if the course is not found
     */
    public CourseDTO updateCourse(Integer id, Course courseRequest) {
        Course existingCourse = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with ID: " + id));

        existingCourse.setTitle(courseRequest.getTitle());
        existingCourse.setDescription(courseRequest.getDescription());
        existingCourse.setPricelevel(courseRequest.getPricelevel());
        existingCourse.setDifficulty(courseRequest.getDifficulty());
        existingCourse.setPrice(courseRequest.getPrice());
        existingCourse.setThumbnail(courseRequest.getThumbnail());

        Course updatedCourse = courseRepository.save(existingCourse);
        return entityMapper.toCourseDTO(updatedCourse);
    }

    /**
     * Deletes a course by its ID.
     *
     * @param id the course ID to delete
     * @throws ResourceNotFoundException if the course is not found
     */
    public void deleteCourse(Integer id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with ID: " + id));
        courseRepository.delete(course);
    }
}
