package com.example.talent_man.dto.assessment;

import lombok.Data;

@Data
public class ManagerUserAssessmentStatusDto {
    private int userId;
    private String username;
    private String userFullName;
    private String pf;
    private boolean selfAssessed; // Indicates if the user has self-assessed
    private boolean managerAssessed; // Indicates if the manager has assessed the user

    // Getters and Setters
}

