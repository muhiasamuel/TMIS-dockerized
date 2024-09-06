package com.example.talent_man.services;

import com.example.talent_man.controllers.succession.SuccessionPlanResponseDto;
import com.example.talent_man.dto.succession.SuccessionPlanDto;

import java.util.List;

public interface SuccessionPlanService {
    SuccessionPlanDto createSuccessionPlan(SuccessionPlanDto successionPlanDto);
    List<SuccessionPlanDto> getAllSuccessionPlans();
    SuccessionPlanDto getSuccessionPlanById(int id);
    SuccessionPlanDto updateSuccessionPlan(int id, SuccessionPlanDto successionPlanDto);
    void deleteSuccessionPlan(int id);

    List<SuccessionPlanResponseDto> getSuccessionPlanDetails();

}
