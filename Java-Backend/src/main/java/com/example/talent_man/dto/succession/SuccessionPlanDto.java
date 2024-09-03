package com.example.talent_man.dto.succession;

import com.example.talent_man.models.succession.ExternalSuccessorDto;
import lombok.Data;

import java.util.List;

@Data
public class SuccessionPlanDto {
    private int planId;
    private int departmentId;
    private int driverId;
    private int positionId; // Key role as a Position ID
    private String retentionRiskRating;
    private int currentRoleHolderId;

    private List<ReadyUserDto> readyNow; // List of Ready Users with their details
    private List<ReadyUserDto> readyAfterTwoYears; // List of Ready Users with their details
    private List<ReadyUserDto> readyMoreThanTwoYears; // List of Ready Users with their details

    private List<ProposedInterventionDto> proposedInterventions; // List of Proposed Interventions
    private List<SuccessorDevelopmentNeedDto> successorDevelopmentNeeds; // List of Successor Development Needs

    private List<ExternalSuccessorDto> externalSuccessor; // New field for External Successor

    // Getters and Setters
}
