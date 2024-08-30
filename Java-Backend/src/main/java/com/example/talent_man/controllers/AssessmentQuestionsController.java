package com.example.talent_man.controllers;

import com.example.talent_man.dto.assessment.*;
import com.example.talent_man.models.Assessment;
import com.example.talent_man.models.AssessmentQuestion;
import com.example.talent_man.service_imp.AssessmentQuestionServiceImp;
import com.example.talent_man.service_imp.AssessmentServiceImp;
import com.example.talent_man.service_imp.PotentialAttributeServiceImp;
import com.example.talent_man.services.AssessmentService;
import com.example.talent_man.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequestMapping("/v1/api")
@RestController
public class AssessmentQuestionsController {
    @Autowired
    private AssessmentQuestionServiceImp qService;
    @Autowired
    private AssessmentServiceImp aService;

    @Autowired
    private AssessmentService assessmentService;
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

    @GetMapping("/getQuestionsByAttributeId")
    public ApiResponse<List<QuestionDto>> getQuestionsByAttributeId(@RequestParam int attributeId) {
        try {
            if (attributeId < 1) {
                return new ApiResponse<>(300, "Enter a valid attribute ID");
            }

            List<QuestionDto> questionDtos = pService.getQuestionsByAttributeId(attributeId);
            if (questionDtos == null || questionDtos.isEmpty()) {
                return new ApiResponse<>(300, "No questions found for this potential attribute");
            }

            ApiResponse<List<QuestionDto>> response = new ApiResponse<>(200, "Successful");
            response.setItem(questionDtos);
            return response;

        } catch (Exception e) {
            return new ApiResponse<>(500, e.getMessage());
        }
    }
    @GetMapping("/getAssessmentWithAttributesAndQuestions")
    public ApiResponse<AssessmentDto> getAssessmentWithAttributesAndQuestions(@RequestParam int assessmentId) {
        try {
            if (assessmentId < 1) {
                return new ApiResponse<>(300, "Enter a valid assessment ID");
            }

            AssessmentDto assessmentDto = assessmentService.getAssessmentWithAttributesAndQuestions(assessmentId);
            if (assessmentDto == null) {
                return new ApiResponse<>(300, "Assessment not found");
            }

            ApiResponse<AssessmentDto> response = new ApiResponse<>(200, "Successful");
            response.setItem(assessmentDto);
            return response;

        } catch (Exception e) {
            return new ApiResponse<>(500, e.getMessage());
        }
    }

    @GetMapping("/activeAssessments")
    public ApiResponse<List<AssessmentDto>> getActiveAssessments() {
        try {
            List<AssessmentDto> activeAssessments = assessmentService.getActiveAssessments();

            if (activeAssessments.isEmpty()) {
                return new ApiResponse<>(300, "No active assessments found");
            }

            ApiResponse<List<AssessmentDto>> response = new ApiResponse<>(200, "Successfully retrieved active assessments");
            response.setItem(activeAssessments);
            return response;
        } catch (Exception e) {
            return new ApiResponse<>(500, e.getMessage());
        }
    }

    @GetMapping("/active/not-attempted/{userId}")
    public ApiResponse<List<AssessmentResponse>> getActiveAssessmentsNotAttempted(@PathVariable int userId) {

        // Check if the user exists in the system
        if (!assessmentService.doesUserExist(userId)) {
            return new ApiResponse<>(404, "User not found.");
        }
        List<AssessmentResponse> assessmentResponses = assessmentService.getActiveAssessmentsNotAttemptedByUser(userId);

        // Filter to get only assessments that have not been attempted
        List<AssessmentResponse> notAttemptedAssessmentResponses = assessmentResponses.stream()
                .filter(response -> !response.isAttempted()) // Check if the assessment is not attempted
                .collect(Collectors.toList());

        // Check if the filtered list is empty and return a 404 response if no assessments are found
        if (notAttemptedAssessmentResponses.isEmpty()) {
            return new ApiResponse<>(404, "No active assessments found that have not been attempted.");
        }

        // Create a new ApiResponse object with status and message
        ApiResponse<List<AssessmentResponse>> response = new ApiResponse<>(200, "Active assessments retrieved successfully.");

        // Set the list of AssessmentResponse items
        response.setItem(notAttemptedAssessmentResponses);

        return response;
    }

    @GetMapping("/not-assessed/{managerId}")
    public ApiResponse<List<ManagerAssessmentResponse>> getAssessmentsNotAssessedByManager(@PathVariable("managerId") int managerId) {
        // Retrieve assessments not yet assessed by the manager
        List<ManagerAssessmentResponse> assessments = assessmentService.getAssessmentsNotAssessedByManager(managerId);

        // Check if the manager exists
        if (!assessmentService.doesUserExist(managerId)) {
            return new ApiResponse<>(404, "Manager not found.");
        }

        // Check if there are any assessments
        if (assessments.isEmpty()) {
            return new ApiResponse<>(404, "No assessments found that have not been assessed by the manager.");
        }
        // Create a new ApiResponse object with status, message, and data
        ApiResponse<List<ManagerAssessmentResponse>> response = new ApiResponse<>(200, "Assessments not assessed by the manager retrieved successfully.");
        response.setItem(assessments);

        return response;

        // Return success response with the list of assessments
    }

    @GetMapping("/{managerId}/assessment-status")
    public ApiResponse<List<ManagerUserAssessmentStatusDto>> getManagerUsersAssessmentStatus(
            @PathVariable("managerId") int managerId) {

        // Check if the manager exists
        if (!assessmentService.doesUserExist(managerId)) {
            return new ApiResponse<>(404, "Manager not found.");
        }

        // Retrieve the assessment status for all users managed by the given managerId
        List<ManagerUserAssessmentStatusDto> statusList = assessmentService.getManagerUsersAssessmentStatus(managerId);

        // Check if there are any statuses
        if (statusList.isEmpty()) {
            return new ApiResponse<>(404, "No assessment status found for the given manager ID.");
        }

        // Create a new ApiResponse object with status, message, and data
        ApiResponse<List<ManagerUserAssessmentStatusDto>> response = new ApiResponse<>(200, "Assessment status retrieved successfully.");
        response.setItem(statusList);

        return response;
    }

    @GetMapping("/user/{userId}/scoring-history")
    public ApiResponse<List<UserScoringHistoryDto>> getUserScoringHistory(@PathVariable("userId") int userId) {
        // Check if the user exists
        if (!assessmentService.doesUserExist(userId)) {
            return new ApiResponse<>(404, "User not found.");
        }

        // Retrieve the scoring history for the user
        List<UserScoringHistoryDto> historyList = assessmentService.getUserScoringHistory(userId);

        // Check if there is any scoring history
        if (historyList.isEmpty()) {
            return new ApiResponse<>(404, "No scoring history found for the given user ID.");
        }

        // Create a new ApiResponse object with status, message, and data
        ApiResponse<List<UserScoringHistoryDto>> response = new ApiResponse<>(200, "Scoring history retrieved successfully.");
        response.setItem(historyList);

        return response;
    }

    @GetMapping("/allAssessments")
    public ApiResponse<List<com.example.talent_man.dto.AssessmentDtoRes>> getAllAssessments() {
        try {
            List<com.example.talent_man.dto.AssessmentDtoRes> assessments = assessmentService.getAllAssessments();

            if (assessments.isEmpty()) {
                return new ApiResponse<>(300, "No active assessments found");
            }

            ApiResponse<List<com.example.talent_man.dto.AssessmentDtoRes>> response = new ApiResponse<>(200, "Successfully retrieved active assessments");
            response.setItem(assessments);
            return response;
        } catch (Exception e) {
            return new ApiResponse<>(500, e.getMessage());
        }
    }
}
