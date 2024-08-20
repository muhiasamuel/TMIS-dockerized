package com.example.talent_man.dto.assessment;

import lombok.Data;

@Data
public class UserChoiceDto {
    private int questionId;
    private int choiceId;
    private String choiceName;
    private int choiceValue; // Assuming this holds the score or value
}