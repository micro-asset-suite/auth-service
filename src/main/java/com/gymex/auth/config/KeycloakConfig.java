package com.gymex.auth.config;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class KeycloakConfig {
    @Value("${keycloak.auth-server-url}")
    private String serverUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.admin.username}")
    private String adminUsername;

    @Value("${keycloak.admin.password}")
    private String adminPassword;

    @Bean
    public Keycloak keycloakAdminClient() {
        return KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm("master")
                .username(adminUsername)
                .password(adminPassword)
                .clientId("admin-cli")
                .grantType(OAuth2Constants.PASSWORD)
                .build();
    }

    @Bean
    public String realmName() {
        return realm;
    }
}
