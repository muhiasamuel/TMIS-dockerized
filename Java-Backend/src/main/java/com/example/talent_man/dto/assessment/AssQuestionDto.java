package com.example.talent_man.dto.assessment;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class AssQuestionDto implements Serializable {
    private int questionId;
    private String questionDescription;
    private ChoiceDto employeeChoice = new ChoiceDto(0,0,"0");
    private ChoiceDto managerChoice = new ChoiceDto(0,0,"0");
    private List<ChoiceDto> choices = new ArrayList<>();
}
