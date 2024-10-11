package com.example.talent_man.dto.user;

import lombok.Data;

@Data
public class UserDetailsDto {
    private Long userId;
    private String pf;
    private String userFullName;
    private String userEmail;
    private String roleName;
    private Boolean isEnabled;
    private Boolean isLocked;
    private String userType;
    private String managerName;
    private String departmentName;
    private Long departmentId;
    private String positionName;
    private Long positionId;

    // Getters and Setters
}
