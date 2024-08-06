package com.example.talent_man.dto.assessment;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class UserDoneAssessmentDto implements Serializable {
    private int id;
    private String attName;
    private String assName;
    private String assDescription;
    private UserAssessmentDto user;
    private List<AssQuestionDto> assQuestions = new ArrayList<>();
}
