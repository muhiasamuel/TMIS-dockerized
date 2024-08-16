package com.example.talent_man.services;

import com.example.talent_man.dto.assessment.AssessmentDto;
import com.example.talent_man.dto.assessment.AssessmentResponse;
import com.example.talent_man.models.Assessment;
import com.example.talent_man.models.PotentialAttribute;

import java.util.List;

public interface AssessmentService {
    //create
    Assessment addAss(Assessment ass);

    //Read
    Assessment getById(int id);
    AssessmentDto getAssessmentWithAttributesAndQuestions(int assessmentId);


    List<AssessmentDto> getActiveAssessments();

    List<AssessmentResponse> getActiveAssessmentsNotAttemptedByUser(int userId);

    boolean doesUserExist(int userId);
    List<Assessment> findAssessmentsByPotentialAttribute(PotentialAttribute attribute);
    //REad all assignments of an attribute
    List<Assessment> getAttAssess(int attId);
}
