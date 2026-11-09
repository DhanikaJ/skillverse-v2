package com.skillverse.demo;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DemoControllerTest {

    @Test
    void disabledDemoModePublishesNoCredentials() {
        DemoController controller = new DemoController(new DemoProperties(false, "secret-value"));

        ResponseEntity<Map<String, Object>> response = controller.getDemoAccounts();

        assertEquals(false, response.getBody().get("enabled"));
        assertEquals(List.of(), response.getBody().get("accounts"));
        assertFalse(response.getBody().toString().contains("secret-value"));
    }

    @Test
    void demoAccountChecksAreOffWhenDemoModeIsDisabled() {
        DemoProperties off = new DemoProperties(false, "x");
        assertFalse(off.isDemoAccount(DemoProperties.STUDENT_EMAIL));
        assertFalse(off.isDemoAdmin(DemoProperties.ADMIN_EMAIL));

        DemoProperties on = new DemoProperties(true, "x");
        assertTrue(on.isDemoAccount(DemoProperties.STUDENT_EMAIL.toUpperCase()));
        assertTrue(on.isDemoAdmin(DemoProperties.ADMIN_EMAIL));
        assertFalse(on.isDemoAdmin(DemoProperties.STUDENT_EMAIL));
        assertFalse(on.isDemoAccount("someone.else@example.com"));
    }
}
