package com.example.talent_man.dto.assessment;

import com.example.talent_man.dto.user.ManagerDto;
import com.example.talent_man.models.user.Manager;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
@Data@NoArgsConstructor
public class UserAssessmentDto implements Serializable {
    private int userId;
    private String userType;
    private String userFullName;
    private float averageScore;
    private float managerAverageScore;
    private Manager manager;
    private ManagerDto man;
    private List<AssQuestionDto> assQuestionDtoList = new ArrayList<>();
}
