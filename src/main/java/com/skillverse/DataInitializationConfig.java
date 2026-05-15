package com.skillverse;

import com.skillverse.model.*;
import com.skillverse.repository.CourseRepository;
import com.skillverse.repository.PaymentMethodRepository;
import com.skillverse.repository.UsersRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Configuration class for initializing application data.
 * Ensures required payment methods, users, and courses are available in the database on startup.
 */
@Configuration
public class DataInitializationConfig {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializationConfig.class);
    private static final String[] PAYMENT_METHODS = {"PayHere", "Credit Card", "Debit Card", "PayPal", "Bank Transfer"};

    private final PaymentMethodRepository paymentMethodRepository;
    private final UsersRepository usersRepository;
    private final CourseRepository courseRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializationConfig(PaymentMethodRepository paymentMethodRepository,
                                    UsersRepository usersRepository,
                                    CourseRepository courseRepository,
                                    PasswordEncoder passwordEncoder) {
        this.paymentMethodRepository = paymentMethodRepository;
        this.usersRepository = usersRepository;
        this.courseRepository = courseRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Initializes application data on startup.
     * Creates default payment methods, users, and courses if they don't already exist.
     *
     * @return an ApplicationRunner that performs data initialization
     */
    @Bean
    public ApplicationRunner initializeData() {
        return args -> {
            try {
                initializePaymentMethods();
                initializeUsers();
                initializeCourses();
                logger.info("Data initialization completed successfully");
            } catch (Exception e) {
                logger.error("Error during data initialization", e);
            }
        };
    }

    /**
     * Initializes default payment methods in the database.
     * Skips methods that already exist.
     */
    private void initializePaymentMethods() {
        for (String method : PAYMENT_METHODS) {
            if (paymentMethodRepository.findByMethod(method) == null) {
                PaymentMethod paymentMethod = new PaymentMethod();
                paymentMethod.setMethod(method);
                paymentMethodRepository.save(paymentMethod);
                logger.info("Created payment method: {}", method);
            }
        }
    }

    /**
     * Initializes default users with different roles.
     * Skips users that already exist by email.
     */
    private void initializeUsers() {
        String[][] users = {
            {"instructor@example.com", "INSTRUCTOR"},
            {"student@example.com", "STUDENT"},
            {"admin@example.com", "ADMIN"}
        };

        for (String[] user : users) {
            String email = user[0];
            if (usersRepository.findByEmail(email).isEmpty()) {
                Users newUser = new Users();
                newUser.setEmail(email);
                // BCrypt hashed password for "password123"
                newUser.setPassword_hash(passwordEncoder.encode("password123"));
                newUser.setRole(Role.valueOf(user[1]));
                newUser.setCreated_at(new Date());
                usersRepository.save(newUser);
                logger.info("Created user: {} with role: {}", email, user[1]);
            }
        }
    }

    /**
     * Initializes seeded courses if they don't already exist.
     */
    private void initializeCourses() {
        // Get the instructor user
        Users instructor = usersRepository.findByEmail("instructor@example.com")
            .orElse(null);

        if (instructor == null) {
            logger.warn("Instructor user not found, skipping course initialization");
            return;
        }

        String[][] courses = {
            {"Java Spring Boot Mastery", "Learn to build enterprise applications with Spring Boot",
             "INTERMEDIATE", "INTERMEDIATE", "29.99", "https://via.placeholder.com/300?text=Spring+Boot"},
            {"React JS Advanced", "Master modern React with hooks, state management, and performance optimization",
             "ADVANCED", "ADVANCED", "39.99", "https://via.placeholder.com/300?text=React"},
            {"Python Data Science", "Complete guide to data analysis, visualization, and machine learning with Python",
             "BEGINNER", "BEGINNER", "19.99", "https://via.placeholder.com/300?text=Python"}
        };

        for (String[] courseData : courses) {
            if (courseRepository.findByTitle(courseData[0]).isEmpty()) {
                Course course = new Course();
                course.setTitle(courseData[0]);
                course.setDescription(courseData[1]);
                course.setPricelevel(courseData[2]);
                course.setDifficulty(courseData[3]);
                course.setPrice(Double.parseDouble(courseData[4]));
                course.setThumbnail(courseData[5]);
                course.setUser(instructor);
                course.setCreated_at(new Date());
                courseRepository.save(course);
                logger.info("Created course: {}", courseData[0]);
            }
        }
    }
}

