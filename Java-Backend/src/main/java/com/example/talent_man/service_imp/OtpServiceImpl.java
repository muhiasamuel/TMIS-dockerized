package com.example.talent_man.service_imp;
import com.example.talent_man.services.OtpService;
import com.example.talent_man.services.EmailService;
import com.example.talent_man.repos.user.OtpRepository;
import com.example.talent_man.models.user.Otp;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class OtpServiceImpl implements OtpService {

    @Autowired
    private OtpRepository otpRepository;
    @Autowired
    private EmailService emailService;

    @Override
    public void generateAndSendOtp(String username) {
        String otpCode = String.valueOf(new Random().nextInt(999999));
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(15);

        Otp otp = new Otp();
        otp.setUsername(username);
        otp.setOtp(otpCode);
        otp.setExpiryTime(expiryTime);

        otpRepository.save(otp);
        emailService.sendOtpEmail(username, otpCode);
    }


    @Override
    public boolean validateOtp(String username, String otpCode) {
        System.out.println(username + "1232" + otpCode);

        // Check if the username is in email format
        boolean isEmail = username.contains("@");

        Optional<Otp> otpOptional;

        if (isEmail) {
            // Query OTP records by email
            otpOptional = otpRepository.findByUsernameAndOtp(username, otpCode);
        } else {
            // Query OTP records by username
            otpOptional = otpRepository.findByUsernameAndOtp(username, otpCode);
        }

        System.out.println(" hello12345" + otpOptional);

        if (otpOptional.isPresent()) {
            Otp otp = otpOptional.get();
            if (otp.getExpiryTime().isAfter(LocalDateTime.now())) {
                System.out.println(" hello there:" + otp);
                // The OTP is valid and has not expired
                otpRepository.delete(otp); // Clean up the OTP after successful validation
                return true;
            }
        }
        return false; // OTP not found or expired
    }

}
