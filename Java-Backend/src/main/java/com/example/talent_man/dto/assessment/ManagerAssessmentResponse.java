package com.example.talent_man.dto.assessment;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
public class ManagerAssessmentResponse implements Serializable {
    private int userId;
    private String username;
    private String userFullName;
    private AssessmentDto assessment; // Includes all assessment details
    private List<AssessmentDto.QuestionResponseDto> questions; // List of questions with choices and selected choices

    @Data
    public static class AssessmentDto implements Serializable {
        private int assessmentId;
        private String assessmentName;
        private String assessmentDescription;
        private LocalDate endDate;
        private List<PotentialAttributeDto> potentialAttributes;

        @Data
        public static class PotentialAttributeDto implements Serializable {
            private int potentialAttributeId;
            private String attributeName;
            private List<QuestionResponseDto> questions;
        }

        @Data
        public static class QuestionResponseDto implements Serializable {
            private int assessmentQuestionId;
            private String assessmentQuestionDescription;
            private Set<ChoiceDto> choices;
            private ChoiceDto selectedChoice; // The choice selected by the user
        }

        @Data
        public static class ChoiceDto implements Serializable {
            private int choiceId;
            private int choiceValue;
            private String choiceName;
        }
    }
}


