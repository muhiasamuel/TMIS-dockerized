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


    @GetMapping("/getall/succession-plans")
    public ApiResponse<List<SuccessionPlanResponseDto>> getAllSuccessionPlans() {
        List<SuccessionPlanResponseDto> plans = successionPlanService.getSuccessionPlanDetails();
        ApiResponse<List<SuccessionPlanResponseDto>> res = new ApiResponse<>();

        if (plans.isEmpty()) {
            // Set the response to 404 Not Found if no plans are found
            res.setMessage("No succession plans found");
            res.setStatus(404);
            res.setItem(null);  // Optionally set item to null or leave it out
        } else {
            // Set the response to 200 OK if plans are found
            res.setMessage("Succession plans retrieved successfully");
            res.setStatus(200);
            res.setItem(plans);
        }

        return res;
    }


    @GetMapping("/get-by-id/{id}")
    public ApiResponse<List<SuccessionPlanResponseDto>> getSuccessionPlanById(@PathVariable int id) {
        List<SuccessionPlanResponseDto> plans = successionPlanService.getSuccessionPlanById(id);
        ApiResponse<List<SuccessionPlanResponseDto>> res = new ApiResponse<>();

        if (plans.isEmpty()) {
            // Set the response to 404 Not Found if no plans are found
            res.setMessage("No succession plans found");
            res.setStatus(404);
            res.setItem(null);  // Optionally set item to null or leave it out
        } else {
            // Set the response to 200 OK if plans are found
            res.setMessage("Succession plans retrieved successfully");
            res.setStatus(200);
            res.setItem(plans);
        }
        return res;
    }

    //check if user is mapped as a successor
    @GetMapping("/get-by-Successor-id/{userId}")
    public ApiResponse<List<SuccessionPlanResponseDto>> getSuccessionPlanByUserId(@PathVariable int userId) {
        List<SuccessionPlanResponseDto> plans = successionPlanService.getSuccessionPlanByUserId(userId);
        ApiResponse<List<SuccessionPlanResponseDto>> res = new ApiResponse<>();

        if (plans.isEmpty()) {
            // Set the response to 404 Not Found if no plans are found
            res.setMessage("This User Has Not Been Mapped For Any Role");
            res.setStatus(404);
            res.setItem(null);  // Optionally set item to null or leave it out
        } else {
            // Set the response to 200 OK if plans are found
            res.setMessage("Succession plans retrieved successfully");
            res.setStatus(200);
            res.setItem(plans);
        }
        return res;
    }

    @GetMapping("/get-by-position-id/{positionId}")
    public ApiResponse<List<SuccessionPlanResponseDto>> getSuccessionPlanByPosition(@PathVariable int positionId) {
        List<SuccessionPlanResponseDto> plans = successionPlanService.getSuccessionPlanByPosition(positionId);
        ApiResponse<List<SuccessionPlanResponseDto>> res = new ApiResponse<>();

        if (plans.isEmpty()) {
            // Set the response to 404 Not Found if no plans are found
            res.setMessage("This User Has Not Been Mapped For Any Role");
            res.setStatus(404);
            res.setItem(null);  // Optionally set item to null or leave it out
        } else {
            // Set the response to 200 OK if plans are found
            res.setMessage("Succession plans retrieved successfully");
            res.setStatus(200);
            res.setItem(plans);
        }
        return res;
    }

//check if Critical position has been mapped for succession
@GetMapping("/critical/role/succession/status")
public ApiResponse<List<CriticalRoleCheckForSuccessionDto>> getCriticalPositionStatus() {
    List<CriticalRoleCheckForSuccessionDto> roles = successionPlanService.checkPositionStatus();
    ApiResponse<List<CriticalRoleCheckForSuccessionDto>> res = new ApiResponse<>();

    if (roles.isEmpty()) {
        // Set the response to 404 Not Found if no plans are found
        res.setMessage("No Critical Role has been mapped for succession");
        res.setStatus(404);
        res.setItem(null);  // Optionally set item to null or leave it out
    } else {
        // Set the response to 200 OK if plans are found
        res.setMessage("Critical Roles Status on Succession Retrieved successfully");
        res.setStatus(200);
        res.setItem(roles);
    }
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