package com.example.talent_man.dto.assessment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttributeQuestionDto implements Serializable {
    private int attributeId;
    private List<QuestionDto> questions;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuestionDto {
        private String assessmentQuestionDescription;
        private List<ChoiceDto> choices;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChoiceDto {
        private String choiceValue;
        private String choiceName;
    }
}
