package com.example.talent_man.controllers;

import com.example.talent_man.dto.*;
import com.example.talent_man.models.HiposIntervention;
import com.example.talent_man.models.MVPsAssessment;
import com.example.talent_man.models.MVPsRetentionStrategies;
import com.example.talent_man.services.MVPsAssessmentService;
import com.example.talent_man.services.MVPsRetentionStrategiesService;
import com.example.talent_man.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

@RequestMapping("/v1/api/MVPs")
public class MVPsController {


    @Autowired
    MVPsAssessmentService assessmentService;

    @Autowired
    MVPsRetentionStrategiesService mvPsRetentionStrategiesService;
    @PostMapping("/retention-strategies/create/{employeeId}")
    public ApiResponse<Object> createMVPsRetentionStrategies(@RequestBody List<MVPsRetentionStrategiesDto> dto, @PathVariable int employeeId) {
        List<MVPsRetentionStrategies> strategies = mvPsRetentionStrategiesService.createMVPsStrategies(dto, employeeId);
        ApiResponse<Object> res = new ApiResponse<>();
        res.setMessage("MVPs Retention Strategies added successfully");
        res.setStatus(200);
        res.setItem(strategies);
        return res;
    }

    @PostMapping("/assessment/create/{employeeId}")
    public ApiResponse<Object> createMVPsAssessment(@RequestBody MVPsaAssessmentRequestDto dto, @PathVariable int employeeId){
        MVPsAssessment assessment = assessmentService.createMVPsAssessment(dto, employeeId);
        ApiResponse<Object> res = new ApiResponse<>();
        res.setMessage("MVPs Assessment added successfully");
        res.setStatus(200);
        res.setItem(assessment);
        return res;
    }

    @GetMapping("/employees/{managerId}")
    public ApiResponse<List<MVPsAssessmentResponseDto>> getMVPSAssessmentByManagerId(@PathVariable int managerId) {
        List<MVPsAssessmentResponseDto> assessmentResponseDto = assessmentService.getMVPSAssessmentByManagerId(managerId);
        ApiResponse<List<MVPsAssessmentResponseDto>> res = new ApiResponse<>();
        res.setMessage("Manager Employee Assessment for MVPs retrieved successfully");
        res.setStatus(200);
        res.setItem(assessmentResponseDto);
        return res;
    }

}
