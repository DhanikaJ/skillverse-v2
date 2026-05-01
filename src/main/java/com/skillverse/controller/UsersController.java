package com.skillverse.controller;

import com.skillverse.dto.UserDTO;
import com.skillverse.dto.UserRequestDTO;
import com.skillverse.model.Users;
import com.skillverse.service.UsersService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for managing users.
 * Handles CRUD operations and query endpoints for users.
 */
@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Users", description = "User management endpoints")
public class UsersController {

    private final UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    /**
     * Retrieves all users with pagination.
     *
     * @param page the page number (default: 0)
     * @param size the page size (default: 10)
     * @return ResponseEntity with paginated user data (200 OK)
     */
    @GetMapping
    @Operation(summary = "Get all users with pagination")
    @ApiResponse(responseCode = "200", description = "Users retrieved successfully")
    public ResponseEntity<Page<UserDTO>> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(usersService.getUsersWithPagination(pageable));
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id the user ID
     * @return ResponseEntity with user data (200 OK) or (404 Not Found)
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID")
    @ApiResponse(responseCode = "200", description = "User found")
    @ApiResponse(responseCode = "404", description = "User not found")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Integer id) {
        return ResponseEntity.ok(usersService.getUserById(id));
    }

    /**
     * Creates a new user.
     *
     * @param userRequestDTO the user data to create (validated)
     * @return ResponseEntity with created user data (201 Created)
     */
    @PostMapping
    @Operation(summary = "Create a new user")
    @ApiResponse(responseCode = "201", description = "User created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid user data")
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        Users user = new Users();
        user.setFname(userRequestDTO.getFname());
        user.setLname(userRequestDTO.getLname());
        user.setEmail(userRequestDTO.getEmail());
        usersService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(usersService.getUserById(user.getId()));
    }
}
