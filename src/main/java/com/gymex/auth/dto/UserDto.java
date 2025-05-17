package com.gymex.auth.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private UUID id;
    private String keycloakId;
    private String fullName;
    private String email;
    private String phone;
    private String membershipType;
    private String city;
    private String preferredGym;
}
