package com.skillverse;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Spring Boot application entry point for Skillverse V2.
 * Initializes and starts the Skillverse learning management system.
 */
@SpringBootApplication(scanBasePackages = "com.skillverse")
public class SkillverseV2Application {

    public static void main(String[] args) {
        // Load .env file BEFORE Spring starts
        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMissing()
                .load();

        dotenv.entries().forEach(entry -> {
            if (System.getenv(entry.getKey()) == null) {
                System.setProperty(entry.getKey(), entry.getValue());
            }
        });

        SpringApplication.run(SkillverseV2Application.class, args);
    }

}
