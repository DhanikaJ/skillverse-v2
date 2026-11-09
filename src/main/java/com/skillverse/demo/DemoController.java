package com.skillverse.demo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Publishes the demo credentials so the UI can offer one-click demo sign-in.
 * Returns an empty list unless demo mode is enabled.
 */
@RestController
@RequestMapping("/api/v1/auth/demo-accounts")
@Tag(name = "Demo", description = "Demo mode (sandbox showcase)")
public class DemoController {

    private final DemoProperties demoProperties;
    private final ObjectProvider<DemoDataSeeder> seeder;

    public DemoController(DemoProperties demoProperties, ObjectProvider<DemoDataSeeder> seeder) {
        this.demoProperties = demoProperties;
        this.seeder = seeder;
    }

    @GetMapping
    @Operation(summary = "List the public demo accounts (empty unless demo mode is enabled; 503 while seeding)")
    public ResponseEntity<Map<String, Object>> getDemoAccounts() {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("enabled", demoProperties.isEnabled());

        if (!demoProperties.isEnabled()) {
            body.put("accounts", List.of());
            return ResponseEntity.ok(body);
        }

        DemoDataSeeder dataSeeder = seeder.getIfAvailable();
        if (dataSeeder != null && !dataSeeder.isReady()) {
            // seeding has not finished yet: clients should retry shortly
            body.put("ready", false);
            body.put("accounts", List.of());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(body);
        }

        List<Map<String, String>> accounts = demoProperties.accounts().stream()
                .map(account -> {
                    Map<String, String> entry = new LinkedHashMap<>();
                    entry.put("role", account.role());
                    entry.put("label", account.label());
                    entry.put("description", account.description());
                    entry.put("email", account.email());
                    entry.put("password", demoProperties.getPassword());
                    return entry;
                })
                .toList();
        body.put("accounts", accounts);
        return ResponseEntity.ok(body);
    }
}
