package com.skillverse.demo;

import com.jayway.jsonpath.JsonPath;
import com.skillverse.repository.CourseRepository;
import com.skillverse.repository.EnrollmentRepository;
import com.skillverse.repository.PaymentRepository;
import com.skillverse.repository.UsersRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Demo mode: published credentials, seeded showcase data and the limits placed on public accounts.
 * Uses its own in-memory database so it never interferes with the other integration tests.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:demotestdb;MODE=PostgreSQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
        "app.demo.enabled=true",
        "app.demo.password=Demo-Test-Pass-9",
        "app.seed.password=Test-Seed-Pass-1"
})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DemoModeIntegrationTest {

    private static final String SPRING_COURSE = "Spring Boot REST APIs in Practice";
    private static final String DOCKER_COURSE = "Docker and Deployment Essentials";

    @Autowired
    private MockMvc mvc;
    @Autowired
    private DemoDataSeeder seeder;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private EnrollmentRepository enrollmentRepository;
    @Autowired
    private PaymentRepository paymentRepository;

    private String studentToken;
    private String instructorToken;
    private String adminToken;

    @BeforeAll
    void setUp() throws Exception {
        studentToken = login(DemoProperties.STUDENT_EMAIL);
        instructorToken = login(DemoProperties.INSTRUCTOR_EMAIL);
        adminToken = login(DemoProperties.ADMIN_EMAIL);
    }

    @Test
    void publishesTheThreeDemoAccountsWithTheirSharedPassword() throws Exception {
        MvcResult result = mvc.perform(get("/api/v1/auth/demo-accounts"))
                .andExpect(status().isOk()).andReturn();
        String body = result.getResponse().getContentAsString();

        assertEquals(true, JsonPath.read(body, "$.enabled"));
        List<String> roles = JsonPath.read(body, "$.accounts[*].role");
        List<String> emails = JsonPath.read(body, "$.accounts[*].email");
        List<String> passwords = JsonPath.read(body, "$.accounts[*].password");
        assertEquals(List.of("STUDENT", "INSTRUCTOR", "ADMIN"), roles);
        assertEquals(List.of(DemoProperties.STUDENT_EMAIL, DemoProperties.INSTRUCTOR_EMAIL,
                DemoProperties.ADMIN_EMAIL), emails);
        assertTrue(passwords.stream().allMatch("Demo-Test-Pass-9"::equals));
    }

    @Test
    void seededShowcaseDataIsRichAndStudentHasProgress() throws Exception {
        assertTrue(courseRepository.findByTitle(SPRING_COURSE).isPresent());

        MvcResult courses = mvc.perform(get("/api/v1/courses?size=50")
                        .header("Authorization", bearer(studentToken)))
                .andExpect(status().isOk()).andReturn();
        int total = JsonPath.read(courses.getResponse().getContentAsString(), "$.totalElements");
        assertTrue(total >= 8, "expected at least eight demo courses, found " + total);

        MvcResult enrollments = mvc.perform(get("/api/v1/users/me/enrollments")
                        .header("Authorization", bearer(studentToken)))
                .andExpect(status().isOk()).andReturn();
        String body = enrollments.getResponse().getContentAsString();
        List<String> titles = JsonPath.read(body, "$[*].courseTitle");
        assertTrue(titles.contains(SPRING_COURSE));
        assertFalse(titles.contains(DOCKER_COURSE), "some courses must stay open to try enrolling");

        // an enrolled learner can open lessons; a non-enrolled one cannot
        int springId = courseRepository.findByTitle(SPRING_COURSE).orElseThrow().getId();
        int dockerId = courseRepository.findByTitle(DOCKER_COURSE).orElseThrow().getId();
        MvcResult lessons = mvc.perform(get("/api/v1/courses/" + springId + "/lessons")
                        .header("Authorization", bearer(studentToken)))
                .andExpect(status().isOk()).andReturn();
        List<Object> lessonList = JsonPath.read(lessons.getResponse().getContentAsString(), "$");
        assertEquals(4, lessonList.size());
        mvc.perform(get("/api/v1/courses/" + dockerId + "/lessons")
                        .header("Authorization", bearer(studentToken)))
                .andExpect(status().isForbidden());

        // and the demo student can enrol in an open course
        mvc.perform(post("/api/v1/enroll/" + dockerId).header("Authorization", bearer(studentToken)))
                .andExpect(status().isCreated());
    }

    @Test
    void instructorOwnsCoursesWithQuizzesAndSupportingAccountsCannotLogIn() throws Exception {
        int springId = courseRepository.findByTitle(SPRING_COURSE).orElseThrow().getId();
        mvc.perform(get("/api/v1/enrollments/course/" + springId)
                        .header("Authorization", bearer(instructorToken)))
                .andExpect(status().isOk());

        mvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"nimal.perera@example.com\",\"password\":\"Demo-Test-Pass-9\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void demoAdminSeesEverythingButCannotChangeAnything() throws Exception {
        MvcResult stats = mvc.perform(get("/api/v1/admin/courses/stats")
                        .header("Authorization", bearer(adminToken)))
                .andExpect(status().isOk()).andReturn();
        int enrollments = JsonPath.read(stats.getResponse().getContentAsString(), "$.totalEnrollments");
        assertTrue(enrollments > 0);

        MvcResult payments = mvc.perform(get("/api/v1/payments").header("Authorization", bearer(adminToken)))
                .andExpect(status().isOk()).andReturn();
        List<Object> paymentList = JsonPath.read(payments.getResponse().getContentAsString(), "$");
        assertFalse(paymentList.isEmpty());
        mvc.perform(get("/api/v1/orders").header("Authorization", bearer(adminToken)))
                .andExpect(status().isOk());

        MvcResult denied = mvc.perform(post("/api/v1/users")
                        .header("Authorization", bearer(adminToken))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"fname\":\"New\",\"lname\":\"User\",\"email\":\"new.user@example.com\"}"))
                .andExpect(status().isForbidden()).andReturn();
        assertTrue(denied.getResponse().getContentAsString().contains("read-only"));
        mvc.perform(put("/api/v1/orders/1").param("status", "CANCELLED")
                        .header("Authorization", bearer(adminToken)))
                .andExpect(status().isForbidden());
    }

    @Test
    void demoAccountsCannotUploadFilesButInstructorCanStillCreateCourses() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "a.png", "image/png", new byte[]{1, 2, 3});
        mvc.perform(multipart("/api/v1/upload").file(file).header("Authorization", bearer(instructorToken)))
                .andExpect(status().isForbidden());
        mvc.perform(multipart("/api/v1/upload").file(file).header("Authorization", bearer(adminToken)))
                .andExpect(status().isForbidden());

        mvc.perform(post("/api/v1/courses")
                        .header("Authorization", bearer(instructorToken))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Recruiter Created Course\","
                                + "\"description\":\"Created from the demo instructor account\","
                                + "\"pricelevel\":\"BEGINNER\",\"difficulty\":\"BEGINNER\",\"price\":10.0}"))
                .andExpect(status().isCreated());
    }

    @Test
    void seedingIsIdempotent() throws Exception {
        long courses = courseRepository.count();
        long users = usersRepository.count();
        long enrollments = enrollmentRepository.count();
        long payments = paymentRepository.count();

        seeder.run();
        seeder.run();

        assertEquals(courses, courseRepository.count());
        assertEquals(users, usersRepository.count());
        assertEquals(enrollments, enrollmentRepository.count());
        assertEquals(payments, paymentRepository.count());
    }

    private String login(String email) throws Exception {
        MvcResult result = mvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"" + email + "\",\"password\":\"Demo-Test-Pass-9\"}"))
                .andExpect(status().isOk()).andReturn();
        return JsonPath.read(result.getResponse().getContentAsString(), "$.token");
    }

    private static String bearer(String token) {
        return "Bearer " + token;
    }
}
