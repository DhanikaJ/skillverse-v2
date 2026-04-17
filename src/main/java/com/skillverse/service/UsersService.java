package com.skillverse.service;

import com.skillverse.model.Users;
import com.skillverse.repository.UsersRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsersService {
    private  final  UsersRepository usersRepository;

    UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public  void insertUser(Users users) {
        usersRepository.save(users);
    }

    public List<Users> getUsers() {
        return usersRepository.findAll();
    }

    public Users getUsersById(Integer id) {
        return usersRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException(id + "User not found"));
    }
}
