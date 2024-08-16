package com.example.talent_man.dto.assessment;

import com.example.talent_man.dto.assessment.ChoiceDto;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

@Data
public class QuestionDto implements Serializable {
    private int assessmentQuestionId;
    private String assessmentQuestionDescription;
    private Set<ChoiceDto> choices;
}

