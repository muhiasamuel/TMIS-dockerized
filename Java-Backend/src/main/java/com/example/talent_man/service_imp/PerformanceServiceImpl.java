package com.example.talent_man.service_imp;

import com.example.talent_man.dto.*;
import com.example.talent_man.models.Performance;
import com.example.talent_man.models.user.User;
import com.example.talent_man.repos.PerformanceRepository;
import com.example.talent_man.repos.user.UserRepo;
import com.example.talent_man.services.PerformanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PerformanceServiceImpl implements PerformanceService {

    @Autowired
    private UserRepo userRepository;

    @Autowired
    private PerformanceRepository performanceRepository;

    @Override
    public Performance createPerformanceForUser(int userId, PerformanceRequestDto performanceRequestDto) {
        // Find the user by userId
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // Convert YearDto to java.time.Year
        Year year = toYear(performanceRequestDto.getYear());

        // Create a new Performance entity and set its fields
        Performance performance = new Performance();
        performance.setUser(user);
        performance.setYear(year);
        performance.setQuarter(performanceRequestDto.getQuarter());
        performance.setPerformanceMetric(performanceRequestDto.getPerformanceMetric());

        // Save the performance
        return performanceRepository.save(performance);
    }

    // Method to convert YearDto to java.time.Year
    private Year toYear(YearDto yearDto) {
        return Year.of(yearDto.getValue());
    }

    @Override
    public List<PerformanceRepository.TalentInterface> getUserPerformancesByManagerId(int managerId) {
        try {
            return performanceRepository.getUserPerformancesByManagerId(managerId);
        }catch (Exception e){
            throw new RuntimeException("Error occurred while getting employee talent mapping: " + e.getMessage(), e);
        }
    }

    @Override
    public List<PerformanceRepository.TalentInterface> getAllUserPerformances() {
        try {
            return performanceRepository.getAllUserPerformances();
        }catch (Exception e){
            throw new RuntimeException("Error occurred while getting employee talent mapping: " + e.getMessage(), e);
        }
    }


    @Override
    public List<PerformanceYearlyDto> getPerformancesByManagerId(int managerId) {
        List<PerformanceRepository.performanceInterface> performanceInterfaces = performanceRepository.getAllUsersPerformances(managerId);
        Map<Integer, PerformanceYearlyDto> performanceMap = new HashMap<>();

        for (PerformanceRepository.performanceInterface performanceInterface : performanceInterfaces) {
            int userId = performanceInterface.getUserId();
            String userFullName = performanceInterface.getUserFullName();
            double performanceRating = performanceInterface.getPerformanceRating();
            int performanceYear = performanceInterface.getPerformanceYear();
            int yearsCount = performanceInterface.getYearsCount();
            double totalPerformanceRating = performanceInterface.getAveragePerformanceLastThreeYears();
            if (performanceMap.containsKey(userId)) {
                performanceMap.get(userId).getPerformances().add(new PerformanceDto(performanceRating, performanceYear));
            } else {
                PerformanceYearlyDto performanceYearlyDto = new PerformanceYearlyDto(userId, userFullName,yearsCount,totalPerformanceRating, new ArrayList<>());
                performanceYearlyDto.getPerformances().add(new PerformanceDto(performanceRating, performanceYear));
                performanceMap.put(userId, performanceYearlyDto);
            }
        }

        return new ArrayList<>(performanceMap.values());
    }



    @Override
    public PerformanceYearlyDto getPerformanceByEmployeeId(int employeeId) {
        List<PerformanceRepository.performanceInterface> performanceInterfaces = performanceRepository.getPerformanceByEmployeeId(employeeId);
        PerformanceYearlyDto performanceYearlyDto = null;

        for (PerformanceRepository.performanceInterface performanceInterface : performanceInterfaces) {
            int userId = performanceInterface.getUserId();
            String userFullName = performanceInterface.getUserFullName();
            double performanceRating = performanceInterface.getPerformanceRating();
            int performanceYear = performanceInterface.getPerformanceYear();
            int yearsCount = performanceInterface.getYearsCount();
            double totalPerformanceRating = performanceInterface.getAveragePerformanceLastThreeYears();

            if (performanceYearlyDto == null) {
                performanceYearlyDto = new PerformanceYearlyDto(userId, userFullName, yearsCount, totalPerformanceRating, new ArrayList<>());
            }

            performanceYearlyDto.getPerformances().add(new PerformanceDto(performanceRating, performanceYear));
        }

        return performanceYearlyDto;
    }

    @Override
    public List<UserPerformanceDTO> getAllUserHIPOs(int managerId) {
        try {
            List<PerformanceRepository.UserPerformanceData> talentInterfaces = performanceRepository.getAllUserHIPOs(managerId);
            List<UserPerformanceDTO> dtos = new ArrayList<>();
            UserPerformanceDTO data = new UserPerformanceDTO();

            double totalManAssessmentAvg = 0.0; // Add this line to store the total
            for (PerformanceRepository.UserPerformanceData talentInterface : talentInterfaces) {
                UserPerformanceDTO dto = mapToDTO(talentInterface);
                dtos.add(dto);
            }
            return dtos;
        } catch (Exception e){
            throw new RuntimeException("Error occurred while getting employee talent mapping: " + e.getMessage(), e);
        }
    }
    public List<UserPerformanceDTO> getAllUserHIPObyYear(int managerId, int year) {
        try {
            List<PerformanceRepository.UserPerformanceData> talentInterfaces = performanceRepository.getAllUserHIPOsByYear(managerId, year);
            List<UserPerformanceDTO> dtos = new ArrayList<>();
            UserPerformanceDTO data = new UserPerformanceDTO();

            double totalManAssessmentAvg = 0.0; // Add this line to store the total
            for (PerformanceRepository.UserPerformanceData talentInterface : talentInterfaces) {
                UserPerformanceDTO dto = mapToDTO(talentInterface);
                dtos.add(dto);
            }
            return dtos;
        } catch (Exception e){
            throw new RuntimeException("Error occurred while getting employee talent mapping: " + e.getMessage(), e);
        }
    }

    private UserPerformanceDTO mapToDTO(PerformanceRepository.UserPerformanceData talentInterface) {
        UserPerformanceDTO dto = new UserPerformanceDTO();
        dto.setUserId(talentInterface.getUserId());
        dto.setUsername(talentInterface.getUsername());
        dto.setAveragePerformance(talentInterface.getAveragePerformance());
        dto.setUserAssessmentAvg(talentInterface.getUserAssessmentAvg());
        dto.setPerformanceYear(talentInterface.getPerformanceYear());
        dto.setPotentialAttributeName(talentInterface.getPotentialAttribute());
        dto.setAveragePotential(talentInterface.getPotentialRating());
        dto.setUserFullName(talentInterface.getUserFullName());
        // Calculate and set talentRating and potentialRating if needed
        //dto.setTalentRating(talentInterface.getPotentialRating());
        dto.setPotentialRating(calculatePotentialRating(talentInterface));
        dto.setManAssessmentAvg(talentInterface.getManAssessmentAvg());
        dto.setTalentRating( calculateTalentRating(talentInterface));
        dto.setPerformanceRating(calculatePerformanceRating(talentInterface));
        System.out.println("H   ELOO" + talentInterface.getPerformanceYear() + talentInterface.getAveragePerformance());

        return dto;
    }

    private Integer calculatePerformanceRating(PerformanceRepository.UserPerformanceData talentInterface) {
        // Your logic to calculate talent rating based on performance metrics
        // Example:
        double averagePerformance = talentInterface.getAveragePerformance();
        if (averagePerformance >= 1 && averagePerformance <= 2.9) {
            return 3;
        } else if (averagePerformance >=3 && averagePerformance <= 4) {
            return 2;
        } else {
            return 1;
        }
    }

    private String calculatePotentialRating(PerformanceRepository.UserPerformanceData talentInterface) {

        Double potential = talentInterface.getPotentialRating();
        // Determine potential rating based on average manAssessmentAvg
        if (potential >= 1 && potential <= 1.9) {
            return "C";
        } else if (potential >= 2 && potential < 4) {
            return "B";
        } else if((potential >= 4 && potential <= 6)){
            return "A";
        }
        else {
            return "low";
        }
    }

    private String calculateTalentRating(PerformanceRepository.UserPerformanceData talentInterface) {
        // Your logic to calculate potential rating based on assessment metrics
        // Example:
        Double potential = talentInterface.getPotentialRating();
        Double perfomance = talentInterface.getAveragePerformance();

        if ((perfomance >= 4.1 && perfomance <= 5) && (potential >= 4 && potential <= 6)) {

            return "A1";
        } else if ((perfomance >= 4 && perfomance <= 5) && (potential >= 2 && potential < 4)) {
            return "B1";
        }  else if ((perfomance >= 4 && perfomance <= 5) && (potential >= 1 && potential <= 1.9)) {
            return "C1";
        }else if ((perfomance >= 2.9 && perfomance <= 3.9) && (potential >= 4 && potential <= 6)) {
            return "A2";
        }else if ((perfomance > 2.9 && perfomance <= 3.9) && (potential >= 2 && potential <= 3.9)) {
            return "B2";
        }else if ((perfomance >= 2.9 && perfomance <= 3.9) && (potential >= 1 && potential <= 1.9)) {
            return "C2";
        }else if ((perfomance >= 1 && perfomance < 2.9) && (potential >= 4 && potential <= 6)) {
            return "A3";
        }
        else if ((perfomance >= 1 && perfomance < 2.9) && (potential >= 2 && potential <= 3.9)) {
            return "B3";
        }
        else if ((perfomance >= 1 && perfomance < 2.9) && (potential >= 1 && potential <= 1.9)) {
            return "C3";
        }else {
            return "Low";
        }
    }

}
