package com.example.talent_man.controllers.pages.controllers;

import com.example.talent_man.controllers.pages.dtos.*;
import com.example.talent_man.dto.Attributes;
import com.example.talent_man.models.Assessment;
import com.example.talent_man.models.AssessmentQuestion;
import com.example.talent_man.models.Choice;
import com.example.talent_man.models.PotentialAttribute;
import com.example.talent_man.models.user.Manager;
import com.example.talent_man.service_imp.*;
import com.example.talent_man.services.UserService;
import com.example.talent_man.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Map;

@RestController()
@RequestMapping("/v1/api/pages")
public class PageController {
    @Autowired
    private UserService uService;

    @Autowired
    private UserQuestionAnswerImp qService;

    @Autowired
    private ChoiceServiceImp cService;

    @Autowired
    private AssessmentServiceImp assService;

    @Autowired
    private AssessmentQuestionServiceImp assQuestService;
    @Autowired
    private PotentialAttributeServiceImp pService;

    @GetMapping("/attributesPage")
    public ApiResponse<AttributePageDto> getAttributesPage(@RequestParam Integer manId) {
        try {
            if (manId == null || manId == 0) {
                return new ApiResponse<>(301, "Enter a valid id");
            } else if (uService.getManagerById(manId) == null) {
                return new ApiResponse<>(301, "No user existing with the given id");
            } else {
                // initialising the response object
                ApiResponse<AttributePageDto> response = new ApiResponse<>();
                AttributePageDto pageDto = new AttributePageDto();
                List<Attributes> attemptedByAll = new ArrayList<>();
                List<Attributes> attemptedByNone = new ArrayList<>();
                List<PerformedAttribute> performed = new ArrayList<>();

                //getting the manager from the db
                Manager man = uService.getManagerById(manId);

                //getting number of employees of a manager
                List<Integer> manEmployees = uService.getManEmployees(manId);


                //getting all potential attributes associated by him
                Set<PotentialAttribute> atts = man.getPotentialAttributeSet();

                if (atts == null || atts.isEmpty()) {
                    pageDto.setTotalAttributes(0);
                    pageDto.setAttemptedByAll(null);
                    pageDto.setNotAttemptedByALl(null);
                    pageDto.setPerformed(null);
                    response.setItem(pageDto);
                    response.setStatus(200);
                    response.setMessage("successful");
                    return response;
                } else {

                    //iterating over each attribute
                    for (PotentialAttribute att : atts) {
                        PerformedAttribute performedAttribute = new PerformedAttribute();

                        //getting all assessments associated with the user
                        Set<Assessment> assess = att.getAssessments();
                        int attsDoneBy = 0;
                        if (assess != null && !assess.isEmpty()) {
                            //iterate over each assessment
                            if (manEmployees != null && !manEmployees.isEmpty()) {
                                List<Integer> assScores = new ArrayList<>();

                                //initialising a list to store done by
                                List<Integer> attScores = new ArrayList<>();

                                for (Assessment ass : assess) {
                                    //getting all the users who have attempted the assessment
                                    List<Integer> userIds = qService.userDoneAssessIds(ass.getAssessmentId(), manId);
                                    int doneBy = 0;
                                    if (userIds != null) {
                                        attsDoneBy += userIds.size();
                                        doneBy = userIds.size();
                                        //getting all the questions of the assessment
                                        Set<AssessmentQuestion> questions = ass.getAssessmentQuestions();
                                        if (questions != null && !questions.isEmpty()) {
                                            //iterate over each question
                                            int assTotalCount = 0;
                                            for (AssessmentQuestion assQ : questions) {
                                                //getting the best performed attribute
                                                for (Integer i : userIds) {

                                                    //getting the choice selected by the user
                                                    List<Integer> selectedChoiceIds = qService.getEmployeeSelectedChoice(i, assQ.getAssessmentQuestionId(), ass.getAssessmentId());
                                                    int selectedChoiceId = selectedChoiceIds.get(selectedChoiceIds.size() - 1);

                                                    //getting the question choices
                                                    if (assQ.getChoices() != null && !assQ.getChoices().isEmpty()) {
                                                        for (Choice c : assQ.getChoices()) {
                                                            if (c.getChoiceId() == selectedChoiceId) {
                                                                assTotalCount += Integer.parseInt(c.getChoiceValue());
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                            attScores.add(doneBy);
                                            performedAttribute.setAttId(att.getPotentialAttributeId());
                                            performedAttribute.setAttName(att.getPotentialAttributeName());
                                            performedAttribute.setAttScore(assTotalCount);
                                            assScores.add(assTotalCount);
                                            performedAttribute.calculateAverageScore(assScores);
                                            //performedAttribute.setAssName(ass.getAssessmentName());
                                        }
                                    }
                                }
                                //performedAttribute.setDoneBy(attsDoneBy);
                                performed.add(performedAttribute);

                                int confirmInt = 0;
                                for (Integer attScore:attScores) {
                                    if(attScore == manEmployees.size()){
                                        confirmInt ++;
                                    }
                                }
                                if (confirmInt == attScores.size()){
                                     Attributes attributes = new Attributes(att.getPotentialAttributeName(), att.getPotentialAttributeDescription());
                                    attemptedByAll.add(attributes);
                                }else{
                                    Attributes attributes = new Attributes(att.getPotentialAttributeName(), att.getPotentialAttributeDescription());
                                    attemptedByNone.add(attributes);
                                }
                            }

                        }
                    }


                    pageDto.setPerformed(performed);
                    pageDto.setAttemptedByAll(attemptedByAll);
                    pageDto.setNotAttemptedByALl(attemptedByNone);
                    pageDto.setTotalAttributes(atts.size());
                    pageDto.setPotentialAttributes(atts);
                    response.setItem(pageDto);
                    response.setStatus(200);
                    response.setMessage("successful");
                    return response;
                }


            }
        } catch (Exception e) {
            return new ApiResponse<>(301, e.getMessage());
        }

    }

    @GetMapping("/assessmentsPage")
    public ApiResponse<AssessmentsPageDto> getPageAssessments(@RequestParam Integer manId, @RequestParam Integer attId){
        try{
            if (manId == null || manId == 0 || attId == null || attId == 0) {
                return new ApiResponse<>(301, "Enter a valid id");
            } else if (uService.getManagerById(manId) == null) {
                return new ApiResponse<>(301, "No user existing with the given id");
            } else if (pService.getPotentialAttributeById(attId) == null) {
                return new ApiResponse<>(301, "No attribute existing with the given id");
            }else{
                AssessmentsPageDto assessmentsPageDto  = new AssessmentsPageDto();

                List<Integer> manEmployees = uService.getManEmployees(manId);

                PotentialAttribute attribute = pService.getPotentialAttributeById(attId);

                //getting all assessments of the given attribute
                Set<Assessment> assessments = attribute.getAssessments();
                List<PageAssessment> pageAssessments= new ArrayList<>();

                if(assessments != null && !assessments.isEmpty()){
                    //iterate over each assessment
                    for (Assessment ass:assessments) {
                        PageAssessment pageAssessment = new PageAssessment();
                        //getting all the users who have done the assessment
                        List<Integer> userIds = qService.userDoneAssessIds(ass.getAssessmentId(), manId);

                        pageAssessment.setAssId(ass.getAssessmentId());
                        pageAssessment.setAssName(ass.getAssessmentName());
                        pageAssessment.setAssDescription(ass.getAssessmentDescription());

                        if(userIds.size() != 0){
                            pageAssessment.setDoneBy(userIds.size());

                            //getting all the assessment questions
                            Set<AssessmentQuestion> assessmentQuestions = ass.getAssessmentQuestions();
                            if(assessmentQuestions != null && !assessmentQuestions.isEmpty()){
                                //iterate over each question
                                int assTotalCount = 0;
                                List<Integer> questionScore = new ArrayList<>();
                                for (AssessmentQuestion assQ : assessmentQuestions) {
                                    //getting the best performed attribute
                                    int userScore = 0;

                                    for (Integer i : userIds) {

                                        //getting the choice selected by the user
                                        List<Integer> selectedChoiceIds = qService.getEmployeeSelectedChoice(i, assQ.getAssessmentQuestionId(), ass.getAssessmentId());
                                        int selectedChoiceId = selectedChoiceIds.get(selectedChoiceIds.size() - 1);

                                        //getting the question choices
                                        if (assQ.getChoices() != null && !assQ.getChoices().isEmpty()) {
                                            for (Choice c : assQ.getChoices()) {
                                                if (c.getChoiceId() == selectedChoiceId) {
                                                    userScore = Integer.parseInt(c.getChoiceValue());
                                                    assTotalCount += Integer.parseInt(c.getChoiceValue());
                                                }
                                            }
                                        }
                                        questionScore.add(userScore);
                                    }
                                }
                                pageAssessment.setAssScore(assTotalCount);
                                pageAssessment.calculateAverageScore(questionScore);

                            }
                        }else{
                            pageAssessment.setDoneBy(0);
                            pageAssessment.setAssScore(0);
                            pageAssessment.setEmpAverageScore(0.0);

                        }
                        pageAssessments.add(pageAssessment);
                    }
                }

                assessmentsPageDto.setAssessments(pageAssessments);
                assessmentsPageDto.setAttId(attId);
                assessmentsPageDto.setAttName(attribute.getPotentialAttributeName());
                assessmentsPageDto.setAttDescription(attribute.getPotentialAttributeDescription());
                assessmentsPageDto.setTotalEmployees(manEmployees.size());

                ApiResponse<AssessmentsPageDto> response = new ApiResponse<>(200, "successful");
                response.setItem(assessmentsPageDto);
                return response;
            }
        }catch(Exception e){
            return new ApiResponse<>(500, e.getMessage());

        }
    }

    @GetMapping("/assessMyPotential")
    public ApiResponse<AssessMyPotential> getAssesMyPotentialData(@RequestParam Integer manId){
        try {
            if (manId == null || manId == 0) {
                return new ApiResponse<>(301, "Enter a valid id");
            } else if (uService.getManagerById(manId) == null) {
                return new ApiResponse<>(301, "No user existing with the given id");
            }else{
                AssessMyPotential assessMyPotential = new AssessMyPotential();
                //getting the manager from the db
                Manager man = uService.getManagerById(manId);

                //getting number of employees of a manager
                List<Integer> manEmployees = uService.getManEmployees(manId);


                //getting all potential attributes associated by him
                Set<PotentialAttribute> atts = man.getPotentialAttributeSet();
                int allAssessments = 0;
                for (PotentialAttribute att:atts) {
                    allAssessments += att.getAssessments().size();
                }
                assessMyPotential.setNumberOfAssessments(allAssessments);
                assessMyPotential.setNumberOfEmployees(manEmployees.size());
                assessMyPotential.setManagerName(man.getUserFullName());
                ApiResponse<AssessMyPotential> response =  new ApiResponse<>(200, "successful");
                response.setItem(assessMyPotential);
                return response;
            }
        }catch (Exception e){
            return new ApiResponse<>(500, e.getMessage());

        }
    }
}
