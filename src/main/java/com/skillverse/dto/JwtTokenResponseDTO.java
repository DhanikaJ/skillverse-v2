package com.skillverse.dto;

/**
 * DTO for JWT token response
 */
public class JwtTokenResponseDTO {

    private String token;
    private String type = "Bearer ";
    private String email;
    private String role;

    // Constructors
    public JwtTokenResponseDTO() {
    }

    public JwtTokenResponseDTO(String token, String email, String role) {
        this.token = token;
        this.email = email;
        this.role = role;
    }

    // Getters and Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}

