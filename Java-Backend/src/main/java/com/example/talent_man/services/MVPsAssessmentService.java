package com.example.talent_man.services;

import com.example.talent_man.dto.MVPsAssessmentResponseDto;
import com.example.talent_man.dto.MVPsaAssessmentRequestDto;
import com.example.talent_man.dto.PerformanceRequestDto;
import com.example.talent_man.dto.PerformanceYearlyDto;
import com.example.talent_man.models.MVPsAssessment;
import com.example.talent_man.models.Performance;
import org.springframework.stereotype.Service;

import java.util.List;


public interface MVPsAssessmentService {
    MVPsAssessment createMVPsAssessment(MVPsaAssessmentRequestDto dto, int employeeId);

    List<MVPsAssessmentResponseDto> getMVPSAssessmentByManagerId(int managerId);

}
