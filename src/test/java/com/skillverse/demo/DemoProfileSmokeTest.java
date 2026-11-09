package com.skillverse.demo;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * The {@code demo} profile must boot with no environment variables at all (no database,
 * JWT secret or Cloudinary credentials) and serve working demo accounts, so it can run on a
 * free host with nothing configured but the UI origin.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("demo")
class DemoProfileSmokeTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void bootsStandaloneAndServesDemoAccounts() throws Exception {
        MvcResult accounts = mvc.perform(get("/api/v1/auth/demo-accounts"))
                .andExpect(status().isOk()).andReturn();
        String body = accounts.getResponse().getContentAsString();
        assertTrue((Boolean) JsonPath.read(body, "$.enabled"));
        String email = JsonPath.read(body, "$.accounts[0].email");
        String password = JsonPath.read(body, "$.accounts[0].password");

        MvcResult login = mvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"" + email + "\",\"password\":\"" + password + "\"}"))
                .andExpect(status().isOk()).andReturn();
        String token = JsonPath.read(login.getResponse().getContentAsString(), "$.token");

        MvcResult courses = mvc.perform(get("/api/v1/courses?size=50")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk()).andReturn();
        int total = JsonPath.read(courses.getResponse().getContentAsString(), "$.totalElements");
        assertTrue(total >= 8, "demo catalogue should be seeded, found " + total);
    }

    @Test
    void swaggerAndDocsAreNotExposed() throws Exception {
        mvc.perform(get("/v3/api-docs")).andExpect(status().isNotFound());
    }

    @Test
    void clientMistakesGetClientErrorsNotServerErrors() throws Exception {
        mvc.perform(get("/api/v1/does-not-exist")).andExpect(status().is4xxClientError());
        mvc.perform(get("/api/v1/auth/login")).andExpect(status().isMethodNotAllowed());
        mvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{not json"))
                .andExpect(status().isBadRequest());
    }
}
