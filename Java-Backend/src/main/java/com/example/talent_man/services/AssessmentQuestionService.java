package com.example.talent_man.services;

import com.example.talent_man.models.Assessment;
import com.example.talent_man.models.AssessmentQuestion;

import java.util.List;

/*
                            NOTE
        - Assessment questions will be added in the assessment service to maintain the integrity of the database.
 */

public interface AssessmentQuestionService {
    //read
    AssessmentQuestion saveQuestion(AssessmentQuestion question);
    AssessmentQuestion getAssessmentQuestionById(int id);
    List<AssessmentQuestion> getAssessmentAllQuestions();
    //update
    AssessmentQuestion updateAssessmentQuestion(int id, Assessment assQuestion);
    //delete


}
