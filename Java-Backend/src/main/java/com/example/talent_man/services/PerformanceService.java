package com.example.talent_man.services;

import com.example.talent_man.dto.PerformanceRequestDto;
import com.example.talent_man.dto.PerformanceYearlyDto;
import com.example.talent_man.dto.UserPerformanceDTO;
import com.example.talent_man.models.Performance;
import com.example.talent_man.repos.PerformanceRepository;
import com.example.talent_man.utils.ApiResponse;
import org.springframework.http.ResponseEntity;

import java.io.InputStream;
import java.util.List;

public interface PerformanceService {
    Performance createPerformanceForUser(int userId, PerformanceRequestDto performanceRequestDto);

    List<PerformanceYearlyDto> getPerformancesByManagerId(int managerId);
    PerformanceYearlyDto getPerformanceByEmployeeId(int employeeId);
    List<PerformanceRepository.TalentInterface> getUserPerformancesByManagerId(int managerId);

    List<PerformanceRepository.TalentInterface> getAllUserPerformances();

    List<UserPerformanceDTO> getAllUserHIPOs(int managerId);

    List<UserPerformanceDTO> getAllUserHIPObyYear(int managerId, int year);

    ResponseEntity<ApiResponse<String>> addPerformanceForUser(String pf, List<PerformanceRequestDto> performanceDtos);
    ResponseEntity<ApiResponse<String>> importPerformancesFromExcel(InputStream inputStream);
    List<PerformanceRequestDto> getPerformancesByUser(String pf);
}
