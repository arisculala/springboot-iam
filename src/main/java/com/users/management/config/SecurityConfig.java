package com.users.management.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
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

    // @Bean
    // public SecurityFilterChain securityFilterChain(HttpSecurity http) throws
    // Exception {
    // http
    // .csrf(csrf -> csrf.disable())
    // .authorizeHttpRequests(auth -> auth
    // .requestMatchers("/api/v1/public/**").permitAll() // Public endpoints
    // .requestMatchers("/test/admin/**").hasRole("ADMIN") // Only Admins
    // .anyRequest().authenticated())
    // .sessionManagement(sess ->
    // sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
    // .oauth2ResourceServer(
    // oauth2 -> oauth2.jwt(jwt ->
    // jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())));

    // return http.build();
    // }

    // @Bean
    // public JwtAuthenticationConverter jwtAuthenticationConverter() {
    // JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new
    // JwtGrantedAuthoritiesConverter();
    // grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_"); // Ensure roles are
    // prefixed

    // JwtAuthenticationConverter jwtAuthenticationConverter = new
    // JwtAuthenticationConverter();
    // jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
    // return jwtAuthenticationConverter;
    // }
}
