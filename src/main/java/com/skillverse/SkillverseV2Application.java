package com.skillverse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Spring Boot application entry point for Skillverse V2.
 * Initializes and starts the Skillverse learning management system.
 */
@SpringBootApplication(scanBasePackages = "com.skillverse")
public class SkillverseV2Application {

    public static void main(String[] args) {
        SpringApplication.run(SkillverseV2Application.class, args);
    }

}
