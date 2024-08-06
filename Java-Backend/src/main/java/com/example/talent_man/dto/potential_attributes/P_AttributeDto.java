package com.example.talent_man.dto.potential_attributes;

import com.example.talent_man.dto.assessment.UserDoneAssessmentDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class P_AttributeDto implements Serializable {
    private int pAttId;
    private String pAttName;
    private List<UserDoneAssessmentDto> assessments;
}
