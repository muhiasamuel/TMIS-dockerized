package com.example.talent_man.dto.user;
import lombok.Data;

@Data
public class OtpRequest {
    private String username;
    private String otp;
}