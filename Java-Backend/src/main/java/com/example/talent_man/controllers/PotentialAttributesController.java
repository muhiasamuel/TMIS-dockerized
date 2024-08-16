package com.example.talent_man.controllers;

import com.example.talent_man.dto.AssessmentDto;
import com.example.talent_man.dto.PotentialReqAttributeDto;
import com.example.talent_man.dto.assessment.AssQuestionDto;
import com.example.talent_man.dto.assessment.ChoiceDto;
import com.example.talent_man.dto.assessment.UserDoneAssessmentDto;
import com.example.talent_man.dto.hippo.HippoScore;
import com.example.talent_man.dto.potential_attributes.P_AttributeDto;
import com.example.talent_man.dto.potential_attributes.P_HippoScore;
import com.example.talent_man.dto.user.UserPotentialMatrix;
import com.example.talent_man.models.Assessment;
import com.example.talent_man.models.AssessmentQuestion;
import com.example.talent_man.models.Choice;
import com.example.talent_man.models.PotentialAttribute;
import com.example.talent_man.models.user.Manager;
import com.example.talent_man.models.user.User;
import com.example.talent_man.repos.PotentialAttributeRepo;
import com.example.talent_man.service_imp.PotentialAttributeServiceImp;
import com.example.talent_man.service_imp.UserQuestionAnswerImp;
import com.example.talent_man.service_imp.UserServiceImp;
import com.example.talent_man.services.AssessmentService;
import com.example.talent_man.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/v1/api")
public class PotentialAttributesController {
    @Autowired
    private PotentialAttributeServiceImp service;

    @Autowired
    private UserQuestionAnswerImp qService;
    @Autowired
    private UserServiceImp userService;

    @Autowired
    private AssessmentService assessmentService;
    @Autowired
    private PotentialAttributeRepo potentialAttributeRepo;


    @PostMapping(value = "/addAssignment", consumes = "application/json")
    public ApiResponse<Assessment> addAssessment(@RequestBody AssessmentDto ass) {
        try {
            if (ass.getAssessmentName() == null || ass.getAssessmentName().isEmpty()) {
                return new ApiResponse<>(300, "Assessment should have a name");
            } else if (ass.getAssessmentDescription() == null || ass.getAssessmentDescription().isEmpty()) {
                return new ApiResponse<>(300, "Please tell us more about the assessment, what to expect and what is required");
            } else {
                Assessment assessment = new Assessment();
                assessment.setAssessmentName(ass.getAssessmentName());
                assessment.setAssessmentDescription(ass.getAssessmentDescription());
                assessment.setTarget(ass.getTarget());
                assessment.setCreatedAt(ass.getCreatedAt());
                assessment.setEndDate(ass.getEndDate());

                // Retrieve all potential attributes
                Set<PotentialAttribute> potentialAttributes = new HashSet<>(service.getAllPotentialAttributes());
                assessment.setPotentialAttributes(potentialAttributes);

                // Save the assessment
                Assessment savedAssessment = assessmentService.addAss(assessment);
                ApiResponse<Assessment> newSet = new ApiResponse<>(200, "Assessment created successfully");
                newSet.setItem(savedAssessment);
                return newSet;
            }
        } catch (Exception e) {
            return new ApiResponse<>(403, e.getMessage());
        }
    }

    @PostMapping(value = "/addAssessments", consumes = "application/json")
    public ApiResponse<PotentialAttribute> addAssessment(@RequestParam Integer attributeId, @RequestBody AssessmentDto ass){

        try{
            if(attributeId == null || attributeId == 0){
                return new ApiResponse<>(300, "Enter a valid id");
            }else if(ass.getAssessmentName() == null || ass.getAssessmentName().isEmpty()){
                return new ApiResponse<>(300, "Assessment should have a name");
            }else if(ass.getAssessmentDescription() == null || ass.getAssessmentDescription().isEmpty()){
                return new ApiResponse<>(300, "Please tell us more about the assessment, what to expect and what is required");
            }else{
                PotentialAttribute attribute = service.getPotentialAttributeById(attributeId);
                Assessment assessment = new Assessment();
                assessment.setAssessmentName(ass.getAssessmentName());
                assessment.setAssessmentDescription(ass.getAssessmentDescription());
                assessment.setTarget(ass.getTarget());
                assessment.setCreatedAt(ass.getCreatedAt());
                assessment.setEndDate(ass.getEndDate());

                if(attribute.getAssessments() == null || attribute.getAssessments().isEmpty()){
                    Set<Assessment> assess = new HashSet<>();
                    assess.add(assessment);
                    attribute.setAssessments(assess);
                }else{
                    attribute.getAssessments().add(assessment);
                }
                PotentialAttribute att = service.addPotentialAttribute(attribute);
                ApiResponse<PotentialAttribute> newSet = new ApiResponse<>(200,  " successfully");
                newSet.setItem(att);
                return newSet;

            }

        }catch (Exception e){
            return new ApiResponse<>(403, e.getMessage());
        }
    }

    @GetMapping("/getAssessments")
    public ApiResponse<?>getPotentialAttributes(@RequestParam int managerId) {
        ApiResponse<Object> newSet = new ApiResponse<>(200,  " successfully");
        Manager manager = (Manager) userService.getManagerById(managerId);
        newSet.setItem(manager.getPotentialAttributeSet());
        return newSet;  // Return the Set directly
    }

    //getting all atrributes
//    @GetMapping("/getManagerAttributes")
//    public ApiResponse<List<PotentialAttribute>> getManagerAttributes(@RequestParam int managerId){
//        Manager manager;
//        try{
//            if(managerId < 1){
//                return new ApiResponse<>(300, "Enter a valid id");
//            }else if((manager = userService.getManagerById(managerId))== null){
//                return new ApiResponse<>(300, "Manager doesn't exist");
//
//            }else if(manager.getPotentialAttributeSet().isEmpty() || manager.getPotentialAttributeSet() == null){
//                return new ApiResponse<>(300, "Manager doesn't have attributes");
//            }else{
//                List<PotentialAttribute> atts = new ArrayList<>(manager.getPotentialAttributeSet());
//                ApiResponse<List<PotentialAttribute>> response = new ApiResponse<>(200, "Successful");
//                response.setItem(atts);
//                return response;
//            }
//
//        }catch(Exception e){
//            return new ApiResponse<>(500, e.getMessage());
//
//        }
//    }


    @GetMapping("/getManagerAttributes")
    public ApiResponse<List<PotentialReqAttributeDto>> getAllAttributes() {
        try {
            List<PotentialAttribute> allAttributes = potentialAttributeRepo.findAll();

            if (allAttributes.isEmpty()) {
                return new ApiResponse<>(300, "No potential attributes found");
            } else {
                List<PotentialReqAttributeDto> attributeDtos = allAttributes.stream()
                        .map(attribute -> {
                            PotentialReqAttributeDto dto = new PotentialReqAttributeDto();
                            dto.setPotentialAttributeId(attribute.getPotentialAttributeId());
                            dto.setPotentialAttributeName(attribute.getPotentialAttributeName());
                            dto.setPotentialAttributeDescription(attribute.getPotentialAttributeDescription());
                            dto.setCreatedAt(attribute.getCreatedAt()); // Include createdAt if needed
                            return dto;
                        })
                        .collect(Collectors.toList());

                ApiResponse<List<PotentialReqAttributeDto>> response = new ApiResponse<>(200, "Successful");
                response.setItem(attributeDtos);
                return response;
            }
        } catch (Exception e) {
            return new ApiResponse<>(500, e.getMessage());
        }
    }

    @GetMapping("/get/all/attributes")

    public ApiResponse<List<PotentialAttribute>> getAllPotentialAttributes(){
        try{
            List<PotentialAttribute> attr = potentialAttributeRepo.findAll();
            ApiResponse<List<PotentialAttribute>> response = new ApiResponse<>(200, "Successful");
            response.setItem(attr);
            return response;
        }catch (Exception e){
            return new ApiResponse<>(500, e.getMessage());

        }
    }

    //get Attribute by id
    @GetMapping("/getAttribute")
    public ApiResponse<PotentialAttribute> getPotentialAttribute(@RequestParam Integer attId){
        try{
            if(attId == null || attId == 0 ){
                return new ApiResponse<>(301, "Please enter a valid id");
            }else if(service.getPotentialAttributeById(attId) == null){
                return new ApiResponse<>(301, "Attribute not found");
            }else{
                PotentialAttribute att = service.getPotentialAttributeById(attId);
                ApiResponse<PotentialAttribute> response = new ApiResponse<>(200, "successful");
                response.setItem(att);
                return response;
            }
        }catch(Exception e){
            return new ApiResponse<>(500, e.getMessage());
        }
    }


    @GetMapping("/getUserPotentialMatrix")
    public ApiResponse<UserPotentialMatrix> getUserPotentialMatrix(@RequestParam Integer userId, @RequestParam Integer managerId){
        try{
            if(userId == null || userId == 0 || managerId == null || managerId == 0){
                return new ApiResponse<>(301, "Please enter a valid id");
            }else if(userService.getUserById(userId) == null || userService.getUserById(managerId) == null){
                return new ApiResponse<>(301, "No user found with the provided id");
            }else{
                //initialising an instance to store the response
                UserPotentialMatrix userPotentialMatrix = new UserPotentialMatrix();

                //getting the user fom the database
                User user = userService.getUserById(userId);
                Manager manager = userService.getManagerById(managerId);

                userPotentialMatrix.setUserId(user.getUserId());
                userPotentialMatrix.setUserName(user.getUserFullName());

                //getting all potential attributes associated with the manager
                Set<PotentialAttribute> attributes = manager.getPotentialAttributeSet();
                List<P_AttributeDto> resAttributes = new ArrayList<>();

                if(attributes != null && !attributes.isEmpty()){
                    for (PotentialAttribute att: attributes) {
                        //initialising
                        P_AttributeDto p_attributeDto = new P_AttributeDto();
                        p_attributeDto.setPAttId(att.getPotentialAttributeId());
                        p_attributeDto.setPAttName(att.getPotentialAttributeName());

                        //getting attribute assessments
                        Set<Assessment> assessments = att.getAssessments();
                        List<UserDoneAssessmentDto> resAssess = new ArrayList<>();

                        if(assessments != null && !assessments.isEmpty()){
                            for (Assessment ass:assessments) {
                                //initialising
                                UserDoneAssessmentDto userAssessmentDto = new UserDoneAssessmentDto();
                                userAssessmentDto.setId(ass.getAssessmentId());
                                userAssessmentDto.setAssName(ass.getAssessmentName());

                                //getting assessment questions
                                Set<AssessmentQuestion> questions = ass.getAssessmentQuestions();
                                List<AssQuestionDto> resQuestions = new ArrayList<>();

                                if(questions != null && !questions.isEmpty()){
                                    for (AssessmentQuestion q:questions) {
                                        //initialising
                                        AssQuestionDto assQuestionDto = new AssQuestionDto();
                                        assQuestionDto.setQuestionId(q.getAssessmentQuestionId());
                                        assQuestionDto.setQuestionDescription(q.getAssessmentQuestionDescription());

                                        //getting all the question choices
                                        Set<Choice> choices = q.getChoices();

                                        if(choices != null && !choices.isEmpty()){
                                            //getting the manager selected choice
                                            List<Integer> selectedChoiceIds = qService.getManagerSelectedChoice(userId, q.getAssessmentQuestionId(),ass.getAssessmentId(),managerId);
                                            if (selectedChoiceIds != null && !selectedChoiceIds.isEmpty()){
                                                int selectedChoiceId = selectedChoiceIds.get(selectedChoiceIds.size() - 1);

                                                //assigning the managers selected choice
                                                ChoiceDto choiceDto = new ChoiceDto();
                                                for (Choice c:choices) {
                                                    if(c.getChoiceId() == selectedChoiceId){
                                                        choiceDto.setChoiceId(selectedChoiceId);
                                                        choiceDto.setChoiceName(c.getChoiceName());
                                                        choiceDto.setChoiceValue(Integer.parseInt(c.getChoiceValue()));
                                                    }

                                                }
                                                assQuestionDto.setManagerChoice(choiceDto);
                                            }
                                        }
                                        resQuestions.add(assQuestionDto);
                                    }
                                }
                                userAssessmentDto.setAssQuestions(resQuestions);
                                resAssess.add(userAssessmentDto);

                            }
                        }
                        p_attributeDto.setAssessments(resAssess);
                        resAttributes.add(p_attributeDto);
                    }
                }
                userPotentialMatrix.setPAttributes(resAttributes);
                ApiResponse<UserPotentialMatrix> response = new ApiResponse<>(200, "successful");
                response.setItem(userPotentialMatrix);
                return response;
            }

        }catch(Exception e){
            return new ApiResponse<>(500, e.getMessage());
        }
    }

    //getting HIPPO results of a user
    @GetMapping("/getUserHippo")
    public ApiResponse<List<HippoScore>> getUserHippoSCore(@RequestParam Integer manId){
        try{
            if(manId == null || manId == 0){
                return new ApiResponse<>(301, "Please enter a valid id");
            }else if(userService.getUserById(manId) == null){
                return new ApiResponse<>(301, "No user found with the provided id");
            }else{
                //initialising the response object
                List<HippoScore> resObject = new ArrayList<>();

                //getting the manager
                Manager manager = userService.getManagerById(manId);

                //getting the users associated with the manager
                List<User> users = userService.getManagerEmployees(manager);

                if(users != null && !users.isEmpty()){
                    for (User user:users) {
                        HippoScore hippoScoreObject = new HippoScore();
                        hippoScoreObject.setEmpId(user.getUserId());
                        hippoScoreObject.setEmpName(user.getUserFullName());

                        //getting all the potential attributes associated with the manager
                        Set<PotentialAttribute> pAtts = manager.getPotentialAttributeSet();
                        List<P_HippoScore> hippoScores = new ArrayList<>();

                        if (pAtts != null && !pAtts.isEmpty()){
                            for (PotentialAttribute att:pAtts) {
                                P_HippoScore hippoScore = new P_HippoScore();
                                hippoScore.setId(att.getPotentialAttributeId());
                                hippoScore.setPAttName(att.getPotentialAttributeName());

                                //getting assessments
                                Set<Assessment> assessments = att.getAssessments();

                                //getting assessment average scores
                                double assessmentScore = 0.0;
                                List<Integer> assessmentScores = new ArrayList<>();
                                if(assessments != null && !assessments.isEmpty()){
                                    for (Assessment ass:assessments) {
                                        //getting the assessment questions
                                        Set<AssessmentQuestion> assessmentQuestions = ass.getAssessmentQuestions();
                                        List<Integer> questionScores = new ArrayList<>();
                                        if(assessmentQuestions != null && !assessmentQuestions.isEmpty()){
                                            for (AssessmentQuestion assQ: assessmentQuestions) {
                                                //getting the manager selected choice
                                                List<Integer> selectedChoiceIds = qService.getManagerSelectedChoice(user.getUserId(), assQ.getAssessmentQuestionId(),ass.getAssessmentId(),manId);
                                                int managerChoiceValue = 0;

                                                if (selectedChoiceIds != null && !selectedChoiceIds.isEmpty()) {
                                                    int selectedChoiceId = selectedChoiceIds.get(selectedChoiceIds.size() - 1);
                                                    //getting question choices
                                                    Set<Choice> qChoices = assQ.getChoices();
                                                    if(qChoices != null && !qChoices.isEmpty()){
                                                        for (Choice c:qChoices) {
                                                            if(c.getChoiceId() == selectedChoiceId){
                                                                questionScores.add(Integer.parseInt(c.getChoiceValue()));
                                                            }

                                                        }
                                                    }
                                                }else{
                                                    questionScores.add(managerChoiceValue);
                                                }


                                            }
                                        }

                                        //calculating assessment total score
                                        int totalSCore = 0;
                                        for (Integer manScore :questionScores) {
                                            totalSCore += manScore;
                                        }
                                        assessmentScores.add(totalSCore);
                                    }
                                }

                                //calculating hippo scores
                                hippoScore.calculateTotalScore(assessmentScores);
                                hippoScores.add(hippoScore);
                            }
                        }

                        hippoScoreObject.setAttScores(hippoScores);
                        List<Integer> scores = new ArrayList<>();
                        for (P_HippoScore p:hippoScores) {
                            scores.add(p.getScore());
                        }
                        hippoScoreObject.calculateAverageScore(scores);
                        resObject.add(hippoScoreObject);
                    }

                }

                ApiResponse<List<HippoScore>> response = new ApiResponse<>(200, "successful");
                response.setItem(resObject);
                return response;
            }
        }catch(Exception e){
            return new ApiResponse<>(500, e.getMessage());
        }
    }

}
