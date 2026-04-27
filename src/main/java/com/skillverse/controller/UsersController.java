package com.skillverse.controller;

import com.skillverse.dto.UserDTO;
import com.skillverse.dto.UserRequestDTO;
import com.skillverse.model.Users;
import com.skillverse.service.UsersService;
import jakarta.validation.Valid;
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
    public List<UserDTO> getUsers() {
        return usersService.getUsers();
    }

    @GetMapping({"{id}"})
    public UserDTO getUsersById(@PathVariable Integer id) {
        return usersService.getUsersById(id);
    }

    @PostMapping
    public void addNewUser(@Valid @RequestBody UserRequestDTO userRequestDTO){
        Users user = new Users();
        user.setFname(userRequestDTO.getFname());
        user.setLname(userRequestDTO.getLname());
        user.setEmail(userRequestDTO.getEmail());
        usersService.insertUser(user);
    }
}
