package com.example.talent_man.repos;

import com.example.talent_man.models.Assessment;
import com.example.talent_man.models.AssessmentQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssessmentQuestionsRepo extends JpaRepository<AssessmentQuestion, Integer> {
}
