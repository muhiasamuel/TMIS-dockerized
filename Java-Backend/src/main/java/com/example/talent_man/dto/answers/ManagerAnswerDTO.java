package com.example.talent_man.dto.answers;


import lombok.Data;

import java.util.List;

@Data
public class ManagerAnswerDTO {
    private int managerId; // ID of the manager submitting the answers
    private List<ManagerAnswer> answers; // List of answers submitted by the manager
    private int assessmentId; // ID of the assessment

    @Data
    public static class ManagerAnswer {
        private int assessmentQuestionId; // ID of the assessment question
        private int userId; // ID of the user being assessed
        private int choiceId; // ID of the choice selected by the manager
        private boolean isManagerAssessed; // Indicates if this answer is from manager assessment
    }
}
