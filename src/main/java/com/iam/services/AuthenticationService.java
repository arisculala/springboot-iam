package com.iam.services;

import java.util.Map;

public interface AuthenticationService {

    /**
     * Authenticates a user and returns an access token, refresh token, and
     * expiration info.
     *
     * @param username The username of the user.
     * @param password The password of the user.
     * @return A map containing access token, refresh token, and expiration details.
     */
    Map<String, Object> authenticateUser(String username, String password);

    /**
     * Refreshes an access token using a refresh token.
     *
     * @param refreshToken The refresh token.
     * @return A new access token and refresh token if successful.
     */
    Map<String, Object> refreshToken(String refreshToken);

    /**
     * Retrieves an access token using client credentials (for backend-to-backend
     * communication).
     *
     * @return The client credentials access token.
     */
    String getClientCredentialsAccessToken();
}
