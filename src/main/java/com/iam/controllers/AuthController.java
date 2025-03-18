package com.iam.controllers;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.generic.exceptions.UnauthorizedException;
import com.iam.services.AuthenticationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "API for user authentication and token management")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthenticationService authenticationService;

    @Autowired
    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Operation(summary = "User login", description = "Authenticates a user using their username and password, returning an access token.")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) {
        try {
            Map<String, Object> accessCredentials = authenticationService.authenticateUser(username, password);

            Map<String, Object> response = new HashMap<>();
            response.put("access_token", accessCredentials.get("access_token"));
            response.put("expires_in", accessCredentials.get("expires_in"));
            response.put("token_type", accessCredentials.get("token_type"));
            response.put("refresh_token", accessCredentials.get("refresh_token"));
            response.put("refresh_expires_in", accessCredentials.get("refresh_expires_in"));

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error authenticate login: {}", e);
            throw new UnauthorizedException("Invalid credentials.");
        }
    }

    @Operation(summary = "Refresh access token", description = "Uses a refresh token to generate a new access token.")
    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestParam String refreshToken) {
        try {
            Map<String, Object> accessCredentials = authenticationService.refreshToken(refreshToken);

            Map<String, Object> response = new HashMap<>();
            response.put("access_token", accessCredentials.get("access_token"));
            response.put("expires_in", accessCredentials.get("expires_in"));
            response.put("token_type", accessCredentials.get("token_type"));
            response.put("refresh_token", accessCredentials.get("refresh_token"));
            response.put("refresh_expires_in", accessCredentials.get("refresh_expires_in"));

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error authenticate login: {}", e);
            throw new UnauthorizedException("Invalid credentials.");
        }
    }
}
