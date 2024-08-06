// HiposInterventionController.java
package com.example.talent_man.controllers;

import com.example.talent_man.dto.*;
import com.example.talent_man.models.CriticalRolesStrategies;
import com.example.talent_man.models.HiposIntervention;
import com.example.talent_man.models.MappedSuccession;
import com.example.talent_man.services.HiposInterventionService;
import com.example.talent_man.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api/hipos")
public class HiposInterventionController {

    @Autowired
    private HiposInterventionService hiposInterventionService;

    @GetMapping
    public ResponseEntity<List<HiposIntervention>> getAllHiposInterventions() {
        List<HiposIntervention> hiposInterventions = hiposInterventionService.getAllHiposInterventions();
        return ResponseEntity.ok(hiposInterventions);
    }

    // HIPOS Development interventions

    @PostMapping("/interventions/create/{employeeId}")
    public ApiResponse<Object> nextPotentialRoles(@RequestBody List<HiposInterventionDTO> hiposInterventionDTO, @PathVariable int employeeId) {
        List<HiposIntervention> hiposIntervention = hiposInterventionService.createHiposIntervention(hiposInterventionDTO, employeeId);
        ApiResponse<Object> res = new ApiResponse<>();
        res.setMessage("HIPOS Potential Next Role added successfully");
        res.setStatus(200);
        res.setItem(hiposIntervention);
        return res;
    }



    // create next potential for the hipos

    @PostMapping("/potential_next_roles/create/{employeeId}")
    public ApiResponse<Object> createHiposIntervention(@RequestBody MappedSuccessionDto mappedSuccessionDto, @PathVariable int employeeId) {
        MappedSuccession mappedSuccession = hiposInterventionService.createHIPOsNextPotentialRoles(mappedSuccessionDto, employeeId);
        ApiResponse<Object> res = new ApiResponse<>();
        res.setMessage("HIPOS development intervention added successfully");
        res.setStatus(200);
        res.setItem(mappedSuccession);
        return res;
    }

    @GetMapping("/hipos/{managerId}")
    public ApiResponse<List<HIPOsFullDataDto>> getHIPOData(@PathVariable int managerId) {
        List<HIPOsFullDataDto> dto = hiposInterventionService.getAllHIPOsData(managerId);
        ApiResponse<List<HIPOsFullDataDto>> res = new ApiResponse<>();
        res.setMessage("Data retrieved successfully");
        res.setStatus(200);
        res.setItem(dto);
        return res;
    }

    @GetMapping("/interventions/{managerId}")
    public ApiResponse<List<HIPOSDataDto>> getHIPOsData(@PathVariable int managerId) {
        List<HIPOSDataDto> dto = hiposInterventionService.getHIPOSData(managerId);
        ApiResponse<List<HIPOSDataDto>> res = new ApiResponse<>();
        res.setMessage("Data retrieved successfully");
        res.setStatus(200);
        res.setItem(dto);
        return res;
    }



}
