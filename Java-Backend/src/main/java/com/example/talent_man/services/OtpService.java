package com.example.talent_man.services;

public interface OtpService {
    void generateAndSendOtp(String username);
    boolean validateOtp(String username, String otpCode);
}