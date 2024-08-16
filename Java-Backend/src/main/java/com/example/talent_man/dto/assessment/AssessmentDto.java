package com.example.talent_man.dto.assessment;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
public class AssessmentDto implements Serializable {
    private int assessmentId;
    private String assessmentName;
    private String assessmentDescription;
    private LocalDate endDate;
    private List<PotentialAttributeDto> potentialAttributes;
}
