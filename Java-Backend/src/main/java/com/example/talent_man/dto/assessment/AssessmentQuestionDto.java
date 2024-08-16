package com.example.talent_man.dto.assessment;

import com.example.talent_man.dto.assessment.ChoiceDto;
import lombok.Data;
import java.util.List;

@Data
public class AssessmentQuestionDto {
    private int AssessmentQuestionId;
    private String assessmentQuestionDescription;
    private List<ChoiceDto> choices; // Assuming you have a Choice DTO

    // Add other fields as necessary
}


