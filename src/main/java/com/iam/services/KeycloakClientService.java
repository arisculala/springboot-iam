package com.iam.services;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.generic.exceptions.BadRequestException;
import com.generic.exceptions.UnauthorizedException;

import io.swagger.v3.oas.annotations.tags.Tag;

@Service
@Tag(name = "Keycloak Client Service", description = "Handles Keycloak authentication and user management")
public class KeycloakClientService implements AuthenticationService {

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
     * Authenticates a user with Keycloak using username and password.
     *
     * @param username The username of the user.
     * @param password The password of the user.
     * @return A valid Keycloak access token if authentication is successful.
     */
    @SuppressWarnings({ "rawtypes", "null" })
    public Map<String, Object> authenticateUser(String username, String password) {
        String tokenUrl = keycloakServerUrl + "/realms/" + keycloakRealm + "/protocol/openid-connect/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // encode form parameters
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
                // extract required fields from response
                Map<String, Object> responseBody = new HashMap<>();
                responseBody.put("access_token", response.getBody().get("access_token"));
                responseBody.put("expires_in", response.getBody().get("expires_in"));
                responseBody.put("token_type", response.getBody().get("token_type"));
                responseBody.put("refresh_token", response.getBody().get("refresh_token"));
                responseBody.put("refresh_expires_in", response.getBody().get("refresh_expires_in"));

                return responseBody;
            }
        } catch (Exception e) {
            logger.error("Error while authenticating user: {}", e);
            throw new UnauthorizedException("Invalid username or password.");
        }
        throw new UnauthorizedException("Invalid username or password.");
    }

    /**
     * Refreshes the access token using a valid refresh token.
     *
     * @param refreshToken The refresh token obtained during authentication.
     * @return A map containing the new access token, its expiration time, token
     *         type, and a new refresh token.
     */
    @SuppressWarnings({ "rawtypes", "null" })
    public Map<String, Object> refreshToken(String refreshToken) {
        logger.debug("CALLING . . . . . . . . . . refreshToken");

        String tokenUrl = keycloakServerUrl + "/realms/" + keycloakRealm + "/protocol/openid-connect/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> bodyParams = new LinkedMultiValueMap<>();
        bodyParams.add("client_id", clientId);
        bodyParams.add("client_secret", clientSecret);
        bodyParams.add("grant_type", "refresh_token");
        bodyParams.add("refresh_token", refreshToken);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(bodyParams, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(tokenUrl, HttpMethod.POST, requestEntity, Map.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {

                // extract required fields from response
                Map<String, Object> responseBody = new HashMap<>();
                responseBody.put("access_token", response.getBody().get("access_token"));
                responseBody.put("expires_in", response.getBody().get("expires_in"));
                responseBody.put("token_type", response.getBody().get("token_type"));
                responseBody.put("refresh_token", response.getBody().get("refresh_token"));
                responseBody.put("refresh_expires_in", response.getBody().get("refresh_expires_in"));

                return responseBody;
            }
        } catch (Exception e) {
            logger.error("Error while refreshing token: {}", e);
            throw new UnauthorizedException("Invalid or expired refresh token.");
        }
        throw new UnauthorizedException("Invalid or expired refresh token.");
    }

    /**
     * Obtains an access token from Keycloak using client credentials (use for
     * backend call to keycloak API's)
     *
     * @return A valid Keycloak access token.
     * @throws RuntimeException if the token retrieval fails.
     */
    @SuppressWarnings({ "rawtypes", "null" })
    public String getClientCredentialsAccessToken() {
        logger.debug("CALLING . . . . . . . . . . getClientCredentialsAccessToken");

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
            logger.error("Exception while fetching access token from Keycloak: {}", e);
        }
        throw new BadRequestException("Failed to retrieve access token.");
    }

    /**
     * Registers a new user in Keycloak.
     *
     * @param username  The username of the new user.
     * @param email     The email address of the new user.
     * @param firstName The first name of the new user.
     * @param lastName  The last name of the new user.
     * @param password  The password for the new user.
     * @return `true` if the user is successfully created, `false` otherwise.
     */
    public boolean registerKeycloakUser(String username, String email, String firstName, String lastName,
            String password) {
        logger.debug("CALLING . . . . . . . . . . registerKeycloakUser");

        try {
            String accessToken = getClientCredentialsAccessToken();

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

        return false;
    }
}