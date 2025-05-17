package com.gymex.auth.service;

import org.springframework.stereotype.Service;

@Service
public class OTPService {

    private final EmailService eailService ;

    public OTPService(EmailService eailService) {
        this.eailService = eailService;
    }

    public void sendOtp(String email, String otp) {
        try {
            eailService.sendOtpEmail(email, otp);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send OTP email: " + e.getMessage());
        }
    }
}
