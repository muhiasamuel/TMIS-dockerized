package com.example.talent_man.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPerformanceDTO {

    private int userId;
    private String pf_No;
    private String username;
    private String userFullName;
    private String departmentName;
    private String positionName;
    private int managerId;
    private Double averagePerformance;
    private Double averagePotential;
    private String choiceValue;
    private Double userAssessmentAvg;
    private String manChoiceValue;
    private Double manAssessmentAvg;
    private String assessmentName;
    private String potentialAttributeName;
    private String talentRating;
    private String potentialRating;
    private Integer performanceRating;
    private int performanceYear;
}
