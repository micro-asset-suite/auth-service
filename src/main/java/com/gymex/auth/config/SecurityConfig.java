package com.gymex.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    @Order(1)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // Proper way to disable CSRF in Spring Security 5+
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/auth/send-otp",
                                "/auth/verify-otp",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/swagger-resources/**",
                                "/webjars/**",
                                "/configuration/ui",
                                "/configuration/security",
                                "/public/**",
                                "/signup"
                        ).permitAll()  // Public endpoints
                        .anyRequest().authenticated()  // All others need authentication
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(Customizer.withDefaults())  // Using JWT
                );

        return http.build();
    }


    @Bean
    public JwtDecoder jwtDecoder() {
        // Replace this URL with your Keycloak public key URL or use the auto-detection.
        return JwtDecoders.fromIssuerLocation("http://host.docker.internal:8080/realms/gymex-realm");
    }
}

