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

@Component
public class EntityMapper {

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

    public List<UserDTO> toUserDTOList(List<Users> users) {
        return users.stream()
                .map(this::toUserDTO)
                .collect(Collectors.toList());
    }

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

    public List<CourseDTO> toCourseDTOList(List<Course> courses) {
        return courses.stream()
                .map(this::toCourseDTO)
                .collect(Collectors.toList());
    }

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

    public List<EnrollmentDTO> toEnrollmentDTOList(List<Enrollment> enrollments) {
        return enrollments.stream()
                .map(this::toEnrollmentDTO)
                .collect(Collectors.toList());
    }

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




