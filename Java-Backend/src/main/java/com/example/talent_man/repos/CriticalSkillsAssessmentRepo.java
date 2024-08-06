package com.example.talent_man.repos;

import com.example.talent_man.models.SkillsAsssessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CriticalSkillsAssessmentRepo extends JpaRepository<SkillsAsssessment, Long> {

}
