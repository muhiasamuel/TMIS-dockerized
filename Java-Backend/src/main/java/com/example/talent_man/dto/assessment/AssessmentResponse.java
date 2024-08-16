package com.example.talent_man.dto.assessment;

import lombok.Data;

@Data
public class AssessmentResponse {
    private AssessmentDto assessment;
    private boolean attempted; // Indicates if the assessment has been attempted
}