package com.example.talent_man.services;

import com.example.talent_man.dto.CriticalRolesAssessmentDto;
import com.example.talent_man.dto.RoleDetailsWithStrategiesDTO;
import com.example.talent_man.dto.RolesStrategiesDto;
import com.example.talent_man.models.CriticalRolesStrategies;
import com.example.talent_man.models.RolesAssessment;

import java.util.List;

public interface CriticalRolesAssessmentService {
    RolesAssessment createRolesAssessment(CriticalRolesAssessmentDto requestDto, int ManagerId);

    List<CriticalRolesStrategies> addStrategiesToRolesAssessment(Long rolesAssessmentId, int managerId, List<RolesStrategiesDto> strategiesDto);

    RoleDetailsWithStrategiesDTO getRoleById(Long roleId);

    RolesAssessment editRolesAssessment(CriticalRolesAssessmentDto requestDto, int userId, Long criticalRoleId);
}