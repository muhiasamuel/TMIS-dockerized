package com.example.talent_man.controllers.answers;

import com.example.talent_man.dto.answers.ManagerAnswerDTO;
import com.example.talent_man.dto.answers.UserAnswerDTO;
import com.example.talent_man.models.answers.UserManSelectedQuestionAnswer;
import com.example.talent_man.service_imp.AssessmentQuestionServiceImp;
import com.example.talent_man.services.AssessmentAnswerService;
import com.example.talent_man.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/api/answers")
public class AssessmentAnswersController {
    @Autowired
    private AssessmentAnswerService service;
    @PostMapping("/user")
    public ResponseEntity<ApiResponse<List<UserManSelectedQuestionAnswer>>> submitUserAnswer(@RequestBody UserAnswerDTO userAnswerDTO) {
        ApiResponse<List<UserManSelectedQuestionAnswer>> response = service.submitUserAnswers(userAnswerDTO);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/manager")
    public ResponseEntity<ApiResponse<List<UserManSelectedQuestionAnswer>>> submitManagerAnswer(@RequestBody ManagerAnswerDTO managerAnswerDTO) {


        ApiResponse<List<UserManSelectedQuestionAnswer>> response = service.submitManagerAnswers(managerAnswerDTO);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}

