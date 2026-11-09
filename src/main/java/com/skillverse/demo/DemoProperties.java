package com.skillverse.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Settings for demo mode, a deliberately public sandbox showcase.
 *
 * When {@code app.demo.enabled} is true the application seeds demo data and publishes the
 * demo credentials through {@code GET /api/v1/auth/demo-accounts}. The shared password is
 * public by design, so demo accounts are limited: the demo administrator is read-only and
 * demo accounts cannot upload files.
 */
@Component
public class DemoProperties {

    public static final String ADMIN_EMAIL = "demo.admin@example.com";
    public static final String INSTRUCTOR_EMAIL = "demo.instructor@example.com";
    public static final String STUDENT_EMAIL = "demo.student@example.com";

    /** One entry in the public demo-accounts list. */
    public record DemoAccount(String role, String email, String label, String description) {
    }

    private static final List<DemoAccount> ACCOUNTS = List.of(
            new DemoAccount("STUDENT", STUDENT_EMAIL, "Student",
                    "Browse courses, enrol, and open My Courses with progress already recorded."),
            new DemoAccount("INSTRUCTOR", INSTRUCTOR_EMAIL, "Instructor",
                    "Owns four courses with lessons and quizzes; can create courses and lessons."),
            new DemoAccount("ADMIN", ADMIN_EMAIL, "Administrator",
                    "Read-only view of users, payments, orders and platform statistics.")
    );

    private final boolean enabled;
    private final String password;

    public DemoProperties(
            @Value("${app.demo.enabled:false}") boolean enabled,
            @Value("${app.demo.password:}") String password
    ) {
        this.enabled = enabled;
        this.password = password == null ? "" : password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getPassword() {
        return password;
    }

    public List<DemoAccount> accounts() {
        return ACCOUNTS;
    }

    /** True when demo mode is on and the email belongs to one of the demo accounts. */
    public boolean isDemoAccount(String email) {
        return enabled && email != null
                && ACCOUNTS.stream().anyMatch(a -> a.email().equalsIgnoreCase(email));
    }

    /** True when demo mode is on and the email is the demo administrator. */
    public boolean isDemoAdmin(String email) {
        return enabled && ADMIN_EMAIL.equalsIgnoreCase(email);
    }
}
