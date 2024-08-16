package com.example.talent_man.repos;

import com.example.talent_man.models.PotentialAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PotentialAttributeRepo extends JpaRepository<PotentialAttribute, Integer> {
    List<PotentialAttribute> findByAssessments_AssessmentId(int assessmentId);

}
