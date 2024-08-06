package com.example.talent_man.service_imp;

import com.example.talent_man.models.UserQuestionAnswer;
import com.example.talent_man.repos.UserQuestionAnswerRepo;
import com.example.talent_man.services.UserQuestionAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserQuestionAnswerImp implements UserQuestionAnswerService {
    @Autowired
    private UserQuestionAnswerRepo repo;

    @Override
    public UserQuestionAnswer addUserAnswer(UserQuestionAnswer ans) {
        return repo.save(ans);
    }

    @Override
    public List<UserQuestionAnswer> addMany(List<UserQuestionAnswer> answers) {
        return repo.saveAll(answers);
    }

    @Override
    public List<UserQuestionAnswer> getUserAnswers(int assId, int userId) {
        return repo.answeredAssess(assId,userId);
    }

    @Override
    public List<UserQuestionAnswer> getAll() {
        return repo.findAll();
    }

    @Override
    public List<Integer> managerDoneAssessIds(int managerId) {
        return repo.managerDoneAssess(managerId);
    }

    @Override
    public List<Integer> userDoneAssessIds(int assId, int managerId) {
        return repo.userDoneAssess(assId, managerId);
    }

    @Override
    public List<Integer> userDoneAssQuestionsIds(int assId, int employeeId) {
        return repo.userDoneAssessQuestions(assId,employeeId);
    }

    @Override
    public List<Integer> getEmployeeSelectedChoice(int empId, int qId, int assId) {
        return repo.getEmpSelectedChoice(empId,qId,assId);
    }

    @Override
    public void updateManagerSelectedChoices(int assId, int manId, int questionId, int selectedManagerChoice, int empId) {
        repo.managerUpdateChoiceSelected(assId, manId, questionId, selectedManagerChoice, empId);
    }

    @Override
    public List<UserQuestionAnswer> getUserQuestionAnswer(int assId, int empId, int manId, int questionId) {
        return repo.getUserQuestionAnswer(assId, manId, questionId, empId);
    }

    @Override
    public List<Integer> getUserDoneAssessments(int empId) {
        return repo.userDoneAssessments(empId);
    }

    @Override
    public List<Integer> getUserAssessedAssessments(int manId, int empId) {
        return repo.getUserAssessedAssess(manId, empId);
    }

    @Override
    public List<Integer> getManagerUnAssessedAssessments(int manId) {
        return repo.getManagerUnAssessedAssess(manId);
    }

    @Override
    public List<Integer> getManagerAssessedAssessments(int manId) {
        return repo.getManagerAssessedAssess(manId);
    }

    @Override
    public List<Integer> getManagerSelectedChoice(int empId, int qId, int assId, int manId) {
        return repo.getManagerSelectedChoice(empId,qId,assId,manId);
    }

    @Override
    public List<Integer> getAssessedEmployees(int manId, int assId) {
        return repo.getAssessedEmployees(manId, assId);
    }

    @Override
    public List<Integer> getUnAssessedEmployees(int manId, int assId) {
        return repo.getUnAssessedEmployees(manId, assId);
    }
}
