package com.personalisedlearn.poseidon.service;

import com.personalisedlearn.poseidon.dto.UserRequest;
import com.personalisedlearn.poseidon.dto.UserResponse;
import com.personalisedlearn.poseidon.exception.DuplicateUsernameException;
import com.personalisedlearn.poseidon.exception.ResourceNotFoundException;
import com.personalisedlearn.poseidon.model.User;
import com.personalisedlearn.poseidon.repository.UserRepository;
import com.personalisedlearn.poseidon.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import com.personalisedlearn.poseidon.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final UserMapper userMapper;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public List<UserResponse> getAllUsers() {
        try {
            List<User> users = userRepository.findAll();
            if (users == null || users.isEmpty()) {
                return Collections.emptyList();
            }
            return users.stream()
                    .map(userMapper::toResponse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error fetching users", e);
            return Collections.emptyList();
        }
    }

    public UserResponse getUserById(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return userMapper.toResponse(user);
    }

    public UserResponse createUser(UserRequest userRequest) {
        if (userRepository.existsByUsername(userRequest.getUsername())) {
            throw new DuplicateUsernameException("Username already exists");
        }
        
        User user = userMapper.toEntity(userRequest);
        User savedUser = userRepository.save(user);
        return userMapper.toResponse(savedUser);
    }

    public UserResponse updateUser(String id, UserRequest userRequest) {
        return userRepository.findById(id)
                .map(user -> {
                    userMapper.updateEntityFromDto(userRequest, user);
                    User updatedUser = userRepository.save(user);
                    return userMapper.toResponse(updatedUser);
                })
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    @Transactional
    public void deleteUser(String id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        
        // Delete all posts by this user
        try {
            postRepository.deleteAllByUserId(id);
            logger.info("Deleted all posts for user: {}", id);
        } catch (Exception e) {
            logger.error("Error deleting posts for user: " + id, e);
            throw new RuntimeException("Failed to delete user's posts", e);
        }
        
        // Delete the user
        userRepository.delete(user);
        logger.info("Deleted user: {}", id);
    }

    public UserResponse getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        return userMapper.toResponse(user);
    }
    
    public boolean usernameExists(String username) {
        return userRepository.existsByUsername(username);
    }
}
