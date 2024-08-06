package com.example.talent_man.controllers.user;

import com.example.talent_man.dto.Attributes;
import com.example.talent_man.dto.UserDto;
import com.example.talent_man.models.*;
import com.example.talent_man.models.user.Employee;
import com.example.talent_man.models.user.Manager;
import com.example.talent_man.models.user.User;
import com.example.talent_man.repos.PotentialAttributeRepo;
import com.example.talent_man.repos.RoleRepository;
import com.example.talent_man.service_imp.DepServiceImp;
import com.example.talent_man.service_imp.PotentialAttributeServiceImp;
import com.example.talent_man.service_imp.UserServiceImp;
import com.example.talent_man.utils.ApiResponse;
import com.example.talent_man.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;


@RestController
@RequestMapping("/v1/api")
public class UserController {
    @Autowired
    private UserServiceImp service;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    PotentialAttributeServiceImp repo;


    @Autowired
    private DepServiceImp depServiceImp;



    @PostMapping("/addPotential")
    public ApiResponse<Manager> addPotential(@RequestParam int managerId, @RequestBody Attributes attribute){
        try{
            if (managerId == 0){
                return new ApiResponse<>(300, "Please enter a valid id");
            } else if (attribute.getPotentialAttributeName() == null || attribute.getPotentialAttributeName().equals("")) {
                return new ApiResponse<>(300, "Attribute requires a name");
            } else if (attribute.getPotentialAttributeDescription() == null || attribute.getPotentialAttributeDescription().equals("")) {
                return new ApiResponse<>(300, "Attribute requires a description");
            }else{
                Manager manger = service.getManagerById(managerId);
                PotentialAttribute att = new PotentialAttribute();
                att.setPotentialAttributeName(attribute.getPotentialAttributeName());
                att.setPotentialAttributeDescription(attribute.getPotentialAttributeDescription());
                att.setCreatedAt(attribute.getCreatedAt());


                if (manger.getPotentialAttributeSet() == null || manger.getPotentialAttributeSet().isEmpty()){
                    Set<PotentialAttribute> atts = new HashSet<>();
                    atts.add(att);
                    manger.setPotentialAttributeSet(atts);
                }else{
                    manger.getPotentialAttributeSet().add(att);
                }
                Manager manager = (Manager) service.addUser(manger);
                ApiResponse<Manager> res = new ApiResponse<>(200, "successful");
                res.setItem(manager);
                return res;

            }
        }catch (Exception e){
            return new ApiResponse<>(500, e.getMessage());
        }
    }
    @PostMapping("/addAttributeList")
    public ApiResponse<Set<PotentialAttribute>> addAttributeList(@RequestParam int managerId, @RequestBody List<Attributes> attributes) {
        ApiResponse<Set<PotentialAttribute>> response = new ApiResponse<>(200, "Successful");
        try {
            if (managerId == 0) {
                response.setStatus(300);
                response.setMessage("Enter a valid id");
                return response;
            }

            Manager manager = service.getManagerById(managerId);
            Set<PotentialAttribute> existingAttributes = manager.getPotentialAttributeSet();
            Set<PotentialAttribute> newAttributes = new HashSet<>();

            for (Attributes attrDto : attributes) {
                boolean exists = existingAttributes.stream()
                        .anyMatch(existingAttr -> existingAttr.getPotentialAttributeName().equals(attrDto.getPotentialAttributeName()));

                if (!exists) {
                    PotentialAttribute potentialAttribute = new PotentialAttribute();
                    potentialAttribute.setPotentialAttributeName(attrDto.getPotentialAttributeName());
                    potentialAttribute.setPotentialAttributeDescription(attrDto.getPotentialAttributeDescription());
                    potentialAttribute.setCreatedAt(attrDto.getCreatedAt() != null ? attrDto.getCreatedAt() : LocalDateTime.now());
                    potentialAttribute.setManager(manager);  // Set the manager reference
                    newAttributes.add(potentialAttribute);
                }
            }

            if (!newAttributes.isEmpty()) {
                List<PotentialAttribute> savedAttributes = repo.saveAllAttributes(newAttributes);  // Save all new attributes
                existingAttributes.addAll(savedAttributes);  // Add saved attributes to the existing set
            }

            response.setItem(existingAttributes);  // Return the updated set of attributes
            return response;

        } catch (Exception error) {
            response.setStatus(500);
            response.setMessage(error.getMessage());
            return response;
        }
    }

    @PostMapping("/addSkill")
    public ApiResponse addSkill(@RequestParam int managerId, @RequestBody SkillsAsssessment skillAssessment){
        try{

            if(managerId == 0){
                return new ApiResponse(300, "Manager doesn't exist.");
            }else if(skillAssessment.getSkillName() == null || skillAssessment.getSkillName().equals("")){
                return new ApiResponse(300, "Skill should have a name");
            }else if(skillAssessment.getSkillDescription() == null || skillAssessment.getSkillDescription().equals("")){
                return new ApiResponse(300, "skill should have a description of what it is about.");
            }else if(skillAssessment.getBusinessPriority().equals("")){
                return new ApiResponse(300, "What is the business priority of the skill?");
            }else if(skillAssessment.getCurrentSkillState().equals("")){
                return new ApiResponse(300, "What is the current market state of the skill?");
            }else if(skillAssessment.getScarcityParameter().equals("")){
                return new ApiResponse(300, "What is the scarcity parameter of the skill?");
            }else if(skillAssessment.getMarketFluidity().equals("")){
                return new ApiResponse(300, "What is the market fluidity of the skill?");
            }else if(skillAssessment.getDevelopmentCostAndTimeCommitment().equals("")){
                return new ApiResponse(300, "What is skills current development cost and time commitment?");
            }else if(skillAssessment.getFutureMarketAndTechRelevance().equals("")){
                return new ApiResponse(300, "What is skills future market and tech relevance?");
            }else if(skillAssessment.getAverageRating().equals("")){
                return new ApiResponse(300, "What is the average rating of the skill?");
            }else{
                Manager manager = service.getManagerById(managerId);
                if(manager.getSkillsAssessments() == null ){
                    Set<SkillsAsssessment> s = new HashSet<>();
                    s.add(skillAssessment);
                    manager.setSkillsAssessments(s);
                }else{
                    manager.getSkillsAssessments().add(skillAssessment);
                }
                service.addUser(manager);
                return new ApiResponse(200, "Dear " + manager.getUserFullName() + ", you have successfully added " + skillAssessment);

            }
        }catch (Exception e){
            return  new ApiResponse(500, e.getMessage());
        }
    }
    // get added skills





    //assess critical Roles




 }

