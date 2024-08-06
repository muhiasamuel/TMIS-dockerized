package com.example.talent_man.dto.assessment;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class AssessedAssessmentDto implements Serializable {
    private int id;
    private String assName;
    private String assDescription;
    private UserAssessmentDto doneBy;

}
