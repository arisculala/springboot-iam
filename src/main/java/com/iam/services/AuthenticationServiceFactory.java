package com.iam.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthenticationServiceFactory {

    @Value("${authentication.provider}")
    private String authProvider;

    private final KeycloakClientService keycloakClientService;
    private final InternalAuthService internalAuthService;

    public AuthenticationServiceFactory(KeycloakClientService keycloakClientService,
            InternalAuthService internalAuthService) {
        this.keycloakClientService = keycloakClientService;
        this.internalAuthService = internalAuthService;
    }

    @Bean
    public AuthenticationService authenticationService() {
        if ("keycloak".equalsIgnoreCase(authProvider)) {
            return keycloakClientService;
        } else if ("internal".equalsIgnoreCase(authProvider)) {
            return internalAuthService;
        } else {
            throw new IllegalArgumentException("Unsupported authentication provider: " + authProvider);
        }
    }
}
