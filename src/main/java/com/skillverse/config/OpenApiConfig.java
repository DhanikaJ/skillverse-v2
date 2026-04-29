package com.skillverse.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Skillverse API",
        version = "1.0.0",
        description = "Complete REST API documentation for Skillverse learning platform. " +
                     "Includes endpoints for user management, courses, lessons, quizzes, enrollments, payments, and file uploads."
    ),
    servers = {
        @Server(
            url = "http://localhost:8080",
            description = "Local Development Server"
        ),
        @Server(
            url = "https://api.skillverse.com",
            description = "Production Server"
        )
    }
)
public class OpenApiConfig {
}


