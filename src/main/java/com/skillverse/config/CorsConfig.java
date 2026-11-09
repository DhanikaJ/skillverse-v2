package com.skillverse.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * CORS policy for the API. Allowed origins come from {@code cors.allowed-origins}
 * (environment variable {@code CORS_ALLOWED_ORIGINS}, comma-separated), so each environment
 * lists exactly the UI origins it serves.
 */
@Configuration
public class CorsConfig {

  private final String[] allowedOrigins;

  public CorsConfig(@Value("${cors.allowed-origins}") String[] allowedOrigins) {
    this.allowedOrigins = allowedOrigins;
  }

  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/v1/**")
          .allowedOrigins(allowedOrigins)
          .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
          .allowedHeaders("*")
          .exposedHeaders("*")
          .allowCredentials(false)
          .maxAge(3600);
      }
    };
  }
}
