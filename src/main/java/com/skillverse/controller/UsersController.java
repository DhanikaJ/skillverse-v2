package com.skillverse.controller;

import com.skillverse.dto.UserDTO;
import com.skillverse.model.Users;
import com.skillverse.service.UsersService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/users")
public class UsersController {

    private final UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping
    public Page<UserDTO> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return usersService.getUsers(pageable);
    }

    @GetMapping({"{id}"})
    public UserDTO getUsersById(@PathVariable Integer id) {
        return usersService.getUsersById(id);
    }

    @PostMapping
    public void addNewUser(@Valid @RequestBody Users users){
        usersService.insertUser(users);
    }
}




