package com.example.talent_man.dto.assessment;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ManagerUserAssessmentStatusDto {
    private int assessmentId;
    private String assessmentName;
    private String assessmentDescription;
    private String assessmentExpiry;
    private List<AssessmentStatus> assessmentStatuses; // Added for storing user statuses

    @Data
    public static class AssessmentStatus implements Serializable {
        private int userId;
        private String username;
        private String userFullName;
        private String pf;
        private boolean selfAssessed;
        private boolean managerAssessed;
    }
}
