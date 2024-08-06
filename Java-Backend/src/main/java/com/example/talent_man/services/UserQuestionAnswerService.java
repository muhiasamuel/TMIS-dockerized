package com.example.talent_man.services;

import com.example.talent_man.models.UserQuestionAnswer;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserQuestionAnswerService {
    //Create
    UserQuestionAnswer addUserAnswer(UserQuestionAnswer ans);

    List<UserQuestionAnswer> addMany(List<UserQuestionAnswer> answers);
    List<UserQuestionAnswer> getUserAnswers(int assId, int userId);
    List<UserQuestionAnswer> getAll();

    List<Integer> managerDoneAssessIds(int managerId);

    List<Integer> userDoneAssessIds(int assId, int managerId);
    List<Integer> userDoneAssQuestionsIds(int assId, int employeeId);

    List<Integer> getEmployeeSelectedChoice(int empId, int qId, int assId);

    void updateManagerSelectedChoices(int assId, int manId, int questionId, int selectedManagerChoice, int empId);

    List<UserQuestionAnswer> getUserQuestionAnswer(int assId, int empId, int manId, int questionId);

    List<Integer> getUserDoneAssessments(int empId);

    List<Integer> getUserAssessedAssessments(int manId, int empId);

    List<Integer> getManagerUnAssessedAssessments(int manId);
    List<Integer> getManagerAssessedAssessments(int manId);

    List<Integer> getManagerSelectedChoice(int empId, int qId, int assId, int manId);

    List<Integer> getAssessedEmployees(int manId, int assId);

    List<Integer> getUnAssessedEmployees(int manId, int assId);



}
