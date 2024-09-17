package com.example.talent_man.controllers.succession;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SuccessionPlanResponseDto {
    private Long planId;
    private String riskRating;
    private Long currentRoleHolderId;
    private String currentRoleHolderName;
    private Long departmentId;
    private String departmentName;
    private Long positionId;
    private String positionName;
    private Long driverId;
    private String driverName;
    private List<ReadyUserDto> readyUsers; // List of ready users, including development needs and interventions
    private List<ExternalSuccessorDto> externalSuccessors; // List of external successors

    @Data
    public static class ReadyUserDto {
        private String readyUserName;
        private String readinessLevel;
        private List<DevelopmentNeedDto> developmentNeeds; // List of development needs
        private List<InterventionDto> interventions; // List of interventions for the ready user
    }

    @Data
    public static class ExternalSuccessorDto {
        private String externalSuccessorContact;
        private String externalSuccessorCurrentComp;
        private String externalSuccessorPosition;
        private String externalSuccessorName;
        private String externalSuccessorSelectionReason;
    }

    @Data
    public static class DevelopmentNeedDto {
        private String developmentNeedType;
        private String developmentNeedDescription;
    }

    @Data
    public static class InterventionDto {
        private String interventionDescription;
        private String interventionType;
        private String status;
        private Date startDate;
        private Date endDate;
    }
}
