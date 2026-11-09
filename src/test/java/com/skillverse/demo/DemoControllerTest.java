package com.skillverse.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DemoControllerTest {

    @SuppressWarnings("unchecked")
    private static ObjectProvider<DemoDataSeeder> providerOf(DemoDataSeeder seeder) {
        ObjectProvider<DemoDataSeeder> provider = mock(ObjectProvider.class);
        when(provider.getIfAvailable()).thenReturn(seeder);
        return provider;
    }

    @Test
    void disabledDemoModePublishesNoCredentials() {
        DemoController controller = new DemoController(new DemoProperties(false, "secret-value"), providerOf(null));

        ResponseEntity<Map<String, Object>> response = controller.getDemoAccounts();

        assertEquals(false, response.getBody().get("enabled"));
        assertEquals(List.of(), response.getBody().get("accounts"));
        assertFalse(response.getBody().toString().contains("secret-value"));
    }

    @Test
    void reportsServiceUnavailableUntilSeedingHasFinished() {
        DemoDataSeeder seeder = mock(DemoDataSeeder.class);
        DemoController controller = new DemoController(new DemoProperties(true, "pw"), providerOf(seeder));

        when(seeder.isReady()).thenReturn(false);
        ResponseEntity<Map<String, Object>> notReady = controller.getDemoAccounts();
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, notReady.getStatusCode());
        assertEquals(List.of(), notReady.getBody().get("accounts"));

        when(seeder.isReady()).thenReturn(true);
        ResponseEntity<Map<String, Object>> ready = controller.getDemoAccounts();
        assertEquals(HttpStatus.OK, ready.getStatusCode());
        assertEquals(3, ((List<?>) ready.getBody().get("accounts")).size());
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
