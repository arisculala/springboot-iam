package com.iam.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.generic.dto.SuccessErrorResponseDTO;
import com.iam.dto.NewUserDTO;
import com.iam.dto.UserDTO;
import com.iam.services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User Management", description = "Endpoints for managing users")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Create a New User", description = "Creates a new user and returns user details.", responses = {
            @ApiResponse(responseCode = "200", description = "User created successfully", content = @Content(schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "409", description = "User already exists") })
    @PutMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody NewUserDTO newUserDTO) {
        logger.info("Calling createUser endpoint");
        UserDTO newUser = userService.createUser(newUserDTO);
        return ResponseEntity.ok(newUser);
    }

    @Operation(summary = "Get User by ID", description = "Fetches a user by their unique ID.", responses = {
            @ApiResponse(responseCode = "200", description = "User found", content = @Content(schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUserById(
            @Parameter(description = "User ID", required = true) @PathVariable Long userId) {
        logger.info("Calling getUserById endpoint");
        UserDTO user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Get User by Email", description = "Fetches a user by their email address.", responses = {
            @ApiResponse(responseCode = "200", description = "User found", content = @Content(schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/email")
    public ResponseEntity<UserDTO> getUserByEmail(
            @Parameter(description = "User's email address", required = true) @RequestParam String email) {
        logger.info("Calling getUserByEmail endpoint");
        UserDTO user = userService.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Get User by Username", description = "Fetches a user by their username.", responses = {
            @ApiResponse(responseCode = "200", description = "User found", content = @Content(schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/username")
    public ResponseEntity<UserDTO> getUserByUsername(
            @Parameter(description = "User's username", required = true) @RequestParam String username) {
        logger.info("Calling getUserByUsername endpoint");
        UserDTO user = userService.getUserByUsername(username);
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Update User Status", description = "Enable or disable a user's account.", responses = {
            @ApiResponse(responseCode = "200", description = "User status updated successfully", content = @Content(schema = @Schema(implementation = SuccessErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PatchMapping("/{userId}/disable")
    public ResponseEntity<SuccessErrorResponseDTO> updateUserDisabledStatus(
            @Parameter(description = "User ID", required = true) @PathVariable Long userId,
            @Parameter(description = "Disable flag (true to disable, false to enable)", required = true) @RequestParam boolean disabled) {
        logger.info("Calling updateUserDisabledStatus endpoint");
        userService.updateUserDisabledStatus(userId, disabled);
        String message = disabled ? "User status successfully disabled." : "User status successfully enabled.";
        return ResponseEntity.ok(new SuccessErrorResponseDTO(HttpStatus.OK, true, message));
    }

    @Operation(summary = "Update User Password", description = "Updates a user's password after validating the old password.", responses = {
            @ApiResponse(responseCode = "200", description = "Password updated successfully", content = @Content(schema = @Schema(implementation = SuccessErrorResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid old password or mismatched new passwords"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PatchMapping("/{userId}/password")
    public ResponseEntity<SuccessErrorResponseDTO> updateUserPassword(
            @Parameter(description = "User ID", required = true) @PathVariable Long userId,
            @Parameter(description = "Old password", required = true) @RequestParam String oldPassword,
            @Parameter(description = "New password", required = true) @RequestParam String newPassword,
            @Parameter(description = "Re-entered new password", required = true) @RequestParam String reenterNewPassword) {
        logger.info("Calling updateUserPassword endpoint");
        userService.updateUserPassword(userId, oldPassword, newPassword, reenterNewPassword);
        return ResponseEntity.ok(new SuccessErrorResponseDTO(HttpStatus.OK, true, "Password updated successfully"));
    }
}
