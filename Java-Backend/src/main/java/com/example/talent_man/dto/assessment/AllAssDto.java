package com.example.talent_man.dto.assessment;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@NoArgsConstructor
public class AllAssDto implements Serializable {
    private int id;
    private String potentialAttributeName;
    private String assessmentName;
    private String assessmentDescription;
    private int doneBy;
    private int notDoneBy;
    private float averageScore = 0.F;
    private int totalQuestions;
}
