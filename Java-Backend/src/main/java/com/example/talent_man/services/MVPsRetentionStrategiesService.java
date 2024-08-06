package com.example.talent_man.services;

import com.example.talent_man.dto.HiposInterventionDTO;
import com.example.talent_man.dto.MVPsRetentionStrategiesDto;
import com.example.talent_man.models.HiposIntervention;
import com.example.talent_man.models.MVPsRetentionStrategies;

import java.util.List;

public interface MVPsRetentionStrategiesService {
    List<MVPsRetentionStrategies> createMVPsStrategies(List<MVPsRetentionStrategiesDto> dto, int employeeId);

}
