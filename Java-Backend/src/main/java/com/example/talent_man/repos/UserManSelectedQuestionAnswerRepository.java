package com.example.talent_man.repos;

import com.example.talent_man.models.answers.UserManSelectedQuestionAnswer;
import com.example.talent_man.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserManSelectedQuestionAnswerRepository extends JpaRepository<UserManSelectedQuestionAnswer, Long> {
    // Additional query methods (if needed) can be defined here
    boolean existsByUserAndAssessmentId(User user, int assessmentId);

    @Query(value = "SELECT DISTINCT u.assessment_question_id FROM user_manager_question_answers u WHERE u.user_id = :userId AND assessment_id = :assessmentId", nativeQuery = true)
    List<Integer> findAssessmentIdsByUserId(@Param("userId") int userId, @Param("assessmentId") List<Integer> assessmentId);





    @Query(value = "SELECT CASE WHEN COUNT(1) > 0 THEN TRUE ELSE FALSE END FROM user_manager_question_answers WHERE assessment_id = :assessmentId AND user_id IN :userIds", nativeQuery = true)
    boolean existsByAssessmentIdAndUserIds(@Param("assessmentId") int assessmentId, @Param("userIds") List<Integer> userIds);


    @Query(value = "SELECT DISTINCT u.assessment_question_id FROM user_manager_question_answers u WHERE u.user_id IN :userIds", nativeQuery = true)
    List<Integer> findAssessmentIdsByUserIds(@Param("userIds") List<Integer> userIds);

    @Query(value = "SELECT c.choice_id AS choiceId, c.choice_value AS choiceValue, c.choice_name AS choiceName " +
            "FROM user_manager_question_answers u " +
            "LEFT JOIN choices c ON u.choice_id = c.choice_id " +
            "WHERE u.assessment_question_id = :questionId AND u.user_id IN :userIds",
            nativeQuery = true)
    List<ChoiceProjection> findSelectedChoiceByQuestionIdAndUserIds(@Param("questionId") int questionId, @Param("userIds") List<Integer> userIds);

    @Query(value = "SELECT c.choice_id AS choiceId, c.choice_value AS choiceValue, c.choice_name AS choiceName " +
            "FROM user_manager_question_answers u " +
            "LEFT JOIN choices c ON u.choice_id = c.choice_id " +
            "WHERE u.assessment_question_id = :questionId AND u.user_id = :userId",
            nativeQuery = true)
    ChoiceProjection findSelectedChoiceByQuestionIdAndUserId(@Param("questionId") int questionId, @Param("userId") int userId);

    public interface ChoiceProjection {
        int getChoiceId();
        int getChoiceValue();
        String getChoiceName();
    }


    @Query(value = "SELECT COUNT(umqa.id) > 0 FROM user_manager_question_answers umqa WHERE umqa.user_id = :userId AND umqa.is_self_assessed = true AND umqa.assessment_id IN " +
            "(SELECT a.assessment_id FROM assessments a WHERE a.end_date > CURRENT_DATE)", nativeQuery = true)
    int isSelfAssessedInActiveAssessment(@Param("userId") int userId);

    @Query(value = "SELECT COUNT(umqa.id) > 0 " +
            "FROM user_manager_question_answers umqa " +
            "LEFT JOIN users u ON umqa.user_id = u.user_id " +
            "JOIN users m ON u.manager_id = m.user_id " +
            "WHERE umqa.user_id = :userId " +
            "AND umqa.is_manager_assessed = true " +
            "AND m.user_id = :managerId " +
            "AND umqa.assessment_id IN (" +
            "  SELECT a.assessment_id " +
            "  FROM assessments a " +
            "  WHERE a.end_date > CURRENT_DATE)",
            nativeQuery = true)
    int isManagerAssessedInActiveAssessment(@Param("userId") int userId, @Param("managerId") int managerId);





}
