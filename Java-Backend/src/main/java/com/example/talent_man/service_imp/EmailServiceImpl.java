package com.example.talent_man.service_imp;

import com.example.talent_man.services.EmailService;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;

    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void sendOtpEmail(String username, String otpCode) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(username);
        mailMessage.setSubject("OTP Verification");
        mailMessage.setText("Your OTP for verification is: " + otpCode + " :Expires after 15 minutes");


        try {
            javaMailSender.send(mailMessage);
        } catch (MailException e) {
            // Handle exception appropriately
            e.printStackTrace();
        }
    }

    @Override
    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setFrom("Samuel");
        message.setText(text);
        javaMailSender.send(message);
    }
}
