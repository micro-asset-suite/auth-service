package com.gymex.auth.controller;

import com.gymex.auth.dto.SignupRequest;
import com.gymex.auth.dto.VerifyOtpRequest;
import com.gymex.auth.service.AuthService;
import com.gymex.auth.service.KeycloakAdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final KeycloakAdminService keycloakAdminService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody SignupRequest request) {
        keycloakAdminService.createUser(request);
        return ResponseEntity.ok("User created successfully in Keycloak.");
    }
    @Operation(summary = "Send OTP to email")
    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOtp(@RequestBody SignupRequest request) {
        authService.sendOtp(request);
        return ResponseEntity.ok("OTP sent to email: " + request.getEmail());
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody VerifyOtpRequest request) {
        boolean isValid = authService.verifyOtp(request);
        if (isValid) {
            return ResponseEntity.ok("OTP verified successfully.");
        } else {
            return ResponseEntity.status(400).body("Invalid or expired OTP.");
        }
    }
}
