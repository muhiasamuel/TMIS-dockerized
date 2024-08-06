package com.example.talent_man.dto;

import com.example.talent_man.models.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MVPsaAssessmentRequestDto {
    private String impactOfAttrition;
    private String marketExposure;
    private String careerPriorities;
    private String retentionAssessmentState;
}
