package com.example.talent_man.repos;

import com.example.talent_man.models.UserQuestionAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserQuestionAnswerRepo extends JpaRepository<UserQuestionAnswer, Integer> {
  //  @Query(value="select * from user_question_answers where ass_id = :assId and user_id = :userId", nativeQuery = true)
  //  List<UserQuestionAnswer> answeredAssess(int assId, int userId);
  @Query(value = "select * from user_question_answers", nativeQuery = true)
  List<UserQuestionAnswer> answeredAssess(int assId, int userId);

  @Query(value = "select distinct assessment_id from user_question_answers where manager_id=:manId", nativeQuery = true)
  List<Integer> managerDoneAssess(@Param("manId") int managerId);

  @Query(value = "select distinct assessment_id from user_question_answers where employee_id=:empId", nativeQuery = true)
  List<Integer> userDoneAssessments(@Param("empId") int employeeId);

  @Query(value = "select distinct assessment_id from user_question_answers where assessment_status='a' and manager_id=:manId", nativeQuery = true)
  List<Integer> getManagerAssessedAssess(@Param("manId") int manId);

  @Query(value = "select distinct assessment_id from user_question_answers where assessment_status='u' and manager_id=:manId", nativeQuery = true)
  List<Integer> getManagerUnAssessedAssess(@Param("manId") int manId);

  @Query(value = "select distinct assessment_id from user_question_answers where assessment_status='a' and manager_id=:manId and employee_id=:empId", nativeQuery = true)
  List<Integer> getUserAssessedAssess(@Param("manId") int manId, @Param("empId") int empId);

  @Query(value = "select distinct employee_id from user_question_answers where assessment_id= :assId and manager_id=:managerId", nativeQuery = true)
  List<Integer> userDoneAssess(@Param("assId") int assId, @Param("managerId") int managerId);

  @Query(value = "select distinct question_id from user_question_answers where assessment_id= :assId and employee_id=:employeeId", nativeQuery = true)
  List<Integer> userDoneAssessQuestions(@Param("assId") int assId, @Param("employeeId") int employeeId);

  //getting users selected answer for a question
  @Query(value = "select distinct user_selected_choice_id from user_question_answers where employee_id=:empId and question_id=:qId and assessment_id=:assId", nativeQuery = true)
  List<Integer> getEmpSelectedChoice(@Param("empId") int empId, @Param("qId") int qId, @Param("assId") int assId);

  //getting the manager selected choice for a particular question.
  @Query(value = "select distinct manager_selected_choice_id from user_question_answers where employee_id=:empId and question_id=:qId and assessment_id=:assId and manager_id=:manId", nativeQuery = true)
  List<Integer> getManagerSelectedChoice(@Param("empId") int empId, @Param("qId") int qId, @Param("assId") int assId, @Param("manId") int manId);
  @Query(value = "update user_question_answers set manager_selected_choice_id=:managerChoiceId where manager_id=:manId and question_id=:qId and assessment_id=:assId and employee_id=:empId", nativeQuery = true)
  void managerUpdateChoiceSelected(@Param("assId") int assId, @Param("manId") int manId, @Param("qId") int questionId, @Param("managerChoiceId") int selectedManagerChoice, @Param("empId") int empId);

  @Query(value = "select * from user_question_answers where manager_id=:manId and question_id=:qId and assessment_id=:assId and employee_id=:empId", nativeQuery = true)
  List<UserQuestionAnswer> getUserQuestionAnswer(@Param("assId") int assId, @Param("manId") int manId, @Param("qId") int questionId, @Param("empId") int empId);


  //getting all assessed employees
  @Query(value = "select distinct employee_id from user_question_answers where manager_id=:manId and assessment_id=:assId and assessment_status='a'", nativeQuery = true)
  List<Integer> getAssessedEmployees(@Param("manId") int manId,@Param("assId") int assId);

  //getting all assessed employees
  @Query(value = "select distinct employee_id from user_question_answers where manager_id=:manId and assessment_id=:assId and assessment_status='u'", nativeQuery = true)
  List<Integer> getUnAssessedEmployees(@Param("manId") int manId,@Param("assId") int assId);


}