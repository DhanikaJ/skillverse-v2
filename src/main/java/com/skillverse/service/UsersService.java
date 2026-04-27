package com.skillverse.service;

import com.skillverse.dto.UserDTO;
import com.skillverse.exception.ResourceNotFoundException;
import com.skillverse.mapper.EntityMapper;
import com.skillverse.model.Users;
import com.skillverse.repository.UsersRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsersService {
    private  final  UsersRepository usersRepository;
    private final EntityMapper entityMapper;

    public UsersService(UsersRepository usersRepository, EntityMapper entityMapper) {
        this.usersRepository = usersRepository;
        this.entityMapper = entityMapper;
    }

    public  void insertUser(Users users) {
        usersRepository.save(users);
    }

    public List<UserDTO> getUsers() {
        return entityMapper.toUserDTOList(usersRepository.findAll());
    }

    public Page<UserDTO> getUsersWithPagination(Pageable pageable) {
        return usersRepository.findAll(pageable)
                .map(entityMapper::toUserDTO);
    }

    public UserDTO getUsersById(Integer id) {
        Users user = usersRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
        return entityMapper.toUserDTO(user);
    }
}
