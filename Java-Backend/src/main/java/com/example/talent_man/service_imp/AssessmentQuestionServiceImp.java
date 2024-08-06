package com.example.talent_man.service_imp;

import com.example.talent_man.models.Assessment;
import com.example.talent_man.models.AssessmentQuestion;
import com.example.talent_man.repos.AssessmentQuestionsRepo;
import com.example.talent_man.services.AssessmentQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/*
        NOTE
      - To maintain the integrity of the database questions will be added using the assignment
        they are contained in.
      - We have methods inside the Assessment Service which can be used to add a list of questions
        and a single question in the database.
 */

@Service
public class AssessmentQuestionServiceImp implements AssessmentQuestionService {
    @Autowired
    private AssessmentQuestionsRepo repo;

    @Override
    public AssessmentQuestion saveQuestion(AssessmentQuestion question) {
        return repo.save(question);
    }

    @Override
    public AssessmentQuestion getAssessmentQuestionById(int id) {
        return repo.getReferenceById(id);
    }

    @Override
    public List<AssessmentQuestion> getAssessmentAllQuestions() {
        return null;
    }

    @Override
    public AssessmentQuestion updateAssessmentQuestion(int id, Assessment assQuestion) {
        return null;
    }
}
