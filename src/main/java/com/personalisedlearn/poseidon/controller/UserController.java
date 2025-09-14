package com.personalisedlearn.poseidon.controller;

import com.personalisedlearn.poseidon.dto.UserRequest;
import com.personalisedlearn.poseidon.dto.UserResponse;
import com.personalisedlearn.poseidon.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
        logger.info("UserController initialized");
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        logger.info("GET /api/users - Fetching all users");
        List<UserResponse> users = userService.getAllUsers();
        logger.debug("Returning {} users", users.size());
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable String id) {
        logger.info("GET /api/users/{} - Fetching user by ID", id);
        UserResponse user = userService.getUserById(id);
        logger.debug("Found user: {}", user);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserResponse> getUserByUsername(@PathVariable("username") String username) {
        logger.info("GET /api/users/username/{} - Fetching user by username", username);
        UserResponse user = userService.getUserByUsername(username);
        logger.debug("Found user: {}", user != null ? user.getUsername() : "null");
        return ResponseEntity.ok(user);
    }
    
    @GetMapping("/check-username/{username}")
    public ResponseEntity<Map<String, Boolean>> checkUsernameAvailability(@PathVariable("username") String username) {
        logger.info("GET /api/users/check-username/{} - Checking username availability", username);
        boolean isAvailable = !userService.usernameExists(username);
        logger.debug("Username {} available: {}", username, isAvailable);
        return ResponseEntity.ok(Collections.singletonMap("available", isAvailable));
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest userRequest) {
        return new ResponseEntity<>(
                userService.createUser(userRequest),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable String id,
            @Valid @RequestBody UserRequest userRequest
    ) {
        return ResponseEntity.ok(userService.updateUser(id, userRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
