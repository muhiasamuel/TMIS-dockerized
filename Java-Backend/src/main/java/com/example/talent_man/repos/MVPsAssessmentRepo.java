package com.example.talent_man.repos;

import com.example.talent_man.models.MVPsAssessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MVPsAssessmentRepo extends JpaRepository<MVPsAssessment, Long> {

    @Query(value = "SELECT " +
            "ma.employee_id AS employeeId, " +
            "ma.career_priorities AS careerPriority, " +
            "ma.impact_of_attrition AS impactOfAttrition, " +
            "ma.market_exposure AS marketExposure, " +
            "ma.retention_assessment_state AS retentionState, " +
            "u.user_full_name AS userFullName, " +
            "mrs.development_interventions AS retentionStrategy " +
            "FROM mvps_assessment ma " +
            "LEFT JOIN mvps_retention_strategies mrs ON ma.employee_id = mrs.employee_id " +
            "LEFT JOIN users u ON u.user_id = ma.employee_id " +
            "WHERE u.manager_id = :managerId " +
            "GROUP BY mrs.id", nativeQuery = true)
    List<MVPSAssessmentInterface> getMVPSAssessmentByManagerId(int managerId);

    interface MVPSAssessmentInterface {
        int getEmployeeId();
        @Nullable
        String getCareerPriority();
        @Nullable
        String getImpactOfAttrition();
        @Nullable
        String getMarketExposure();
        @Nullable
        String getRetentionState();
        @Nullable
        String getUserFullName();
        @Nullable
        String getRetentionStrategy();
    }
}
