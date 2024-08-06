package com.example.talent_man.controllers;


import com.example.talent_man.models.RolesAssessment;
import com.example.talent_man.models.SkillsAsssessment;
import com.example.talent_man.models.user.Manager;
import com.example.talent_man.services.SkillAssessmentService;
import com.example.talent_man.services.UserService;
import com.example.talent_man.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/v1/api/criticalSkills")
public class CriticalSkillAssessmentController {

    @Autowired
    SkillAssessmentService skillAssessmentService;
    @Autowired
    UserService service;
    @GetMapping("/getAssessmentSkillById")
    public ApiResponse<SkillsAsssessment> getAddedSkills(@RequestParam Long skillId) {
        ApiResponse<SkillsAsssessment> res = new ApiResponse<>();
        try {
            SkillsAsssessment skill = skillAssessmentService.getSkillById(skillId);
            res.setItem(skill);
            res.setStatus(200);
            res.setMessage("Success");
        } catch (RuntimeException e) {
            res.setStatus(404);
            res.setMessage(e.getMessage());
        }
        return res;
    }

    @GetMapping("/getAddedSkills")
    public ApiResponse getManagerAddedSkills(@RequestParam int managerId){

        try{
            if (managerId == 0){
                return new ApiResponse(300, "Enter a valid id");
            }else {
                ApiResponse<Object> newSet = new ApiResponse<>(200,  " successfully");
                Manager manager = service.getManagerById(managerId);
                newSet.setItem(manager.getSkillsAssessments());
                return newSet;
            }
        }catch (Exception e){
            return new ApiResponse(500, e.getMessage());
        }
    }

    @PutMapping("/{skillId}/{userId}")
    public ApiResponse<SkillsAsssessment> updateSkill(@PathVariable Long skillId, @PathVariable int userId, @RequestBody SkillsAsssessment updatedSkill) {
        ApiResponse<SkillsAsssessment> response = new ApiResponse<>();
        try {
            SkillsAsssessment skill = skillAssessmentService.updateSkill(skillId,userId, updatedSkill);
            response.setItem(skill);
            response.setStatus(200);
            response.setMessage("Skill updated successfully");
        } catch (RuntimeException e) {
            response.setStatus(404);
            response.setMessage(e.getMessage());
        }
        return response;
    }
}
