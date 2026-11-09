package com.skillverse;

import com.jayway.jsonpath.JsonPath;
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

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * End-to-end checks of the authorization, pricing and payment-notification rules,
 * run against the in-memory H2 database with the seeded test accounts.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(properties = {
        "app.seed.password=Test-Seed-Pass-1",
        "payhere.merchant-id=1234567",
        "payhere.merchant-secret=test-merchant-secret"
})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SecurityHardeningIntegrationTest {

    private static final String SEED_PASSWORD = "Test-Seed-Pass-1";
    private static final String MERCHANT_ID = "1234567";
    private static final String MERCHANT_SECRET = "test-merchant-secret";

    @Autowired
    private MockMvc mvc;

    private String adminToken;
    private String instructorToken;
    private String studentToken;
    private String otherStudentToken;
    private int studentId;
    private int otherStudentId;

    @BeforeAll
    void setUp() throws Exception {
        adminToken = login("admin@example.com");
        instructorToken = login("instructor@example.com");
        studentToken = login("student@example.com");
        otherStudentToken = login("alice.student@example.com");
        studentId = currentUserId(studentToken);
        otherStudentId = currentUserId(otherStudentToken);
    }

    // ---------- access control ----------

    @Test
    void anonymousRequestsToProtectedEndpointsAreRejected() throws Exception {
        mvc.perform(get("/api/v1/orders")).andExpect(status().is4xxClientError());
        mvc.perform(get("/api/v1/payments")).andExpect(status().is4xxClientError());
    }

    @Test
    void listingAllPaymentsAndOrdersIsAdminOnly() throws Exception {
        mvc.perform(get("/api/v1/payments").header("Authorization", bearer(studentToken)))
                .andExpect(status().isForbidden());
        mvc.perform(get("/api/v1/orders").header("Authorization", bearer(studentToken)))
                .andExpect(status().isForbidden());
        mvc.perform(get("/api/v1/payments").header("Authorization", bearer(adminToken)))
                .andExpect(status().isOk());
        mvc.perform(get("/api/v1/orders").header("Authorization", bearer(adminToken)))
                .andExpect(status().isOk());
    }

    @Test
    void usersCannotReadEachOthersData() throws Exception {
        String auth = bearer(studentToken);
        mvc.perform(get("/api/v1/users/" + otherStudentId).header("Authorization", auth))
                .andExpect(status().isForbidden());
        mvc.perform(get("/api/v1/enrollments/user/" + otherStudentId).header("Authorization", auth))
                .andExpect(status().isForbidden());
        mvc.perform(get("/api/v1/payments/user/" + otherStudentId).header("Authorization", auth))
                .andExpect(status().isForbidden());
        mvc.perform(get("/api/v1/orders/user/" + otherStudentId).header("Authorization", auth))
                .andExpect(status().isForbidden());

        mvc.perform(get("/api/v1/users/" + studentId).header("Authorization", auth))
                .andExpect(status().isOk());
        mvc.perform(get("/api/v1/enrollments/user/" + studentId).header("Authorization", auth))
                .andExpect(status().isOk());
        mvc.perform(get("/api/v1/users/" + otherStudentId).header("Authorization", bearer(adminToken)))
                .andExpect(status().isOk());
    }

    @Test
    void onlyInstructorsAndAdminsCanCreateCoursesOrUploadFiles() throws Exception {
        mvc.perform(post("/api/v1/courses")
                        .header("Authorization", bearer(studentToken))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(courseJson("Student Made Course", 10.0)))
                .andExpect(status().isForbidden());

        MockMultipartFile file = new MockMultipartFile("file", "a.png", "image/png", new byte[]{1, 2, 3});
        mvc.perform(multipart("/api/v1/upload").file(file).header("Authorization", bearer(studentToken)))
                .andExpect(status().isForbidden());
    }

    @Test
    void studentCannotSelfEnrolOthersOrCreateUsers() throws Exception {
        int courseId = createCourse("Admin Only Enrol Course", 20.0);
        mvc.perform(post("/api/v1/enrollments/enroll/" + otherStudentId + "/" + courseId)
                        .header("Authorization", bearer(studentToken)))
                .andExpect(status().isForbidden());
        mvc.perform(post("/api/v1/users")
                        .header("Authorization", bearer(studentToken))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"fname\":\"New\",\"lname\":\"User\",\"email\":\"new.user@example.com\"}"))
                .andExpect(status().isForbidden());
    }

    @Test
    void lessonsRequireEnrollmentAndNeverExposePasswordHashes() throws Exception {
        int courseId = createCourse("Lesson Access Course", 30.0);
        String lessonBody = "{\"courseId\":" + courseId
                + ",\"title\":\"Intro lesson\",\"video_url\":\"https://example.com/video.mp4\",\"order_index\":1}";

        mvc.perform(post("/api/v1/courses/" + courseId + "/lessons")
                        .header("Authorization", bearer(studentToken))
                        .contentType(MediaType.APPLICATION_JSON).content(lessonBody))
                .andExpect(status().isForbidden());
        mvc.perform(post("/api/v1/courses/" + courseId + "/lessons")
                        .header("Authorization", bearer(instructorToken))
                        .contentType(MediaType.APPLICATION_JSON).content(lessonBody))
                .andExpect(status().isCreated());

        mvc.perform(get("/api/v1/courses/" + courseId + "/lessons").header("Authorization", bearer(studentToken)))
                .andExpect(status().isForbidden());

        mvc.perform(post("/api/v1/enroll/" + courseId).header("Authorization", bearer(studentToken)))
                .andExpect(status().isCreated());

        MvcResult asStudent = mvc.perform(get("/api/v1/courses/" + courseId + "/lessons")
                        .header("Authorization", bearer(studentToken)))
                .andExpect(status().isOk()).andReturn();
        MvcResult asInstructor = mvc.perform(get("/api/v1/courses/" + courseId + "/lessons")
                        .header("Authorization", bearer(instructorToken)))
                .andExpect(status().isOk()).andReturn();

        for (MvcResult result : new MvcResult[]{asStudent, asInstructor}) {
            String body = result.getResponse().getContentAsString();
            assertTrue(body.contains("Intro lesson"));
            assertFalse(body.contains("password_hash"), "password hash must never be serialized");
            assertFalse(body.contains("$2a$"), "BCrypt hash must never be serialized");
        }
    }

    // ---------- pricing ----------

    @Test
    void orderItemPriceAlwaysComesFromTheCourse() throws Exception {
        int courseId = createCourse("Priced Order Course", 50.0);

        MvcResult created = mvc.perform(post("/api/v1/orders").header("Authorization", bearer(studentToken)))
                .andExpect(status().isOk()).andReturn();
        int orderId = JsonPath.read(created.getResponse().getContentAsString(), "$.orderId");

        MvcResult item = mvc.perform(post("/api/v1/orders/" + orderId + "/items")
                        .param("courseId", String.valueOf(courseId))
                        .param("price", "1.00")
                        .header("Authorization", bearer(studentToken)))
                .andExpect(status().isOk()).andReturn();
        double price = JsonPath.read(item.getResponse().getContentAsString(), "$.price");
        assertEquals(50.0, price, 0.001);

        mvc.perform(get("/api/v1/orders/" + orderId).header("Authorization", bearer(otherStudentToken)))
                .andExpect(status().isForbidden());
        mvc.perform(post("/api/v1/orders/" + orderId + "/items")
                        .param("courseId", String.valueOf(courseId))
                        .header("Authorization", bearer(otherStudentToken)))
                .andExpect(status().isForbidden());
        mvc.perform(get("/api/v1/orders/" + orderId).header("Authorization", bearer(adminToken)))
                .andExpect(status().isOk());
    }

    @Test
    void paymentAmountAndUserComeFromTheServer() throws Exception {
        int courseId = createCourse("Priced Payment Course", 50.0);

        MvcResult initiated = mvc.perform(post("/api/v1/payments/initiate")
                        .header("Authorization", bearer(studentToken))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"courseId\":" + courseId + ",\"amount\":1,\"userId\":" + otherStudentId + "}"))
                .andExpect(status().isOk()).andReturn();
        String body = initiated.getResponse().getContentAsString();
        double amount = JsonPath.read(body, "$.amount");
        int paymentId = JsonPath.read(body, "$.paymentId");
        String txn = JsonPath.read(body, "$.txnReference");

        assertEquals(50.0, amount, 0.001);
        assertTrue(txn.startsWith("SV-"));

        // the payment belongs to the caller, not to the userId in the body
        mvc.perform(get("/api/v1/payments/" + paymentId).header("Authorization", bearer(studentToken)))
                .andExpect(status().isOk());
        mvc.perform(get("/api/v1/payments/" + paymentId).header("Authorization", bearer(otherStudentToken)))
                .andExpect(status().isForbidden());
    }

    // ---------- PayHere notifications ----------

    @Test
    void webhookRejectsInvalidSignatureAndWrongAmountWithoutChangingThePayment() throws Exception {
        int courseId = createCourse("Webhook Rejection Course", 50.0);
        String txn = initiatePayment(studentToken, courseId);

        mvc.perform(post("/api/v1/payments/webhook/payhere")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("merchant_id", MERCHANT_ID).param("order_id", txn)
                        .param("payhere_amount", "50.00").param("payhere_currency", "LKR")
                        .param("status_code", "2").param("md5sig", "0000INVALID"))
                .andExpect(status().isBadRequest());

        mvc.perform(post("/api/v1/payments/webhook/payhere")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("merchant_id", MERCHANT_ID).param("order_id", txn)
                        .param("payhere_amount", "1.00").param("payhere_currency", "LKR")
                        .param("status_code", "2")
                        .param("md5sig", signature(txn, "1.00", "LKR", "2")))
                .andExpect(status().isBadRequest());

        assertEquals("PENDING", paymentStatus(txn));
        assertFalse(isEnrolled(studentToken, courseId));
    }

    @Test
    void validWebhookIsPublicEnrolsOnceAndCannotBeDowngraded() throws Exception {
        int courseId = createCourse("Webhook Success Course", 50.0);
        String txn = initiatePayment(studentToken, courseId);

        // no Authorization header: PayHere calls this endpoint server-to-server
        mvc.perform(post("/api/v1/payments/webhook/payhere")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("merchant_id", MERCHANT_ID).param("order_id", txn)
                        .param("payhere_amount", "50.00").param("payhere_currency", "LKR")
                        .param("status_code", "2")
                        .param("md5sig", signature(txn, "50.00", "LKR", "2")))
                .andExpect(status().isOk());

        assertEquals("SUCCESS", paymentStatus(txn));
        assertTrue(isEnrolled(studentToken, courseId));

        // a later failure notification for the same payment must not undo the success
        mvc.perform(post("/api/v1/payments/webhook/payhere")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("merchant_id", MERCHANT_ID).param("order_id", txn)
                        .param("payhere_amount", "50.00").param("payhere_currency", "LKR")
                        .param("status_code", "-2")
                        .param("md5sig", signature(txn, "50.00", "LKR", "-2")))
                .andExpect(status().isOk());
        assertEquals("SUCCESS", paymentStatus(txn));
    }

    // ---------- helpers ----------

    private String login(String email) throws Exception {
        MvcResult result = mvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"" + email + "\",\"password\":\"" + SEED_PASSWORD + "\"}"))
                .andExpect(status().isOk()).andReturn();
        return JsonPath.read(result.getResponse().getContentAsString(), "$.token");
    }

    private int currentUserId(String token) throws Exception {
        MvcResult result = mvc.perform(get("/api/v1/auth/me").header("Authorization", bearer(token)))
                .andExpect(status().isOk()).andReturn();
        return JsonPath.read(result.getResponse().getContentAsString(), "$.id");
    }

    private int createCourse(String title, double price) throws Exception {
        MvcResult result = mvc.perform(post("/api/v1/courses")
                        .header("Authorization", bearer(instructorToken))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(courseJson(title, price)))
                .andExpect(status().isCreated()).andReturn();
        return JsonPath.read(result.getResponse().getContentAsString(), "$.id");
    }

    private String initiatePayment(String token, int courseId) throws Exception {
        MvcResult result = mvc.perform(post("/api/v1/payments/initiate")
                        .header("Authorization", bearer(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"courseId\":" + courseId + ",\"paymentMethod\":\"PayHere\"}"))
                .andExpect(status().isOk()).andReturn();
        return JsonPath.read(result.getResponse().getContentAsString(), "$.txnReference");
    }

    private String paymentStatus(String txn) throws Exception {
        MvcResult result = mvc.perform(get("/api/v1/payments/txn/" + txn)
                        .header("Authorization", bearer(adminToken)))
                .andExpect(status().isOk()).andReturn();
        return JsonPath.read(result.getResponse().getContentAsString(), "$.status");
    }

    private boolean isEnrolled(String token, int courseId) throws Exception {
        MvcResult result = mvc.perform(get("/api/v1/users/me/enrollments")
                        .header("Authorization", bearer(token)))
                .andExpect(status().isOk()).andReturn();
        java.util.List<Integer> courseIds =
                JsonPath.read(result.getResponse().getContentAsString(), "$[*].courseId");
        return courseIds.contains(courseId);
    }

    private static String courseJson(String title, double price) {
        return "{\"title\":\"" + title + "\",\"description\":\"A course description that is long enough\","
                + "\"pricelevel\":\"BEGINNER\",\"difficulty\":\"BEGINNER\",\"price\":" + price + "}";
    }

    private static String bearer(String token) {
        return "Bearer " + token;
    }

    private static String signature(String orderId, String amount, String currency, String statusCode) {
        return md5Upper(MERCHANT_ID + orderId + amount + currency + statusCode + md5Upper(MERCHANT_SECRET));
    }

    private static String md5Upper(String value) {
        try {
            byte[] hash = MessageDigest.getInstance("MD5").digest(value.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().withUpperCase().formatHex(hash);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
