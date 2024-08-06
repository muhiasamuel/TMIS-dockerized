package com.example.talent_man.controllers;

import com.example.talent_man.models.Answer;
import com.example.talent_man.models.AssessmentQuestion;
import com.example.talent_man.models.Choice;
import com.example.talent_man.models.UserQuestionAnswer;
import com.example.talent_man.models.composite_keys.UserAnswerKey;
import com.example.talent_man.models.user.User;
import com.example.talent_man.service_imp.AssessmentQuestionServiceImp;
import com.example.talent_man.service_imp.ChoiceServiceImp;
import com.example.talent_man.service_imp.UserServiceImp;
import com.example.talent_man.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ChoiceController {
    @Autowired
    private ChoiceServiceImp serviceImp;
    @Autowired
    private UserServiceImp uServiceImp;
    @Autowired
    private AssessmentQuestionServiceImp qServiceImp;


}
