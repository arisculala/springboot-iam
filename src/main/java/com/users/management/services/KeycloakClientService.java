package com.users.management.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
public class KeycloakClientService {

    private static final Logger logger = LoggerFactory.getLogger(KeycloakClientService.class);

    @Value("${keycloak.server-url}")
    private String keycloakServerUrl;

    @Value("${keycloak.realm}")
    private String keycloakRealm;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Obtain Keycloak access token using client credentials
     * 
     * @return
     */
    @SuppressWarnings({ "rawtypes", "null" })
    public String getAccessToken() {
        String tokenUrl = keycloakServerUrl + "/realms/" + keycloakRealm + "/protocol/openid-connect/token";

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            String body = "client_id=" + URLEncoder.encode(clientId, StandardCharsets.UTF_8)
                    + "&client_secret=" + URLEncoder.encode(clientSecret, StandardCharsets.UTF_8)
                    + "&grant_type=client_credentials";

            HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);

            ResponseEntity<Map> response = restTemplate.exchange(tokenUrl, HttpMethod.POST, requestEntity, Map.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody().get("access_token").toString();
            } else {
                logger.error("Failed to retrieve access token. Response: {}", response);
            }
        } catch (Exception e) {
            logger.error("Exception while fetching access token from Keycloak: ", e);
        }

        throw new RuntimeException("Failed to retrieve access token from Keycloak.");
    }

    /**
     * Create a new user in Keycloak.
     */
    public boolean registerUser(String username, String email, String firstName, String lastName, String password) {
        try {
            String accessToken = getAccessToken();

            String userUrl = keycloakServerUrl + "/admin/realms/" + keycloakRealm + "/users";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(accessToken);

            // create request body
            Map<String, Object> user = new HashMap<>();
            user.put("username", username);
            user.put("enabled", true);
            user.put("email", email);
            user.put("firstName", firstName);
            user.put("lastName", lastName);

            // create credentials
            Map<String, Object> credential = new HashMap<>();
            credential.put("type", "password");
            credential.put("value", password);
            credential.put("temporary", false);

            user.put("credentials", new Map[] { credential });

            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(user, headers);

            ResponseEntity<String> response = restTemplate.exchange(userUrl, HttpMethod.POST, requestEntity,
                    String.class);

            logger.info("registerUser response status: {}", response.getStatusCode());
            logger.info("registerUser response body: {}", response.getBody());

            return response.getStatusCode() == HttpStatus.CREATED;
        } catch (HttpClientErrorException e) {
            logger.error("Client error while registering user: Status Code: {}, Response Body: {}",
                    e.getStatusCode(), e.getResponseBodyAsString(), e);
        } catch (HttpServerErrorException e) {
            logger.error("Server error while registering user: Status Code: {}, Response Body: {}",
                    e.getStatusCode(), e.getResponseBodyAsString(), e);
        } catch (ResourceAccessException e) {
            logger.error("Network error while registering user: {}", e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Unexpected error while registering user: {}", e.getMessage(), e);
        }

        return false; // return false if registration fails
    }

    @SuppressWarnings({ "rawtypes", "null" })
    public String authenticateUser(String username, String password) {
        String tokenUrl = keycloakServerUrl + "/realms/" + keycloakRealm + "/protocol/openid-connect/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Use LinkedMultiValueMap to properly encode form parameters
        MultiValueMap<String, String> bodyParams = new LinkedMultiValueMap<>();
        bodyParams.add("client_id", clientId);
        bodyParams.add("client_secret", clientSecret);
        bodyParams.add("grant_type", "password");
        bodyParams.add("username", username);
        bodyParams.add("password", password);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(bodyParams, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(tokenUrl, HttpMethod.POST, requestEntity, Map.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody().get("access_token").toString();
            }
        } catch (Exception e) {
            throw new RuntimeException("Error while authenticating user: " + e.getMessage());
        }

        throw new RuntimeException("Invalid username or password.");
    }
}