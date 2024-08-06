package com.example.talent_man.services;

import com.example.talent_man.models.SkillsAsssessment;

import java.util.Set;

public interface SkillAssessmentService {

    Set<SkillsAsssessment> getAddedSkills(int managerId);
    SkillsAsssessment getSkillById(Long skillId);

    SkillsAsssessment updateSkill(Long skillId,int useId, SkillsAsssessment updatedSkill);


}
