package com.example.talent_man.repos;

import com.example.talent_man.models.RolesAssessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CriticalRolesAssessmentRepo extends JpaRepository<RolesAssessment, Long> {

//    SELECT ra.critical_role_id as criticalRoleId, ra.impact_on_operation as impactOnOperation, ra.risk_impact as riskImpact, ra.role_name as roleName, ra.skill_experience as skillExperience, ra.vacancy_risk as vacancyRisk, ra.average_rating as averageRating, ra.strategic_importance as strategicImportance, ra.current_state as currentState FROM roles_assessment ra;
    @Query(value = "SELECT ra.critical_role_id as criticalRoleId," +
            " ra.impact_on_operation as impactOnOperation," +
            " ra.risk_impact as riskImpact, " +
            "ra.role_name as roleName, " +
            "ra.skill_experience as skillExperience, " +
            "ra.vacancy_risk as vacancyRisk, " +
            "ra.average_rating as averageRating, " +
            "ra.strategic_importance as strategicImportance, " +
            "ra.current_state as currentState, " +
            "c.strategy_name as strategyName " +
            "FROM critical_roles_strategies c left join roles_assessment ra on c.role_assessment_id = " +
            "ra.critical_role_id", nativeQuery = true)
    List<RolesAssessmentInterface> findAllDetails();
     interface RolesAssessmentInterface{
         Long getCriticalRoleId();
         String getImpactOnOperation();
         String getRiskImpact();
         String getRoleName();
         String getSkillExperience();
         String getStrategicImportance();
         String getVacancyRisk();
         String getStrategyName();

         String getCurrentState();
         Double getAverageRating();

     }
}
