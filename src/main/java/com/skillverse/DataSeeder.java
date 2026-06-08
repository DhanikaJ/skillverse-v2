package com.skillverse;

import com.skillverse.model.*;
import com.skillverse.repository.CourseRepository;
import com.skillverse.repository.EnrollmentRepository;
import com.skillverse.repository.UsersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataSeeder.class);

    private final UsersRepository usersRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UsersRepository usersRepository,
                      CourseRepository courseRepository,
                      EnrollmentRepository enrollmentRepository,
                      PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        logger.info("Starting data seeding...");
        
        try {
            // 1. Create Instructors
            Users instructor1 = createUserIfNotFound("dr.smith@skillverse.com", "John", "Smith", Role.INSTRUCTOR);
            Users instructor2 = createUserIfNotFound("prof.jones@skillverse.com", "Sarah", "Jones", Role.INSTRUCTOR);

            // 2. Create Students
            Users student1 = createUserIfNotFound("alice.student@example.com", "Alice", "Wonderland", Role.STUDENT);
            Users student2 = createUserIfNotFound("bob.tester@example.com", "Bob", "Builder", Role.STUDENT);
            Users student3 = createUserIfNotFound("charlie.learner@example.com", "Charlie", "Brown", Role.STUDENT);

            // 3. Create Courses
            Course course1 = createCourseIfNotFound("Advanced Java Architecture", 
                "In-depth guide to microservices, design patterns, and high-performance Java applications.", 
                99.99, "ADVANCED", instructor1);
            
            Course course2 = createCourseIfNotFound("Web Development Bootcamp 2026", 
                "From zero to hero: HTML, CSS, JavaScript, React, and Node.js.", 
                49.50, "BEGINNER", instructor2);
                
            Course course3 = createCourseIfNotFound("Python for Data Science", 
                "Master Pandas, NumPy, and Scikit-Learn with real-world datasets.", 
                75.00, "INTERMEDIATE", instructor1);
                
            Course course4 = createCourseIfNotFound("UX/UI Design Fundamentals", 
                "Learn the principles of user-centered design and master Figma.", 
                29.99, "BEGINNER", instructor2);
                
            Course course5 = createCourseIfNotFound("Cloud Computing with AWS", 
                "Scale your applications using AWS Lambda, S3, and DynamoDB.", 
                120.00, "INTERMEDIATE", instructor1);

            // 4. Create Enrollments
            createEnrollmentIfNotFound(student1, course1, 25.5);
            createEnrollmentIfNotFound(student1, course2, 10.0);
            createEnrollmentIfNotFound(student2, course2, 85.0);
            createEnrollmentIfNotFound(student2, course4, 0.0);
            createEnrollmentIfNotFound(student3, course3, 45.0);
            createEnrollmentIfNotFound(student3, course5, 12.0);

            logger.info("Data seeding completed successfully!");
        } catch (Exception e) {
            logger.error("Error during data seeding: {}", e.getMessage(), e);
        }
    }

    private Users createUserIfNotFound(String email, String fname, String lname, Role role) {
        Optional<Users> existingUser = usersRepository.findByEmail(email);
        if (existingUser.isPresent()) {
            return existingUser.get();
        }

        Users user = new Users();
        user.setEmail(email);
        user.setFname(fname);
        user.setLname(lname);
        user.setPassword_hash(passwordEncoder.encode("password123"));
        user.setRole(role);
        user.setCreated_at(new Date());
        
        Users savedUser = usersRepository.save(user);
        logger.info("Seeded user: {}", email);
        return savedUser;
    }

    private Course createCourseIfNotFound(String title, String description, double price, String difficulty, Users instructor) {
        Optional<Course> existingCourse = courseRepository.findByTitle(title);
        if (existingCourse.isPresent()) {
            return existingCourse.get();
        }

        Course course = new Course();
        course.setTitle(title);
        course.setDescription(description);
        course.setPrice(price);
        course.setDifficulty(difficulty);
        course.setPricelevel(difficulty); // Using difficulty as pricelevel for consistency with DataInitializationConfig
        course.setUser(instructor);
        course.setCreated_at(new Date());
        course.setThumbnail("https://via.placeholder.com/300?text=" + title.replace(" ", "+"));
        
        Course savedCourse = courseRepository.save(course);
        logger.info("Seeded course: {}", title);
        return savedCourse;
    }

    private void createEnrollmentIfNotFound(Users student, Course course, double progress) {
        if (enrollmentRepository.existsByUser_IdAndCourse_Id(student.getId(), course.getId())) {
            return;
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setUser(student);
        enrollment.setCourse(course);
        enrollment.setProgress(progress);
        enrollment.setEnrolled_at(new Date());
        
        enrollmentRepository.save(enrollment);
        logger.info("Seeded enrollment: {} in {}", student.getEmail(), course.getTitle());
    }
}
