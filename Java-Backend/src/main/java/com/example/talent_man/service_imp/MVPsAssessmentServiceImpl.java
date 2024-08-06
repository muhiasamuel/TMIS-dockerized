package com.example.talent_man.service_imp;

import com.example.talent_man.dto.MVPsAssessmentResponseDto;
import com.example.talent_man.dto.MVPsaAssessmentRequestDto;
import com.example.talent_man.dto.PerformanceDto;
import com.example.talent_man.dto.PerformanceYearlyDto;
import com.example.talent_man.dto.user.retentionStrategiesDto;
import com.example.talent_man.models.MVPsAssessment;
import com.example.talent_man.models.Performance;
import com.example.talent_man.models.user.User;
import com.example.talent_man.repos.MVPsAssessmentRepo;
import com.example.talent_man.repos.PerformanceRepository;
import com.example.talent_man.repos.user.UserRepo;
import com.example.talent_man.services.MVPsAssessmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MVPsAssessmentServiceImpl implements MVPsAssessmentService {

    @Autowired
    MVPsAssessmentRepo assessmentRepo;

    @Autowired
    UserRepo userRepo;

    @Override
    public MVPsAssessment createMVPsAssessment(MVPsaAssessmentRequestDto dto, int employeeId){
        // Find the user by userId
      try {
            User user = userRepo.findById(employeeId)
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + employeeId));

            // Create a new MVPsAssessment entity and set its fields
            MVPsAssessment mvPsAssessment = new MVPsAssessment();
            mvPsAssessment.setUser(user);
            mvPsAssessment.setRetentionAssessmentState(dto.getRetentionAssessmentState());
            mvPsAssessment.setCareerPriorities(dto.getCareerPriorities());
            mvPsAssessment.setMarketExposure(dto.getMarketExposure());
            mvPsAssessment.setImpactOfAttrition(dto.getImpactOfAttrition());

            // Save the performance
            return assessmentRepo.save(mvPsAssessment);
      }catch (Exception e){
          throw new RuntimeException("Error occurred while Creating MVPs assessment: " + e.getMessage(), e);
      }
    }

    @Override
    public List<MVPsAssessmentResponseDto> getMVPSAssessmentByManagerId(int managerId){
       try {
            List<MVPsAssessmentRepo.MVPSAssessmentInterface> mvpsAssessmentInterfaces = assessmentRepo.getMVPSAssessmentByManagerId(managerId);
           Map<Integer, MVPsAssessmentResponseDto> performanceMap = new HashMap<>();
           for (MVPsAssessmentRepo.MVPSAssessmentInterface assessmentInterface : mvpsAssessmentInterfaces) {
               int employeeId = assessmentInterface.getEmployeeId();
               String userFullName = assessmentInterface.getUserFullName();
               String careerPriority = assessmentInterface.getCareerPriority();
               String retentionState = assessmentInterface.getRetentionState();
               String impactOfAttrition = assessmentInterface.getImpactOfAttrition();
               String marketExposure = assessmentInterface.getMarketExposure();
               String strategies= assessmentInterface.getRetentionStrategy();
               if (performanceMap.containsKey(employeeId)) {
                   performanceMap.get(employeeId).getStrategies().add(new retentionStrategiesDto(strategies));
               } else {
                   MVPsAssessmentResponseDto mvPsAssessmentResponseDto = new MVPsAssessmentResponseDto(userFullName,employeeId,careerPriority,impactOfAttrition,marketExposure,retentionState, new ArrayList<>());
                   mvPsAssessmentResponseDto.getStrategies().add(new retentionStrategiesDto(strategies));
                   performanceMap.put(employeeId, mvPsAssessmentResponseDto);
               }
           }

           return new ArrayList<>(performanceMap.values());

       }catch (Exception e){
           throw new RuntimeException("Error occurred while getting Assessment for MVPs: " + e.getMessage(), e);
       }
    }
}
