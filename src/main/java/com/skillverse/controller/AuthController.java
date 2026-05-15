package com.skillverse.controller;

import com.skillverse.dto.AuthRequestDTO;
import com.skillverse.model.Role;
import com.skillverse.model.Users;
import com.skillverse.repository.UsersRepository;
import com.skillverse.security.JwtUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Authentication Controller
 * Handles user registration and login
 */
@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "Auth", description = "Authentication endpoints")
public class AuthController {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Register a new user
     * POST /api/v1/auth/register
     * Body: { "email": "user@example.com", "password": "password123" }
     */
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@Valid @RequestBody AuthRequestDTO authRequest) {
        try {
            // Check if user already exists
            if (usersRepository.findByEmail(authRequest.getEmail()).isPresent()) {
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", "User with this email already exists"
                ));
            }

            // Create new user
            Users user = new Users();
            user.setEmail(authRequest.getEmail());
            user.setPassword_hash(passwordEncoder.encode(authRequest.getPassword()));
            user.setRole(Role.STUDENT); // Default role
            user.setFname("User");
            user.setLname("Registered");

            // Save user
            Users savedUser = usersRepository.save(user);

            // Generate JWT token
            String token = jwtUtil.generateToken(savedUser.getEmail());

            // Return response
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "User registered successfully");
            response.put("token", token);
            response.put("type", "Bearer");
            response.put("email", savedUser.getEmail());
            response.put("role", savedUser.getRole().name());

            return ResponseEntity.status(201).body(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Error registering user: " + e.getMessage()
            ));
        }
    }

    /**
     * Login user
     * POST /api/v1/auth/login
     * Body: { "email": "user@example.com", "password": "password123" }
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody AuthRequestDTO authRequest) {
        try {
            // Find user by email
            Users user = usersRepository.findByEmail(authRequest.getEmail())
                    .orElseThrow(() -> new Exception("User not found"));

            // Validate password
            if (!passwordEncoder.matches(authRequest.getPassword(), user.getPassword_hash())) {
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", "Invalid password"
                ));
            }

            // Generate JWT token
            String token = jwtUtil.generateToken(user.getEmail());

            // Return response
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Login successful");
            response.put("token", token);
            response.put("type", "Bearer");
            response.put("email", user.getEmail());
            response.put("role", user.getRole().name());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Error logging in: " + e.getMessage()
            ));
        }
    }

    /**
     * Get current authenticated user info
     * GET /api/v1/auth/me
     * Requires JWT token in Authorization header
     */
    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> getCurrentUser() {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            Users user = usersRepository.findByEmail(email)
                    .orElseThrow(() -> new Exception("User not found"));

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("id", user.getId());
            response.put("email", user.getEmail());
            response.put("fname", user.getFname());
            response.put("lname", user.getLname());
            response.put("role", user.getRole().name());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of(
                    "success", false,
                    "message", "Unauthorized: " + e.getMessage()
            ));
        }
    }
}
