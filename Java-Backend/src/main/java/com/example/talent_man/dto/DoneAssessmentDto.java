package com.example.talent_man.dto;

import com.example.talent_man.dto.assessment.AssQuestionDto;
import com.example.talent_man.dto.assessment.UserAssessmentDto;
import com.example.talent_man.models.user.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class DoneAssessmentDto implements Serializable {
    private int id;
    private String potentialAttributeName;
    private String assessmentName;
    private String assessmentDescription;
    private List<UserAssessmentDto> doneBy;
    private List<UserAssessmentDto> notDoneBy;
    private List<AssQuestionDto> questions;

}
