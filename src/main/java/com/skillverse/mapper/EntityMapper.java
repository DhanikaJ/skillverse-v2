package com.skillverse.mapper;

import com.skillverse.dto.CourseDTO;
import com.skillverse.dto.EnrollmentDTO;
import com.skillverse.dto.UserDTO;
import com.skillverse.model.Course;
import com.skillverse.model.Enrollment;
import com.skillverse.model.Users;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Component for mapping between entity and DTO objects.
 * Provides conversion methods for entity-to-DTO and DTO-to-entity transformations.
 */
@Component
public class EntityMapper {

    /**
     * Converts a Users entity to a UserDTO.
     *
     * @param user the Users entity
     * @return the UserDTO object, or null if the input is null
     */
    public UserDTO toUserDTO(Users user) {
        if (user == null) {
            return null;
        }
        return new UserDTO(
                user.getId(),
                user.getFname(),
                user.getLname(),
                user.getEmail(),
                user.getPhoto(),
                user.getCreated_at()
        );
    }

    /**
     * Converts a list of Users entities to a list of UserDTOs.
     *
     * @param users the list of Users entities
     * @return a list of UserDTO objects
     */
    public List<UserDTO> toUserDTOList(List<Users> users) {
        return users.stream()
                .map(this::toUserDTO)
                .collect(Collectors.toList());
    }

    /**
     * Converts a Course entity to a CourseDTO.
     *
     * @param course the Course entity
     * @return the CourseDTO object, or null if the input is null
     */
    public CourseDTO toCourseDTO(Course course) {
        if (course == null) {
            return null;
        }
        return new CourseDTO(
                course.getId(),
                course.getTitle(),
                course.getDescription(),
                course.getPricelevel(),
                course.getDifficulty(),
                course.getPrice(),
                course.getThumbnail(),
                course.getCreated_at()
        );
    }

    /**
     * Converts a list of Course entities to a list of CourseDTOs.
     *
     * @param courses the list of Course entities
     * @return a list of CourseDTO objects
     */
    public List<CourseDTO> toCourseDTOList(List<Course> courses) {
        return courses.stream()
                .map(this::toCourseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Converts an Enrollment entity to an EnrollmentDTO.
     *
     * @param enrollment the Enrollment entity
     * @return the EnrollmentDTO object, or null if the input is null
     */
    public EnrollmentDTO toEnrollmentDTO(Enrollment enrollment) {
        if (enrollment == null) {
            return null;
        }
        return new EnrollmentDTO(
                enrollment.getId(),
                enrollment.getUser().getId(),
                enrollment.getUser().getFname() + " " + enrollment.getUser().getLname(),
                enrollment.getCourse().getId(),
                enrollment.getCourse().getTitle(),
                enrollment.getEnrolled_at(),
                enrollment.getProgress(),
                enrollment.getStatus() != null ? enrollment.getStatus().getType() : "active"
        );
    }

    /**
     * Converts a list of Enrollment entities to a list of EnrollmentDTOs.
     *
     * @param enrollments the list of Enrollment entities
     * @return a list of EnrollmentDTO objects
     */
    public List<EnrollmentDTO> toEnrollmentDTOList(List<Enrollment> enrollments) {
        return enrollments.stream()
                .map(this::toEnrollmentDTO)
                .collect(Collectors.toList());
    }

    /**
     * Converts a UserDTO to a Users entity.
     *
     * @param dto the UserDTO object
     * @return the Users entity, or null if the input is null
     */
    public Users toUserEntity(UserDTO dto) {
        if (dto == null) {
            return null;
        }
        Users user = new Users();
        user.setId(dto.getId());
        user.setFname(dto.getFname());
        user.setLname(dto.getLname());
        user.setEmail(dto.getEmail());
        user.setPhoto(dto.getPhone());
        user.setCreated_at(dto.getCreated_at());
        return user;
    }

    /**
     * Converts a CourseDTO to a Course entity.
     *
     * @param dto the CourseDTO object
     * @return the Course entity, or null if the input is null
     */
    public Course toCourseEntity(CourseDTO dto) {
        if (dto == null) {
            return null;
        }
        Course course = new Course();
        course.setId(dto.getId());
        course.setTitle(dto.getTitle());
        course.setDescription(dto.getDescription());
        course.setPricelevel(dto.getPricelevel());
        course.setDifficulty(dto.getDifficulty());
        course.setPrice(dto.getPrice());
        course.setThumbnail(dto.getThumbnail());
        course.setCreated_at(dto.getCreated_at());
        return course;
    }
}




