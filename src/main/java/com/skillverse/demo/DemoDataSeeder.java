package com.skillverse.demo;

import com.skillverse.model.Course;
import com.skillverse.model.Enrollment;
import com.skillverse.model.Lesson;
import com.skillverse.model.OrderItem;
import com.skillverse.model.Orders;
import com.skillverse.model.Payment;
import com.skillverse.model.PaymentMethod;
import com.skillverse.model.Quiz;
import com.skillverse.model.QuizQuestion;
import com.skillverse.model.Role;
import com.skillverse.model.Users;
import com.skillverse.repository.CourseRepository;
import com.skillverse.repository.EnrollmentRepository;
import com.skillverse.repository.LessonRepository;
import com.skillverse.repository.OrdersRepository;
import com.skillverse.repository.PaymentMethodRepository;
import com.skillverse.repository.PaymentRepository;
import com.skillverse.repository.QuizRepository;
import com.skillverse.repository.UsersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Seeds the sandbox showcase: three public demo accounts (student, instructor, administrator)
 * plus realistic supporting data so every screen has something to show.
 *
 * Active in any profile, but only when {@code app.demo.enabled=true} ({@code DEMO_MODE}).
 * Seeding is idempotent: existing rows are reused, so restarting never duplicates data.
 * Supporting faculty and learner accounts are locked (they cannot sign in); only the three
 * demo accounts share the public demo password.
 */
@Component
@ConditionalOnProperty(name = "app.demo.enabled", havingValue = "true")
public class DemoDataSeeder implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DemoDataSeeder.class);

    /** Not a valid BCrypt hash, so these accounts can never authenticate. */
    private static final String LOCKED_PASSWORD_HASH = "!disabled";

    private static final String ETHAN = "ethan.brooks@example.com";
    private static final String MAYA = "maya.fernando@example.com";

    private static final String SPRING = "Spring Boot REST APIs in Practice";
    private static final String REACT = "React and TypeScript Fundamentals";
    private static final String POSTGRES = "PostgreSQL for Application Developers";
    private static final String SECURITY = "Secure APIs with Spring Security and JWT";
    private static final String DOCKER = "Docker and Deployment Essentials";
    private static final String CLEAN = "Clean Code and Testing Habits";
    private static final String WEB = "Introduction to Web Development";
    private static final String PYTHON = "Data Analysis with Python";

    private record CourseSpec(String title, String description, double price, String difficulty,
                              String ownerEmail, List<String> lessons) {
    }

    private record LearnerSpec(String fname, String lname, String email) {
    }

    private record EnrollmentSpec(String learnerEmail, String courseTitle, double progress, int daysAgo) {
    }

    private static final List<CourseSpec> COURSES = List.of(
            new CourseSpec(SPRING,
                    "Design, build and test a production-style REST API with Spring Boot, JPA and validation.",
                    49.00, "INTERMEDIATE", DemoProperties.INSTRUCTOR_EMAIL,
                    List.of("Project setup and layered architecture", "Controllers, DTOs and validation",
                            "Persistence with Spring Data JPA", "Error handling and API documentation")),
            new CourseSpec(REACT,
                    "Build typed React interfaces: components, hooks, routing and API integration.",
                    39.00, "BEGINNER", DemoProperties.INSTRUCTOR_EMAIL,
                    List.of("Components, props and JSX", "State and effects with hooks",
                            "Routing and protected pages", "Calling an API with Axios")),
            new CourseSpec(POSTGRES,
                    "Model data, write efficient queries and understand indexes, joins and transactions.",
                    29.00, "BEGINNER", DemoProperties.INSTRUCTOR_EMAIL,
                    List.of("Relational modelling basics", "Joins and aggregations",
                            "Indexes and query plans", "Transactions and isolation")),
            new CourseSpec(SECURITY,
                    "Protect an API with BCrypt, stateless JWT authentication and role-based access control.",
                    59.00, "ADVANCED", DemoProperties.INSTRUCTOR_EMAIL,
                    List.of("Authentication versus authorization", "Issuing and validating JWTs",
                            "Method security and ownership rules", "Testing secured endpoints")),
            new CourseSpec(DOCKER,
                    "Containerise an application, run it locally with Compose and deploy it to a cloud host.",
                    35.00, "BEGINNER", ETHAN,
                    List.of("Images and containers", "Docker Compose for local development",
                            "Environment-based configuration", "Deploying to a managed platform")),
            new CourseSpec(CLEAN,
                    "Habits for readable code and a pragmatic testing strategy for backend services.",
                    25.00, "INTERMEDIATE", ETHAN,
                    List.of("Naming and small functions", "Unit versus integration tests",
                            "Refactoring safely", "Code review practices")),
            new CourseSpec(WEB,
                    "A free starter course covering HTML, CSS and the basics of JavaScript.",
                    0.00, "BEGINNER", MAYA,
                    List.of("How the web works", "HTML structure and semantics",
                            "Styling with CSS", "Your first JavaScript")),
            new CourseSpec(PYTHON,
                    "Clean, analyse and visualise data using Python, pandas and notebooks.",
                    45.00, "INTERMEDIATE", MAYA,
                    List.of("Python refresher for analysts", "Working with DataFrames",
                            "Cleaning messy data", "Charts and storytelling"))
    );

    private static final List<LearnerSpec> LEARNERS = List.of(
            new LearnerSpec("Nimal", "Perera", "nimal.perera@example.com"),
            new LearnerSpec("Sara", "Khan", "sara.khan@example.com"),
            new LearnerSpec("Liam", "Wong", "liam.wong@example.com"),
            new LearnerSpec("Aisha", "Rahman", "aisha.rahman@example.com"),
            new LearnerSpec("Tom", "Becker", "tom.becker@example.com"),
            new LearnerSpec("Elena", "Rossi", "elena.rossi@example.com")
    );

    private static final List<EnrollmentSpec> LEARNER_ENROLLMENTS = List.of(
            new EnrollmentSpec("nimal.perera@example.com", SPRING, 100, 60),
            new EnrollmentSpec("nimal.perera@example.com", SECURITY, 30, 10),
            new EnrollmentSpec("nimal.perera@example.com", POSTGRES, 80, 30),
            new EnrollmentSpec("sara.khan@example.com", REACT, 55, 25),
            new EnrollmentSpec("sara.khan@example.com", PYTHON, 15, 3),
            new EnrollmentSpec("liam.wong@example.com", DOCKER, 100, 50),
            new EnrollmentSpec("liam.wong@example.com", CLEAN, 40, 12),
            new EnrollmentSpec("aisha.rahman@example.com", SPRING, 20, 6),
            new EnrollmentSpec("aisha.rahman@example.com", WEB, 90, 45),
            new EnrollmentSpec("aisha.rahman@example.com", PYTHON, 70, 20),
            new EnrollmentSpec("tom.becker@example.com", SECURITY, 10, 2),
            new EnrollmentSpec("tom.becker@example.com", POSTGRES, 45, 15),
            new EnrollmentSpec("elena.rossi@example.com", REACT, 100, 35),
            new EnrollmentSpec("elena.rossi@example.com", CLEAN, 60, 18),
            new EnrollmentSpec("elena.rossi@example.com", WEB, 35, 8)
    );

    /** The demo student's enrollments. The other courses stay open so the enrol flow can be tried. */
    private static final List<EnrollmentSpec> STUDENT_ENROLLMENTS = List.of(
            new EnrollmentSpec(DemoProperties.STUDENT_EMAIL, SPRING, 65, 21),
            new EnrollmentSpec(DemoProperties.STUDENT_EMAIL, REACT, 100, 40),
            new EnrollmentSpec(DemoProperties.STUDENT_EMAIL, WEB, 20, 5)
    );

    private final UsersRepository usersRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final LessonRepository lessonRepository;
    private final QuizRepository quizRepository;
    private final OrdersRepository ordersRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final PasswordEncoder passwordEncoder;
    private final DemoProperties demoProperties;

    public DemoDataSeeder(UsersRepository usersRepository,
                          CourseRepository courseRepository,
                          EnrollmentRepository enrollmentRepository,
                          LessonRepository lessonRepository,
                          QuizRepository quizRepository,
                          OrdersRepository ordersRepository,
                          PaymentRepository paymentRepository,
                          PaymentMethodRepository paymentMethodRepository,
                          PasswordEncoder passwordEncoder,
                          DemoProperties demoProperties) {
        this.usersRepository = usersRepository;
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.lessonRepository = lessonRepository;
        this.quizRepository = quizRepository;
        this.ordersRepository = ordersRepository;
        this.paymentRepository = paymentRepository;
        this.paymentMethodRepository = paymentMethodRepository;
        this.passwordEncoder = passwordEncoder;
        this.demoProperties = demoProperties;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (demoProperties.getPassword().isBlank()) {
            logger.error("Demo mode is enabled but app.demo.password is empty; skipping demo data");
            return;
        }
        logger.info("Seeding demo data...");
        try {
            PaymentMethod payHere = ensurePaymentMethod("PayHere");

            upsertDemoUser(DemoProperties.ADMIN_EMAIL, "Demo", "Admin", Role.ADMIN);
            upsertDemoUser(DemoProperties.INSTRUCTOR_EMAIL, "Demo", "Instructor", Role.INSTRUCTOR);
            Users student = upsertDemoUser(DemoProperties.STUDENT_EMAIL, "Demo", "Student", Role.STUDENT);

            Map<String, Users> people = new LinkedHashMap<>();
            people.put(DemoProperties.INSTRUCTOR_EMAIL, usersRepository.findByEmail(DemoProperties.INSTRUCTOR_EMAIL).orElseThrow());
            people.put(DemoProperties.STUDENT_EMAIL, student);
            people.put(ETHAN, ensureLockedUser(ETHAN, "Ethan", "Brooks", Role.INSTRUCTOR));
            people.put(MAYA, ensureLockedUser(MAYA, "Maya", "Fernando", Role.INSTRUCTOR));
            for (LearnerSpec learner : LEARNERS) {
                people.put(learner.email(), ensureLockedUser(learner.email(), learner.fname(), learner.lname(), Role.STUDENT));
            }

            Map<String, Course> courses = new LinkedHashMap<>();
            for (CourseSpec spec : COURSES) {
                Course course = ensureCourse(spec, people.get(spec.ownerEmail()));
                ensureLessons(course, spec.lessons());
                courses.put(spec.title(), course);
            }

            ensureQuizzes(courses);

            List<EnrollmentSpec> allEnrollments = new ArrayList<>(STUDENT_ENROLLMENTS);
            allEnrollments.addAll(LEARNER_ENROLLMENTS);
            for (EnrollmentSpec spec : allEnrollments) {
                Users user = people.get(spec.learnerEmail());
                Course course = courses.get(spec.courseTitle());
                ensureEnrollment(user, course, spec.progress(), spec.daysAgo());
                if (course.getPrice() > 0) {
                    ensurePurchase(user, course, payHere, spec.daysAgo());
                }
            }
            ensurePendingPayment(student, courses.get(POSTGRES), payHere);

            logger.info("Demo data ready: {} courses, {} users", courseRepository.count(), usersRepository.count());
        } catch (Exception e) {
            logger.error("Demo data seeding failed", e);
        }
    }

    // ---------- users ----------

    /** Creates a demo account, or realigns its role and password with the current configuration. */
    private Users upsertDemoUser(String email, String fname, String lname, Role role) {
        Users user = usersRepository.findByEmail(email).orElseGet(Users::new);
        boolean isNew = user.getId() == null;
        user.setEmail(email);
        user.setFname(fname);
        user.setLname(lname);
        user.setRole(role);
        if (isNew) {
            user.setCreated_at(new Date());
        }
        String hash = user.getPassword_hash();
        if (hash == null || !passwordEncoder.matches(demoProperties.getPassword(), hash)) {
            user.setPassword_hash(passwordEncoder.encode(demoProperties.getPassword()));
        }
        return usersRepository.save(user);
    }

    private Users ensureLockedUser(String email, String fname, String lname, Role role) {
        return usersRepository.findByEmail(email).orElseGet(() -> {
            Users user = new Users();
            user.setEmail(email);
            user.setFname(fname);
            user.setLname(lname);
            user.setRole(role);
            user.setPassword_hash(LOCKED_PASSWORD_HASH);
            user.setCreated_at(new Date());
            return usersRepository.save(user);
        });
    }

    // ---------- catalogue ----------

    private Course ensureCourse(CourseSpec spec, Users owner) {
        return courseRepository.findByTitle(spec.title()).orElseGet(() -> {
            Course course = new Course();
            course.setTitle(spec.title());
            course.setDescription(spec.description());
            course.setPrice(spec.price());
            course.setDifficulty(spec.difficulty());
            course.setPricelevel(spec.difficulty());
            course.setThumbnail("https://placehold.co/600x400/1e3a8a/ffffff?text="
                    + spec.title().replace(' ', '+'));
            course.setUser(owner);
            course.setCreated_at(new Date());
            return courseRepository.save(course);
        });
    }

    private void ensureLessons(Course course, List<String> titles) {
        if (!lessonRepository.findByCourse_IdOrderByOrderIndexAsc(course.getId()).isEmpty()) {
            return;
        }
        String slug = course.getTitle().toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]+", "-");
        for (int i = 0; i < titles.size(); i++) {
            Lesson lesson = new Lesson();
            lesson.setCourse(course);
            lesson.setTitle(titles.get(i));
            lesson.setOrderIndex(i + 1);
            lesson.setVideoUrl("https://example.com/videos/" + slug + "/" + (i + 1) + ".mp4");
            lessonRepository.save(lesson);
        }
    }

    private void ensureQuizzes(Map<String, Course> courses) {
        ensureQuiz(courses.get(SPRING), "Spring Boot REST checkpoint", new String[][]{
                {"Which annotation marks a class as a REST controller in Spring?",
                        "@Service", "@RestController", "@Repository", "@Configuration", "B"},
                {"Which HTTP status code is conventionally returned when a resource is created?",
                        "200", "201", "204", "404", "B"},
                {"What does Spring Data JPA derive from a method named findByEmail?",
                        "A query filtering by the email property", "A database migration",
                        "A JSON schema", "A security rule", "A"}
        });
        ensureQuiz(courses.get(REACT), "React and TypeScript checkpoint", new String[][]{
                {"Which hook stores local component state?",
                        "useEffect", "useState", "useMemo", "useRef", "B"},
                {"What does TypeScript add to JavaScript?",
                        "Static types", "A virtual DOM", "A new browser engine", "Server rendering", "A"},
                {"How is data passed from a parent component to a child?",
                        "Props", "Cookies", "Environment variables", "CSS classes", "A"}
        });
        ensureQuiz(courses.get(SECURITY), "API security checkpoint", new String[][]{
                {"Where should a JWT be sent on API requests?",
                        "In the Authorization header as a Bearer token", "In the URL query string",
                        "In an HTML comment", "Only in the response body", "A"},
                {"Which algorithm is appropriate for storing passwords?",
                        "MD5", "BCrypt", "Base64", "CRC32", "B"},
                {"What should an API return when an authenticated user lacks permission?",
                        "403 Forbidden", "200 with an empty body", "500 Internal Server Error",
                        "A redirect to the home page", "A"}
        });
    }

    private void ensureQuiz(Course course, String title, String[][] questions) {
        boolean exists = quizRepository.findAll().stream()
                .anyMatch(q -> title.equals(q.getTitle())
                        && q.getCourse() != null && course.getId().equals(q.getCourse().getId()));
        if (exists) {
            return;
        }
        Quiz quiz = new Quiz();
        quiz.setCourse(course);
        quiz.setTitle(title);
        for (String[] q : questions) {
            QuizQuestion question = new QuizQuestion();
            question.setQuiz(quiz);
            question.setQuestionText(q[0]);
            question.setOptionA(q[1]);
            question.setOptionB(q[2]);
            question.setOptionC(q[3]);
            question.setOptionD(q[4]);
            question.setCorrectOption(q[5]);
            quiz.getQuestions().add(question);
        }
        quizRepository.save(quiz);
    }

    // ---------- activity ----------

    private void ensureEnrollment(Users user, Course course, double progress, int daysAgo) {
        if (enrollmentRepository.existsByUser_IdAndCourse_Id(user.getId(), course.getId())) {
            return;
        }
        Enrollment enrollment = new Enrollment();
        enrollment.setUser(user);
        enrollment.setCourse(course);
        enrollment.setProgress(progress);
        enrollment.setEnrolled_at(daysAgo(daysAgo));
        enrollmentRepository.save(enrollment);
    }

    /** Records a completed order and a successful payment for one purchase. */
    private void ensurePurchase(Users user, Course course, PaymentMethod method, int daysAgo) {
        String txn = "SV-DEMO-" + user.getId() + "-" + course.getId();
        if (paymentRepository.findByTxnReference(txn).isPresent()) {
            return;
        }
        Date when = daysAgo(daysAgo);

        Orders order = new Orders();
        order.setUser(user);
        order.setStatus("COMPLETED");
        order.setTotal(course.getPrice());
        order.setCreated_at(when);
        OrderItem item = new OrderItem();
        item.setCourse(course);
        item.setPrice(course.getPrice());
        order.addOrderItem(item);
        ordersRepository.save(order);

        Payment payment = new Payment();
        payment.setUser(user);
        payment.setCourse(course);
        payment.setAmount(course.getPrice());
        payment.setPaymentMethod(method);
        payment.setStatus("SUCCESS");
        payment.setTxnReference(txn);
        payment.setCreated_at(when);
        payment.setPaid_at(when);
        paymentRepository.save(payment);
    }

    private void ensurePendingPayment(Users user, Course course, PaymentMethod method) {
        String txn = "SV-DEMO-" + user.getId() + "-" + course.getId() + "-PENDING";
        if (paymentRepository.findByTxnReference(txn).isPresent()
                || enrollmentRepository.existsByUser_IdAndCourse_Id(user.getId(), course.getId())) {
            return;
        }
        Payment payment = new Payment();
        payment.setUser(user);
        payment.setCourse(course);
        payment.setAmount(course.getPrice());
        payment.setPaymentMethod(method);
        payment.setStatus("PENDING");
        payment.setTxnReference(txn);
        payment.setCreated_at(daysAgo(1));
        paymentRepository.save(payment);
    }

    private PaymentMethod ensurePaymentMethod(String name) {
        PaymentMethod existing = paymentMethodRepository.findByMethod(name);
        if (existing != null) {
            return existing;
        }
        PaymentMethod method = new PaymentMethod();
        method.setMethod(name);
        return paymentMethodRepository.save(method);
    }

    private static Date daysAgo(int days) {
        return new Date(System.currentTimeMillis() - days * 86_400_000L);
    }
}
