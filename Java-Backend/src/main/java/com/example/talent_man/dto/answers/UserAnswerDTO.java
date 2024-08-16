package com.example.talent_man.dto.answers;

import lombok.Data;

import java.util.List;

@Data
public class UserAnswerDTO {
    private int assessmentId; // ID of the assessment
    private List<UserAnswer> answers; // List of user answers

    @Data
    public static class UserAnswer {
        private int assessmentQuestionId; // ID of the assessment question
        private int userId; // ID of the user being assessed
        private int choiceId; // ID of the choice selected by the user
        private boolean isSelfAssessed; // Indicates if this answer is from self-assessment
    }
}
