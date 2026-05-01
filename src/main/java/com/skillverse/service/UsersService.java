package com.skillverse.service;

import com.skillverse.dto.UserDTO;
import com.skillverse.exception.ResourceNotFoundException;
import com.skillverse.mapper.EntityMapper;
import com.skillverse.model.Users;
import com.skillverse.repository.UsersRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service class for managing User operations.
 * Provides functionality for retrieving, creating, updating, and managing users.
 */
@Service
public class UsersService {
    private final UsersRepository usersRepository;
    private final EntityMapper entityMapper;

    public UsersService(UsersRepository usersRepository, EntityMapper entityMapper) {
        this.usersRepository = usersRepository;
        this.entityMapper = entityMapper;
    }

    /**
     * Creates and saves a new user.
     *
     * @param user the user entity to create
     */
    @Transactional
    public void createUser(Users user) {
        usersRepository.save(user);
    }

    /**
     * Retrieves all users.
     *
     * @return a list of all UserDTO objects
     */
    public List<UserDTO> getUsers() {
        return entityMapper.toUserDTOList(usersRepository.findAll());
    }

    /**
     * Retrieves users with pagination support.
     *
     * @param pageable the pagination configuration
     * @return a page of UserDTO objects
     */
    public Page<UserDTO> getUsersWithPagination(Pageable pageable) {
        return usersRepository.findAll(pageable)
                .map(entityMapper::toUserDTO);
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id the user ID
     * @return the UserDTO object
     * @throws ResourceNotFoundException if the user is not found
     */
    public UserDTO getUserById(Integer id) {
        Users user = usersRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
        return entityMapper.toUserDTO(user);
    }

    /**
     * Updates an existing user.
     *
     * @param id the user ID to update
     * @param userRequest the updated user data
     * @return the updated UserDTO object
     * @throws ResourceNotFoundException if the user is not found
     */
    @Transactional
    public UserDTO updateUser(Integer id, Users userRequest) {
        Users existingUser = usersRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));

        existingUser.setFname(userRequest.getFname());
        existingUser.setLname(userRequest.getLname());
        existingUser.setEmail(userRequest.getEmail());
        existingUser.setPhoto(userRequest.getPhoto());

        Users updatedUser = usersRepository.save(existingUser);
        return entityMapper.toUserDTO(updatedUser);
    }

    /**
     * Deletes a user by their ID.
     *
     * @param id the user ID to delete
     * @throws ResourceNotFoundException if the user is not found
     */
    @Transactional
    public void deleteUser(Integer id) {
        Users user = usersRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
        usersRepository.delete(user);
    }
}
