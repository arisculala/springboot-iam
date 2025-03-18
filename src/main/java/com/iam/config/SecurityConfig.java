package com.iam.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for simplicity (enable it properly in production)
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // Allow all requests
                )
                .formLogin(form -> form.disable()) // Disable form login (if using JWT or token-based authentication)
                .httpBasic(basic -> basic.disable()); // Disable basic authentication (enable if needed)

        return http.build();
    }
}
