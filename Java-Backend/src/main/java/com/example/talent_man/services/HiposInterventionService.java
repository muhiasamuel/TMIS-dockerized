// HiposInterventionService.java
package com.example.talent_man.services;

import com.example.talent_man.dto.*;
import com.example.talent_man.models.CriticalRolesStrategies;
import com.example.talent_man.models.HiposIntervention;
import com.example.talent_man.models.MappedSuccession;

import java.util.List;

public interface HiposInterventionService {
    List<HiposIntervention> getAllHiposInterventions();
    List<HiposIntervention> createHiposIntervention(List<HiposInterventionDTO> hiposInterventionDTO, int employeeId);

    MappedSuccession createHIPOsNextPotentialRoles(MappedSuccessionDto mappedSuccessionDto, int employeeId);

    List<HIPOsFullDataDto> getAllHIPOsData(int managerId);
    List<HIPOSDataDto> getHIPOSData(int managerId);


}
