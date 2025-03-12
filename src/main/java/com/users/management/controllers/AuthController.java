package com.users.management.controllers;

import com.generic.utility.dto.SuccessResponseDTO;
import com.users.management.services.KeycloakClientService;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final KeycloakClientService keycloakClientService;

    public AuthController(KeycloakClientService keycloakClientService) {
        this.keycloakClientService = keycloakClientService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) {
        logger.info("CHECKING . . . . . . . . . . login");
        try {
            String accessToken = keycloakClientService.authenticateUser(username, password);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Login successful");
            response.put("access_token", accessToken);
            response.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(401).body(new SuccessResponseDTO(false, "Invalid credentials"));
        }
    }

    // @GetMapping("/verify-token")
    // public ResponseEntity<?> verifyToken(@RequestHeader("Authorization") String
    // authorizationHeader) {
    // try {
    // // extract the Bearer token from the Authorization header
    // if (authorizationHeader != null && authorizationHeader.startsWith("Bearer "))
    // {
    // String accessToken = authorizationHeader.substring(7); // Remove "Bearer "
    // prefix

    // // use the access token for verification
    // boolean isValid = keycloakClientService.verifyToken(accessToken);

    // if (isValid) {
    // Map<String, Object> response = new HashMap<>();
    // response.put("success", true);
    // response.put("message", "Access token is valid");
    // response.put("timestamp", System.currentTimeMillis());

    // return ResponseEntity.ok(response);

    // } else {
    // return ResponseEntity.status(401).body(new SuccessResponseDTO(false, "Invalid
    // access token"));
    // }
    // } else {
    // return ResponseEntity.badRequest().body("Authorization header is missing or
    // invalid");
    // }
    // } catch (Exception e) {
    // return ResponseEntity.status(401).body(new SuccessResponseDTO(false, "Invalid
    // access token"));
    // }
    // }
}
