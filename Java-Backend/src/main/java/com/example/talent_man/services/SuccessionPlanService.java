package com.example.talent_man.services;

import com.example.talent_man.controllers.succession.CriticalRoleCheckForSuccessionDto;
import com.example.talent_man.controllers.succession.SuccessionPlanResponseDto;
import com.example.talent_man.dto.succession.SuccessionPlanDto;

import java.util.List;

public interface SuccessionPlanService {
    SuccessionPlanDto createSuccessionPlan(SuccessionPlanDto successionPlanDto);
    List<SuccessionPlanDto> getAllSuccessionPlans();
    List<SuccessionPlanResponseDto> getSuccessionPlanById(int id);

    List<SuccessionPlanResponseDto> getSuccessionPlanByUserId(int userId);

    List<SuccessionPlanResponseDto> getSuccessionPlanByPosition(int positionId);
    SuccessionPlanDto updateSuccessionPlan(int id, SuccessionPlanDto successionPlanDto);
    void deleteSuccessionPlan(int id);

    List<CriticalRoleCheckForSuccessionDto> checkPositionStatus();

    List<SuccessionPlanResponseDto> getSuccessionPlanDetails();

}
