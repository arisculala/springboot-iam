package com.users.management.controllers;

import com.generic.utility.dto.SuccessResponseDTO;
import com.users.management.dto.UserDTO;
import com.users.management.services.UserService;

import io.swagger.v3.oas.annotations.Operation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Get User by ID", description = "Fetches a user by their ID. If the user exists, it returns the user's details, otherwise it returns a 404 Not Found.")
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        UserDTO user = userService.getUserById(id);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Get User by email", description = "Fetches a user by their email. If the user exists, it returns the user's details, otherwise it returns a 404 Not Found.")
    @GetMapping("/email")
    public ResponseEntity<UserDTO> getUserByEmail(@RequestParam String email) {
        UserDTO user = userService.getUserByEmail(email);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Get User by username", description = "Fetches a user by their username. If the user exists, it returns the user's details, otherwise it returns a 404 Not Found.")
    @GetMapping("/username")
    public ResponseEntity<UserDTO> getUserByUsername(@RequestParam String username) {
        UserDTO user = userService.getUserByUsername(username);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Create a New User", description = "Create a new user. The user details (username, email, and password) are provided in the request body. If the user is created successfully, it returns the user details; otherwise, it returns a 400 or 409 error based on the failure reason.")
    @PutMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO newUserDTO) {
        UserDTO newUser = userService.createUser(newUserDTO.getUsername(), newUserDTO.getEmail(),
                newUserDTO.getPassword(), newUserDTO.getFirstName(), newUserDTO.getLastName());
        return ResponseEntity.ok(newUser);
    }

    @Operation(summary = "Update user status", description = "Update user status enable or disable")
    @PatchMapping("/{id}/disable")
    public ResponseEntity<SuccessResponseDTO> updateUserDisabledStatus(
            @PathVariable Long id,
            @RequestParam boolean disabled) {
        userService.updateUserDisabledStatus(id, disabled);
        if (!disabled) {
            return ResponseEntity.ok(new SuccessResponseDTO(true, "User status enabled successfully"));
        }
        return ResponseEntity.ok(new SuccessResponseDTO(true, "User status disabled successfully"));
    }

    @Operation(summary = "Update user password", description = "Update user password")
    @PatchMapping("/{id}/password")
    public ResponseEntity<SuccessResponseDTO> updateUserPassword(
            @PathVariable Long id,
            @RequestParam String oldPassword,
            @RequestParam String newPassword,
            @RequestParam String reenterNewPassword) {
        userService.updateUserPassword(id, oldPassword, newPassword, reenterNewPassword);
        return ResponseEntity.ok(new SuccessResponseDTO(true, "Password updated successfully"));
    }
}
