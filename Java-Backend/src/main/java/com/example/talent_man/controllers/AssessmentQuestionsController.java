package com.example.talent_man.controllers;

import com.example.talent_man.models.Assessment;
import com.example.talent_man.models.AssessmentQuestion;
import com.example.talent_man.service_imp.AssessmentQuestionServiceImp;
import com.example.talent_man.service_imp.AssessmentServiceImp;
import com.example.talent_man.service_imp.PotentialAttributeServiceImp;
import com.example.talent_man.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RequestMapping("/v1/api")
@RestController
public class AssessmentQuestionsController {
    @Autowired
    private AssessmentQuestionServiceImp qService;
    @Autowired
    private AssessmentServiceImp aService;
    @Autowired
    private PotentialAttributeServiceImp pService;

    @GetMapping("/allQuestsByAttAndAssId")
    public ApiResponse<Set<AssessmentQuestion>> getQuestionsBasedOnAttributeAndAssessmentId(@RequestParam Integer attId, @RequestParam Integer assId){
        try{
            if(assId == null || attId == null || attId == 0 || assId == 0 ){
                return new ApiResponse<>(301, "Enter a valid id");
            }else if(pService.getPotentialAttributeById(attId) == null){
                return new ApiResponse<>(301, "No attribute found by the given id");

            }else if(aService.getById(assId) == null){
                return new ApiResponse<>(301, "No assessment found by the given id");
            }else{
                //PotentialAttribute pAtt = pService.getPotentialAttributeById(attId);
                Assessment ass = aService.getById(assId);

                if(ass.getAssessmentQuestions() == null || ass.getAssessmentQuestions().isEmpty()){
                    return new ApiResponse<>(403, "No questions found by the given assessment id");
                }else{
                    Set<AssessmentQuestion> assessmentQuestionSet = ass.getAssessmentQuestions();
                    ApiResponse<Set<AssessmentQuestion>> response = new ApiResponse<>(200, "successful");
                    response.setItem(assessmentQuestionSet);
                    return response;
                }
            }
        }catch(Exception e){
            return new ApiResponse<>(500, e.getMessage());
        }
    }
    @GetMapping("/allQuestsByAssId")
    public ApiResponse<Set<AssessmentQuestion>> getQuestionsBasedOnAssessmentId(@RequestParam Integer assId){
        try{
            if(assId == null || assId == 0 ){
                return new ApiResponse<>(301, "Enter a valid id");
            }else if(aService.getById(assId) == null){
                return new ApiResponse<>(301, "No assessment found by the given id");
            }else{
                Assessment ass = aService.getById(assId);

                if(ass.getAssessmentQuestions() == null || ass.getAssessmentQuestions().isEmpty()){
                    return new ApiResponse<>(403, "No questions found by the given assessment id");
                }else{
                    Set<AssessmentQuestion> assessmentQuestionSet = ass.getAssessmentQuestions();
                    ApiResponse<Set<AssessmentQuestion>> response = new ApiResponse<>(200, "successful");
                    response.setItem(assessmentQuestionSet);
                    return response;
                }
            }
        }catch(Exception e){
            return new ApiResponse<>(500, e.getMessage());
        }
    }


}
