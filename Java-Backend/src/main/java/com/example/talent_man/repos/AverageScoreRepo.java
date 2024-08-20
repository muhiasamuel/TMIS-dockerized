package com.example.talent_man.repos;

import com.example.talent_man.models.answers.AverageScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AverageScoreRepo extends JpaRepository<AverageScore, Integer> {
    // Custom query methods can be added here if needed
    List<AverageScore> findByUserUserIdAndAssessmentType(int userId, AverageScore.AssessmentType assessmentType);

}
