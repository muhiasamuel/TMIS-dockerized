package com.example.talent_man.repos;

import com.example.talent_man.models.Assessment;
import com.example.talent_man.models.AssessmentQuestion;
import com.example.talent_man.models.PotentialAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssessmentQuestionsRepo extends JpaRepository<AssessmentQuestion, Integer> {
    List<AssessmentQuestion> findByPotentialAttributeIn(List<PotentialAttribute> potentialAttributes);

}
