package com.example.talent_man.dto;

import com.example.talent_man.models.CriticalRolesStrategies;
import com.example.talent_man.models.RolesAssessment;
import com.example.talent_man.models.user.User;
import lombok.Data;

import java.util.List;

@Data
public class RoleDetailsWithStrategiesDTO {
    private RolesAssessment role;
    private int addedBy;

    private List<CriticalRolesStrategies> strategies;
}
