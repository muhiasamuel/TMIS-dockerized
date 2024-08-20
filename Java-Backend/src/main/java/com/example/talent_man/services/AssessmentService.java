package com.example.talent_man.services;

import com.example.talent_man.dto.assessment.*;
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

    List<ManagerAssessmentResponse> getAssessmentsNotAssessedByManager(int managerId);

    List<UserScoringHistoryDto> getUserScoringHistory(int userId);
    boolean doesUserExist(int userId);
    List<Assessment> findAssessmentsByPotentialAttribute(PotentialAttribute attribute);
    //REad all assignments of an attribute
    List<Assessment> getAttAssess(int attId);
    List<ManagerUserAssessmentStatusDto> getManagerUsersAssessmentStatus(int managerId);
}
