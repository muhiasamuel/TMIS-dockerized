package com.example.talent_man.dto.succession;

import lombok.Data;

import java.util.List;

@Data
public class ReadyUserDto {
    private int id;
    private int userId;
    private String readinessLevel;
    private List<ProposedInterventionDto> proposedInterventions;
    private List<SuccessorDevelopmentNeedDto> developmentNeeds;


}

