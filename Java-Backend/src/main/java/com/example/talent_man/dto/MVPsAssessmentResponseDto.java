package com.example.talent_man.dto;

import com.example.talent_man.dto.user.retentionStrategiesDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MVPsAssessmentResponseDto {
        private String userFullName;
        private int employeeId;
        private String careerPriority;
        private String impactOfAttrition;
        private String marketExposure;
        private String retentionState;
        private List<retentionStrategiesDto> strategies;

        // Constructors, getters, and setters

}
