package com.example.talent_man.repos;

import com.example.talent_man.models.MVPsAssessment;
import com.example.talent_man.models.MVPsRetentionStrategies;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MVPsInterventionRepo extends JpaRepository<MVPsRetentionStrategies,Long> {
}
