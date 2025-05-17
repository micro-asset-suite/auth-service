package com.gymex.auth.service;

import com.gymex.auth.dto.SignupRequest;
import com.gymex.auth.dto.VerifyOtpRequest;
import com.gymex.auth.entity.OtpVerification;
import com.gymex.auth.repository.OtpVerificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final OtpVerificationRepository otpRepo;
    // You will inject SendGridService or JavaMailSender here later.
    private final EmailService emailService;
    private final KeycloakUserService keycloakUserService;

    public void sendOtp(SignupRequest request) {
        String otp = generateOtp();

        OtpVerification otpVerification = OtpVerification.builder()
                .email(request.getEmail())
                .otp(otp)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(5))
                .verified(false)
                .build();

        otpRepo.save(otpVerification);
        Optional<OtpVerification> topByEmailOrderByCreatedAtDesc = otpRepo.findTopByEmailOrderByCreatedAtDesc(request.getEmail());

        emailService.sendOtpEmail(request.getEmail(), otpVerification.getOtp());

        String user = keycloakUserService.createUser(request.getEmail(), request.getPassword(), "user");

        // TODO: Send OTP via SendGrid or other email service
        System.out.println("OTP for " + request.getEmail() + " is: " + otp); // Remove in prod
    }

    public boolean verifyOtp(VerifyOtpRequest request) {
        return otpRepo.findTopByEmailOrderByCreatedAtDesc(request.getEmail())
                .filter(otp -> !otp.isVerified() &&
                               otp.getOtp().equals(request.getOtp()) &&
                               otp.getExpiresAt().isAfter(LocalDateTime.now()))
                .map(otp -> {
                    otp.setVerified(true);
                    otpRepo.save(otp);
                    keycloakUserService.enableUserInKeycloak(request.getEmail());
                    return true;
                })
                .orElse(false);
    }

    private String generateOtp() {
        return String.format("%06d", new Random().nextInt(999999));
    }
}
