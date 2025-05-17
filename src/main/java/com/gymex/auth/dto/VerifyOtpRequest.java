package com.gymex.auth.dto;

import lombok.Data;

@Data
public class VerifyOtpRequest {
    private String email;
    private String otp;

}
