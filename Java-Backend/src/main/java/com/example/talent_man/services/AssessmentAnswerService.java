package com.example.talent_man.services;

import com.example.talent_man.dto.answers.ManagerAnswerDTO;
import com.example.talent_man.dto.answers.UserAnswerDTO;
import com.example.talent_man.models.answers.UserManSelectedQuestionAnswer;
import com.example.talent_man.utils.ApiResponse;

import java.util.List;

public interface AssessmentAnswerService {

    ApiResponse<List<UserManSelectedQuestionAnswer>> submitUserAnswers(UserAnswerDTO userAnswerDTO);
    ApiResponse<List<UserManSelectedQuestionAnswer>> submitManagerAnswers(ManagerAnswerDTO managerAnswerDTO);

}
