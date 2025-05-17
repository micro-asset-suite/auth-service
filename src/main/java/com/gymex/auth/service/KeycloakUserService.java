package com.gymex.auth.service;

import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KeycloakUserService {

    private final Keycloak keycloak;
    private final String realm;

    public KeycloakUserService(Keycloak keycloak, @Value("${keycloak.realm}") String realm) {
        this.keycloak = keycloak;
        this.realm = realm;
    }

    public String createUser( String email, String password,  String roleName) {
        // Set user data
        UserRepresentation user = new UserRepresentation();
        user.setEmail(email);
        user.setUsername(email);
        user.setEnabled(false);
        user.setEmailVerified(false);

        // Set credentials
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setTemporary(false);
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        user.setCredentials(List.of(credential));

        // Create user
        Response response = keycloak.realm(realm).users().create(user);
        if (response.getStatus() != 201) {
            throw new RuntimeException("Failed to create user: " + response.getStatus());
        }

        String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");

        // Assign role
        RoleRepresentation realmRole = keycloak.realm(realm).roles().get(roleName).toRepresentation();
        keycloak.realm(realm).users().get(userId).roles().realmLevel().add(List.of(realmRole));

        return userId;
    }
    public void enableUserInKeycloak(String email) {
        List<UserRepresentation> users = keycloak.realm(realm).users().search(email);
        if (users.isEmpty()) {
            throw new RuntimeException("User not found in Keycloak for email: " + email);
        }

        UserRepresentation user = users.get(0); // Assume email is unique
        user.setEnabled(true); // ✅ Enable user
        user.setEmailVerified(true);

        keycloak.realm(realm).users().get(user.getId()).update(user);
    }
}
