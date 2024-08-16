package com.example.talent_man.repos;

import com.example.talent_man.models.answers.AverageScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AverageScoreRepo extends JpaRepository<AverageScore, Integer> {
    // Custom query methods can be added here if needed
}
