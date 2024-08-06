package com.example.talent_man.service_imp;

import com.example.talent_man.models.SkillsAsssessment;
import com.example.talent_man.models.user.Manager;
import com.example.talent_man.repos.CriticalSkillsAssessmentRepo;
import com.example.talent_man.repos.user.ManagerRepo;
import com.example.talent_man.services.SkillAssessmentService;
import com.example.talent_man.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;


@Service
public class CriticalSkillsServiceImpl implements SkillAssessmentService {

    @Autowired
    UserService managerService;

    @Autowired
    ManagerRepo managerRepo;
    @Autowired
    CriticalSkillsAssessmentRepo skillsRepo;
    @Override
    public Set<SkillsAsssessment> getAddedSkills(int managerId) {
        try {
            Manager manager = managerService.getManagerById(managerId);
            if (manager == null) {
                throw new RuntimeException("Manager with id " + managerId + " does not exist");
            }
            Set<SkillsAsssessment> skillsAssessments = manager.getSkillsAssessments();
            if (skillsAssessments.isEmpty()) {
                throw new RuntimeException("No previous assessments found for manager with id " + managerId);
            }
            return skillsAssessments;
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while getting skills assessments: " + e.getMessage(), e);
        }
    }

    @Override
    public SkillsAsssessment getSkillById(Long skillId) {
        Optional<SkillsAsssessment> skillOptional = skillsRepo.findById(skillId);
        if (skillOptional.isEmpty()) {
            throw new RuntimeException("Skill not found with ID: " + skillId);
        }
        return skillOptional.get();
    }

    //Editing one skill
    @Override
    public SkillsAsssessment updateSkill(Long skillId, int userId, SkillsAsssessment updatedSkill) {
        // Check if the skill exists
        SkillsAsssessment skill = skillsRepo.findById(skillId)
                .orElseThrow(() -> new RuntimeException("Skill not found with ID: " + skillId));
        // Check if any field in the updatedSkill is null or empty

        Manager manager = managerRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        if (!skill.getAddedBy().equals(manager)) {
            throw new IllegalArgumentException("You are not authorized to update this skill");
        }

        if (isAnyFieldEmpty(updatedSkill)) {
            throw new IllegalArgumentException("One or more fields in the updated skill data are empty");
        }
        // Update the existing skill with the new data
        skill.setSkillName(updatedSkill.getSkillName());
        skill.setSkillDescription(updatedSkill.getSkillDescription());
        skill.setCurrentSkillState(updatedSkill.getCurrentSkillState());
        skill.setSkillDevelopmentStrategy(updatedSkill.getSkillDevelopmentStrategy());
        skill.setFutureMarketAndTechRelevance(updatedSkill.getFutureMarketAndTechRelevance());
        skill.setBusinessPriority(updatedSkill.getBusinessPriority());
        skill.setMarketFluidity(updatedSkill.getMarketFluidity());
        skill.setDevelopmentCostAndTimeCommitment(updatedSkill.getDevelopmentCostAndTimeCommitment());
        skill.setScarcityParameter(updatedSkill.getScarcityParameter());
        skill.setAverageRating(updatedSkill.getAverageRating());
        // Update other fields as needed

        // Save and return the updated skill
        return skillsRepo.save(skill);
    }

    //checking if fields are empty
    private boolean isAnyFieldEmpty(SkillsAsssessment skill) {
        return skill.getSkillName() == null || skill.getSkillName().isEmpty() ||
            skill.getCurrentSkillState() == null || skill.getCurrentSkillState().isEmpty()||
            skill.getAverageRating() == null || skill.getAverageRating().isEmpty()||
            skill.getBusinessPriority() == null || skill.getBusinessPriority().isEmpty()||
            skill.getDevelopmentCostAndTimeCommitment() == null || skill.getDevelopmentCostAndTimeCommitment().isEmpty()||
            skill.getMarketFluidity() == null || skill.getMarketFluidity().isEmpty()||
            skill.getScarcityParameter() ==null || skill.getScarcityParameter().isEmpty()||
            skill.getFutureMarketAndTechRelevance() == null || skill.getFutureMarketAndTechRelevance().isEmpty();
        // Add conditions for other fields as needed
    }
}