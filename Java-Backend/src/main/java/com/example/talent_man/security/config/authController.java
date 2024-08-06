package com.example.talent_man.security.config;

import com.example.talent_man.models.OTP.OTP;
import com.example.talent_man.models.OTP.OTPCode;
import com.example.talent_man.models.OTP.OTPRepository;
import com.example.talent_man.models.Responses.MessageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Objects;

public class authController {
    OTPRepository otpRepository;
    @PostMapping(path = "/verifyOTP")
    public ResponseEntity<?> validateOTP(@RequestBody OTPCode otpCode) {
        OTP otp = otpRepository.validOTP(otpCode.username);
        if (Objects.isNull(otp) || !Objects.equals(otp.getOtp(), otpCode.otp)) {
            return ResponseEntity.badRequest().body(new MessageResponse("OTP is not valid!"));
        } else {
            return ResponseEntity.ok(new MessageResponse("Welcome, OTP valid!"));
        }
    }
}
