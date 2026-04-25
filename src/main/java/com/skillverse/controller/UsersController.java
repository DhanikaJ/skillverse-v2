package com.skillverse.controller;

import com.skillverse.dto.UserDTO;
import com.skillverse.model.Users;
import com.skillverse.service.UsersService;
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
    public void  addNewUser(@RequestBody  Users users){
        usersService.insertUser(users);
    }
}
