package com.example.talent_man.dto.assessment;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class AssessedVsUnassessedDto implements Serializable {
    private int id;
    private String assName;
    private String assDescription;
    private List<UserAssessmentDto> assessed;
    private List<UserAssessmentDto> unAssessed;
}
