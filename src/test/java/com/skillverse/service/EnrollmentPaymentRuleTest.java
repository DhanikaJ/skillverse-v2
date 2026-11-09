package com.skillverse.service;

import com.skillverse.DataSeeder;
import com.skillverse.dto.EnrollmentDTO;
import com.skillverse.exception.ValidationException;
import com.skillverse.mapper.EntityMapper;
import com.skillverse.model.Course;
import com.skillverse.model.Enrollment;
import com.skillverse.model.Payment;
import com.skillverse.model.Users;
import com.skillverse.repository.CourseRepository;
import com.skillverse.repository.EnrollmentRepository;
import com.skillverse.repository.PaymentRepository;
import com.skillverse.repository.UsersRepository;
import com.skillverse.security.AccessPolicy;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Profile;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class EnrollmentPaymentRuleTest {

    private final EnrollmentRepository enrollmentRepository = mock(EnrollmentRepository.class);
    private final UsersRepository usersRepository = mock(UsersRepository.class);
    private final CourseRepository courseRepository = mock(CourseRepository.class);
    private final EntityMapper entityMapper = mock(EntityMapper.class);
    private final PaymentRepository paymentRepository = mock(PaymentRepository.class);
    private final AccessPolicy accessPolicy = mock(AccessPolicy.class);

    private final Users user = new Users();
    private final Course paidCourse = new Course();

    EnrollmentPaymentRuleTest() {
        user.setId(1);
        paidCourse.setId(5);
        paidCourse.setPrice(10.0);
        when(accessPolicy.currentUser()).thenReturn(user);
        when(courseRepository.findById(5)).thenReturn(Optional.of(paidCourse));
        when(usersRepository.findById(1)).thenReturn(Optional.of(user));
        when(enrollmentRepository.save(any(Enrollment.class))).thenAnswer(i -> i.getArgument(0));
        when(entityMapper.toEnrollmentDTO(any())).thenReturn(mock(EnrollmentDTO.class));
    }

    private EnrollmentService service(boolean requirePayment) {
        return new EnrollmentService(enrollmentRepository, usersRepository, courseRepository,
                entityMapper, paymentRepository, accessPolicy, requirePayment);
    }

    @Test
    void paidCourseNeedsASuccessfulPaymentWhenEnforced() {
        when(paymentRepository.findByUserAndCourse(user, paidCourse)).thenReturn(List.of());
        assertThrows(ValidationException.class, () -> service(true).enrollCurrentUser(5));

        Payment pending = new Payment();
        pending.setStatus("PENDING");
        when(paymentRepository.findByUserAndCourse(user, paidCourse)).thenReturn(List.of(pending));
        assertThrows(ValidationException.class, () -> service(true).enrollCurrentUser(5));
    }

    @Test
    void successfulPaymentUnlocksEnrollment() {
        Payment paid = new Payment();
        paid.setStatus("SUCCESS");
        when(paymentRepository.findByUserAndCourse(user, paidCourse)).thenReturn(List.of(paid));
        assertNotNull(service(true).enrollCurrentUser(5));
    }

    @Test
    void sandboxModeAllowsDirectEnrollment() {
        assertDoesNotThrow(() -> service(false).enrollCurrentUser(5));
    }

    @Test
    void seedDataIsNeverActiveInProduction() {
        Profile profile = DataSeeder.class.getAnnotation(Profile.class);
        assertNotNull(profile, "DataSeeder must be restricted to specific profiles");
        assertArrayEquals(new String[]{"dev", "test"}, profile.value());
    }
}
