package com.example.talent_man.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private int userId;
    private String userType;
    private String userFullName;
    private String username;
    private String password;
    private Set<RoleResDto> roles;
}
