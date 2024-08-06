package com.example.talent_man.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CriticalRolesAssessmentDto {
    private String roleName;
    private String currentState;
    private String strategicImportance;
    private String riskImpact;
    private String vacancyRisk;
    private String impactOnOperation;
    private String skillExperience;
    private int addedBy;
    private List<RolesStrategiesDto>RoleDevelopmentStrategies;
    private double averageRating;
}
