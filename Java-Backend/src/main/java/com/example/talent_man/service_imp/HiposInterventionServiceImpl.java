// HiposInterventionServiceImpl.java
package com.example.talent_man.service_imp;

import com.example.talent_man.dto.*;
import com.example.talent_man.models.CriticalRolesStrategies;
import com.example.talent_man.models.HiposIntervention;
import com.example.talent_man.models.MappedSuccession;
import com.example.talent_man.models.RolesAssessment;
import com.example.talent_man.models.user.Employee;
import com.example.talent_man.models.user.Manager;
import com.example.talent_man.models.user.User;
import com.example.talent_man.repos.HipoNextPotentialRolesRepo;
import com.example.talent_man.repos.HiposInterventionRepository;
import com.example.talent_man.repos.user.EmployeeRepo;
import com.example.talent_man.services.HiposInterventionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class HiposInterventionServiceImpl implements HiposInterventionService {

    @Autowired
    private HiposInterventionRepository hiposInterventionRepository;

    @Autowired
    private EmployeeRepo employeeRepository;

    @Autowired
    private HipoNextPotentialRolesRepo hipoNextPotentialRolesRepo;
    @Override
    public List<HiposIntervention> getAllHiposInterventions() {
        return hiposInterventionRepository.findAll();
    }


    @Override
    public List<HiposIntervention> createHiposIntervention(List<HiposInterventionDTO> hiposInterventionDTO, int employeeId) {


        try {
            Employee employee = employeeRepository.findById(employeeId)
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + employeeId));

            List<HiposIntervention> interventions = new ArrayList<>();

            for (HiposInterventionDTO interventionDTO : hiposInterventionDTO) {
                HiposIntervention hiposIntervention = new HiposIntervention();
                hiposIntervention.setDevelopmentInterventions(interventionDTO.getDevelopmentInterventions());
                hiposIntervention.setHowToAchieveInterventions(interventionDTO.getHowToAchieveInterventions());
                hiposIntervention.setEmployee(employee);
                interventions.add(hiposIntervention);
            }

            return hiposInterventionRepository.saveAll(interventions);
        } catch (Exception e) {
            throw new IllegalArgumentException("An error occurred when saving HIPOs Intervention: " + e.getMessage());
        }
    }

    @Override
    public MappedSuccession createHIPOsNextPotentialRoles(MappedSuccessionDto mappedSuccessionDto, int employeeId){
        try {
            Employee employee = employeeRepository.findById(employeeId)
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + employeeId));

            MappedSuccession mappedSuccession = new MappedSuccession();
            mappedSuccession.setPotentialNextRole(mappedSuccessionDto.getPotentialNextRole());
            mappedSuccession.setEmployee(employee);


            return hipoNextPotentialRolesRepo.save(mappedSuccession);
        } catch (Exception e) {
            throw new IllegalArgumentException("An error occurred when saving HIPOs next role: " + e.getMessage());
        }
    }


    @Override
    public List<HIPOsFullDataDto> getAllHIPOsData(int managerId) {
        List<HIPOsFullDataDto> dtos = hipoNextPotentialRolesRepo.getAllHIPOsData(managerId);
        for (HIPOsFullDataDto dto : dtos) {
        }
        return dtos;
    }

    @Override
    public List<HIPOSDataDto> getHIPOSData(int managerId) {

           try {
                List<HIPOSDataDto> hiposDataList = new ArrayList<>();
                List<HiposInterventionRepository.HIPOSInterventionInterface> hiposInterventions = hiposInterventionRepository.getHIPOSInterventionsByManagerId(managerId);

                // Group interventions by userId
                Map<Integer, List<HiposInterventionRepository.HIPOSInterventionInterface>> interventionsMap = hiposInterventions.stream()
                        .collect(Collectors.groupingBy(HiposInterventionRepository.HIPOSInterventionInterface::getUserId));

                // Convert grouped interventions to DTOs
                interventionsMap.forEach((userId, interventions) -> {
                    HIPOSDataDto hiposDataDto = new HIPOSDataDto();
                    hiposDataDto.setUserId(userId);
                    hiposDataDto.setUserFullName(interventions.get(0).getUserFullName());
                    hiposDataDto.setNextPotentialRole(interventions.get(0).getPotentialNextRoles());

                    List<InterventionDto> interventionDtoList = new ArrayList<>();
                    for (HiposInterventionRepository.HIPOSInterventionInterface intervention : interventions) {
                        InterventionDto interventionDto = new InterventionDto();
                        interventionDto.setDevelopmentInterventions(intervention.getDevelopmentInterventions());
                        interventionDto.setHowToAchieve(intervention.getHowToAchieve());
                        interventionDtoList.add(interventionDto);
                    }
                    hiposDataDto.setInterventions(interventionDtoList);
                    hiposDataList.add(hiposDataDto);
                });

                return hiposDataList;
            }catch (Exception e){
               throw new RuntimeException("Error occurred while getting employee interventions: " + e.getMessage(), e);
           }
        }

}


