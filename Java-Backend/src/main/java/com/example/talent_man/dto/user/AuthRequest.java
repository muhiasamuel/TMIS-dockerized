package com.example.talent_man.dto.user;

import lombok.Data;

@Data
public class AuthRequest {
    private String username;
    private String password;
}