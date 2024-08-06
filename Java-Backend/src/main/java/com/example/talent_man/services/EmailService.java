package com.example.talent_man.services;

public interface EmailService {
    void sendOtpEmail(String username, String otpCode);

    void sendEmail(String to, String subject, String text);

}
