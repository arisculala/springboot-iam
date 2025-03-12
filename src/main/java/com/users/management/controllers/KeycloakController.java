package com.users.management.controllers;

import com.generic.utility.dto.SuccessResponseDTO;
import com.users.management.services.KeycloakClientService;

import io.swagger.v3.oas.annotations.Operation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class KeycloakController {

    private static final Logger logger = LoggerFactory.getLogger(KeycloakController.class);

    private final KeycloakClientService keycloakClientService;

    public KeycloakController(KeycloakClientService keycloakClientService) {
        this.keycloakClientService = keycloakClientService;
    }

    @Operation(summary = "Register a new user", description = "Creates a new user in Keycloak.")
    @PostMapping("/register")
    public ResponseEntity<SuccessResponseDTO> registerUser(
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String password) {

        logger.info("Registering user with username: {}", username);

        boolean success = keycloakClientService.registerUser(username, email, firstName, lastName, password);
        if (success) {
            return ResponseEntity.ok(new SuccessResponseDTO(true, "User registered successfully"));
        }
        return ResponseEntity.badRequest().body(new SuccessResponseDTO(false, "User registration failed"));
    }
}
