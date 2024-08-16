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

    @Query(value = "SELECT DISTINCT u.assessment_question_id FROM user_manager_question_answers u WHERE u.user_id = :userId", nativeQuery = true)
    List<Integer> findAssessmentIdsByUserId(@Param("userId") int userId);

}
