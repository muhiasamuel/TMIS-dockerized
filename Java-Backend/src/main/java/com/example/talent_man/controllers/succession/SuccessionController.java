package com.example.talent_man.controllers.succession;

import com.example.talent_man.dto.succession.SuccessionPlanDto;
import com.example.talent_man.services.SuccessionPlanService;
import com.example.talent_man.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api/succession-plans")
public class SuccessionController {
    @Autowired
    private SuccessionPlanService successionPlanService;

    @PostMapping
    public ApiResponse<Object> createSuccessionPlan(@RequestBody SuccessionPlanDto successionPlanDto) {
        SuccessionPlanDto createdPlan = successionPlanService.createSuccessionPlan(successionPlanDto);
        ApiResponse<Object> res = new ApiResponse<>();
        res.setMessage("Succession plan added successfully");
        res.setStatus(200);
        res.setItem(createdPlan);
        return res;
    }

    @GetMapping
    public ApiResponse<List<SuccessionPlanDto>> getAllSuccessionPlans() {
        List<SuccessionPlanDto> plans = successionPlanService.getAllSuccessionPlans();
        ApiResponse res = new ApiResponse<>();
        res.setMessage("Succession plans Retrieved successfully");
        res.setStatus(200);
        res.setItem(plans);
        return res;
    }

    @GetMapping("/{id}")
    public ApiResponse<SuccessionPlanDto> getSuccessionPlanById(@PathVariable int id) {
        SuccessionPlanDto plan = successionPlanService.getSuccessionPlanById(id);
        ApiResponse res = new ApiResponse<>();
        res.setMessage("Succession plan Record retrieved successfully");
        res.setStatus(200);
        res.setItem(plan);
        return res;
    }

    @PutMapping("/{id}")
    public ApiResponse<SuccessionPlanDto> updateSuccessionPlan(@PathVariable int id, @RequestBody SuccessionPlanDto successionPlanDto) {
        SuccessionPlanDto updatedPlan = successionPlanService.updateSuccessionPlan(id, successionPlanDto);
        ApiResponse res = new ApiResponse<>();
        res.setMessage("Succession plan Updated successfully");
        res.setStatus(200);
        res.setItem(updatedPlan);
        return res;
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Object> deleteSuccessionPlan(@PathVariable int id) {
        successionPlanService.deleteSuccessionPlan(id);
        ApiResponse<Object> res = new ApiResponse<>();
        res.setMessage("Succession plan Deleted successfully");
        res.setStatus(200);
        return res;
    }
}