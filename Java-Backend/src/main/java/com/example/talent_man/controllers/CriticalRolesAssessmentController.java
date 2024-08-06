package com.example.talent_man.controllers;

import com.example.talent_man.dto.CriticalRolesAssessmentDto;
import com.example.talent_man.dto.RoleDetailsWithStrategiesDTO;
import com.example.talent_man.dto.RolesStrategiesDto;
import com.example.talent_man.models.CriticalRolesStrategies;
import com.example.talent_man.models.RolesAssessment;
import com.example.talent_man.models.SkillsAsssessment;
import com.example.talent_man.models.user.Manager;
import com.example.talent_man.services.CriticalRolesAssessmentService;
import com.example.talent_man.services.UserService;
import com.example.talent_man.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api/criticalRoles")
public class CriticalRolesAssessmentController {

    @Autowired
    CriticalRolesAssessmentService rolesAssessmentService;

    @Autowired
    UserService user;
    @PostMapping(value = "/create/{ManagerId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<Object> createRolesAssessment(@PathVariable int ManagerId, @RequestBody CriticalRolesAssessmentDto requestDto) {

        RolesAssessment item = rolesAssessmentService.createRolesAssessment(requestDto, ManagerId);
        ApiResponse<Object> res = new ApiResponse<>();
        res.setStatus(200);
        res.setMessage("Critical Role Added Successfully");
        res.setItem(item);
        return res;
    }

    //Add strategies for assessed critical roles
    @PostMapping("/{rolesAssessmentId}/add-strategy/{ManagerId}")
    public ApiResponse<Object> addStrategiesToRolesAssessment(@PathVariable Long rolesAssessmentId, @PathVariable int ManagerId, @RequestBody List<RolesStrategiesDto> strategiesDto) {
        List  <CriticalRolesStrategies> strategy = rolesAssessmentService.addStrategiesToRolesAssessment(rolesAssessmentId, ManagerId, strategiesDto);
        ApiResponse<Object> res = new ApiResponse<>();
        res.setMessage("Strategy added Successfully");
        res.setStatus(200);
        res.setItem(strategy);
        return  res;
    }

    //get add manager added critical roles
    @GetMapping("/getAllCriticalRoles")
    public ApiResponse getManagerAddedRoles(@RequestParam int managerId){

        try{
            if (managerId == 0){
                return new ApiResponse(300, "Enter a valid id");
            }else {
                ApiResponse<Object> newSet = new ApiResponse<>(200,  " successfully");
                Manager manager = user.getManagerById(managerId);
                newSet.setItem(manager.getRolesAssessments());
                return newSet;
            }
        }catch (Exception e){
            return new ApiResponse(500, e.getMessage());
        }
    }

    @GetMapping("/getAssessmentRoleById")
    public ApiResponse<RoleDetailsWithStrategiesDTO> getAddedSkills(@RequestParam Long roleId) {
        ApiResponse<RoleDetailsWithStrategiesDTO> res = new ApiResponse<>();
        try {
            RoleDetailsWithStrategiesDTO role = rolesAssessmentService.getRoleById(roleId);
            res.setItem(role);
            res.setStatus(200);
            res.setMessage("Success");
        } catch (RuntimeException e) {
            res.setStatus(404);
            res.setMessage(e.getMessage());
        }
        return res;
    }

    @PutMapping(value = "/edit/{criticalRoleId}/{ManagerId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<Object> editRolesAssessment(@PathVariable int ManagerId, @PathVariable Long criticalRoleId,  @RequestBody CriticalRolesAssessmentDto requestDto) {

        RolesAssessment item = rolesAssessmentService.editRolesAssessment(requestDto, ManagerId, criticalRoleId);
        ApiResponse<Object> res = new ApiResponse<>();
        res.setStatus(200);
        res.setMessage("Critical Role Edited Successfully");
        res.setItem(item);
        return res;
    }


}
