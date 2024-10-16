package com.example.talent_man.repos.succession;

import com.example.talent_man.controllers.succession.SuccessionPlanResponseDto;
import com.example.talent_man.models.Department;
import com.example.talent_man.models.Position;
import com.example.talent_man.models.succession.SuccessionPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface
SuccessionPlanRepository extends JpaRepository<SuccessionPlan, Integer> {

    boolean existsByDepartmentAndPosition(Department department, Position position);

    @Query(value = "SELECT " +
            "    sp.plan_id AS planId, " +
            "    sp.risk_rating AS riskRating, " +
            "    u.user_id AS currentRoleHolderId, " +
            "    u.user_full_name AS currentRoleHolderName, " +
            "    d.department_id AS departmentId, " +
            "    d.department_name AS departmentName, " +
            "    pos.position_id AS positionId, " +
            "    pos.position_name AS positionName, " +
            "    sd.driver_id AS driverId, " +
            "    sd.driver_name AS driverName, " +
            "    su.user_full_name AS readyUserName, " +
            "    sdn.description AS developmentNeedDescription, " +
            "    sdn.need_type AS developmentNeedType, " +
            "    ru.readiness_level AS readinessLevel, " +
            "    es.contact_info AS externalSuccessorContact, " +
            "    es.current_company AS externalSuccessorCurrentComp, " +
            "    es.current_position AS externalSuccessorPosition, " +
            "    es.name AS externalSuccessorName, " +
            "    es.reason_for_selection AS externalSuccessorSelectionReason, " +
            "    pri.description AS interventionDescription, " +
            "    pri.intervention_type AS interventionType, " +
            "    pri.start_date AS interventionStartDate, " +
            "    pri.end_date AS interventionEndDate, " +
            "    pri.status AS interventionStatus, " +
            "    COUNT(pri.ready_user_id) AS interventionCount " +
            "FROM " +
            "    succession_plan sp " +
            "JOIN " +
            "    users u ON sp.current_role_holder_id = u.user_id " +
            "JOIN " +
            "    department_positions pos ON sp.position_id = pos.position_id " +
            "JOIN " +
            "    company_departments d ON sp.department_id = d.department_id " +
            "JOIN " +
            "    succession_drivers sd ON sp.driver_id = sd.driver_id " +
            "JOIN " +
            "    ready_users ru ON sp.plan_id = ru.succession_plan_id " +
            "LEFT JOIN " +
            "    users su ON ru.user_id = su.user_id " +
            "LEFT JOIN " +
            "    proposed_interventions pri ON ru.id = pri.ready_user_id " +
            "LEFT JOIN " +
            "    successor_development_needs sdn ON ru.id = sdn.ready_user_id " +
            "LEFT JOIN " +
            "    external_successor es ON sp.plan_id = es.succession_plan_id " +
            "GROUP BY " +
            "    sp.plan_id, u.user_id, d.department_id, pos.position_id, sd.driver_id, su.user_full_name, ru.readiness_level, es.name, pri.description, pri.intervention_type",
            nativeQuery = true)
    List<SuccessionPlanProjection> getSuccessionPlanDetails();

    @Query(value = "SELECT " +
            "    sp.plan_id AS planId, " +
            "    sp.risk_rating AS riskRating, " +
            "    u.user_id AS currentRoleHolderId, " +
            "    u.user_full_name AS currentRoleHolderName, " +
            "    d.department_id AS departmentId, " +
            "    d.department_name AS departmentName, " +
            "    pos.position_id AS positionId, " +
            "    pos.position_name AS positionName, " +
            "    sd.driver_id AS driverId, " +
            "    sd.driver_name AS driverName, " +
            "    su.user_full_name AS readyUserName, " +
            "    sdn.description AS developmentNeedDescription, " +
            "    sdn.need_type AS developmentNeedType, " +
            "    ru.readiness_level AS readinessLevel, " +
            "    es.contact_info AS externalSuccessorContact, " +
            "    es.current_company AS externalSuccessorCurrentComp, " +
            "    es.current_position AS externalSuccessorPosition, " +
            "    es.name AS externalSuccessorName, " +
            "    es.reason_for_selection AS externalSuccessorSelectionReason, " +
            "    pri.description AS interventionDescription, " +
            "    pri.intervention_type AS interventionType, " +
            "    pri.start_date AS interventionStartDate, " +
            "    pri.end_date AS interventionEndDate, " +
            "    pri.status AS interventionStatus, " +
            "    COUNT(pri.ready_user_id) AS interventionCount " +
            "FROM " +
            "    succession_plan sp " +
            "JOIN " +
            "    users u ON sp.current_role_holder_id = u.user_id " +
            "JOIN " +
            "    department_positions pos ON sp.position_id = pos.position_id " +
            "JOIN " +
            "    company_departments d ON sp.department_id = d.department_id " +
            "JOIN " +
            "    succession_drivers sd ON sp.driver_id = sd.driver_id " +
            "JOIN " +
            "    ready_users ru ON sp.plan_id = ru.succession_plan_id " +
            "LEFT JOIN " +
            "    users su ON ru.user_id = su.user_id " +
            "LEFT JOIN " +
            "    proposed_interventions pri ON ru.id = pri.ready_user_id " +
            "LEFT JOIN " +
            "    successor_development_needs sdn ON ru.id = sdn.ready_user_id " +
            "LEFT JOIN " +
            "    external_successor es ON sp.plan_id = es.succession_plan_id " +
            "WHERE " +
            "    sp.plan_id = :planId " +
            "GROUP BY " +

            "    sp.plan_id, u.user_id, d.department_id, pos.position_id, sd.driver_id, su.user_full_name, ru.readiness_level, es.name, pri.description, pri.intervention_type",
            nativeQuery = true)
    Optional<List<SuccessionPlanProjection>> getSuccessionPlanById(@Param("planId") int planId);

    @Query(value = "SELECT " +
            "    sp.plan_id AS planId, " +
            "    sp.risk_rating AS riskRating, " +
            "    u.user_id AS currentRoleHolderId, " +
            "    u.user_full_name AS currentRoleHolderName, " +
            "    d.department_id AS departmentId, " +
            "    d.department_name AS departmentName, " +
            "    pos.position_id AS positionId, " +
            "    pos.position_name AS positionName, " +
            "    sd.driver_id AS driverId, " +
            "    sd.driver_name AS driverName, " +
            "    su.user_full_name AS readyUserName, " +
            "    sdn.description AS developmentNeedDescription, " +
            "    sdn.need_type AS developmentNeedType, " +
            "    ru.readiness_level AS readinessLevel, " +
            "    es.contact_info AS externalSuccessorContact, " +
            "    es.current_company AS externalSuccessorCurrentComp, " +
            "    es.current_position AS externalSuccessorPosition, " +
            "    es.name AS externalSuccessorName, " +
            "    es.reason_for_selection AS externalSuccessorSelectionReason, " +
            "    pri.description AS interventionDescription, " +
            "    pri.intervention_type AS interventionType, " +
            "    pri.start_date AS interventionStartDate, " +
            "    pri.end_date AS interventionEndDate, " +
            "    pri.status AS interventionStatus, " +
            "    COUNT(pri.ready_user_id) AS interventionCount " +
            "FROM " +
            "    succession_plan sp " +
            "JOIN " +
            "    users u ON sp.current_role_holder_id = u.user_id " +
            "JOIN " +
            "    department_positions pos ON sp.position_id = pos.position_id " +
            "JOIN " +
            "    company_departments d ON sp.department_id = d.department_id " +
            "JOIN " +
            "    succession_drivers sd ON sp.driver_id = sd.driver_id " +
            "JOIN " +
            "    ready_users ru ON sp.plan_id = ru.succession_plan_id " +
            "LEFT JOIN " +
            "    users su ON ru.user_id = su.user_id " +
            "LEFT JOIN " +
            "    proposed_interventions pri ON ru.id = pri.ready_user_id " +
            "LEFT JOIN " +
            "    successor_development_needs sdn ON ru.id = sdn.ready_user_id " +
            "LEFT JOIN " +
            "    external_successor es ON sp.plan_id = es.succession_plan_id " +
            "WHERE " +
            "    ru.user_id = :userId " +
            "GROUP BY " +

            "    sp.plan_id, u.user_id, d.department_id, pos.position_id, sd.driver_id, su.user_full_name, ru.readiness_level, es.name, pri.description, pri.intervention_type",
            nativeQuery = true)
    List<SuccessionPlanProjection> getSuccessionPlanByUserId(@Param("userId") int userId);

    @Query(value = "SELECT " +
            "    sp.plan_id AS planId, " +
            "    sp.risk_rating AS riskRating, " +
            "    u.user_id AS currentRoleHolderId, " +
            "    u.user_full_name AS currentRoleHolderName, " +
            "    d.department_id AS departmentId, " +
            "    d.department_name AS departmentName, " +
            "    pos.position_id AS positionId, " +
            "    pos.position_name AS positionName, " +
            "    sd.driver_id AS driverId, " +
            "    sd.driver_name AS driverName, " +
            "    su.user_full_name AS readyUserName, " +
            "    sdn.description AS developmentNeedDescription, " +
            "    sdn.need_type AS developmentNeedType, " +
            "    ru.readiness_level AS readinessLevel, " +
            "    es.contact_info AS externalSuccessorContact, " +
            "    es.current_company AS externalSuccessorCurrentComp, " +
            "    es.current_position AS externalSuccessorPosition, " +
            "    es.name AS externalSuccessorName, " +
            "    es.reason_for_selection AS externalSuccessorSelectionReason, " +
            "    pri.description AS interventionDescription, " +
            "    pri.intervention_type AS interventionType, " +
            "    pri.start_date AS interventionStartDate, " +
            "    pri.end_date AS interventionEndDate, " +
            "    pri.status AS interventionStatus, " +
            "    COUNT(pri.ready_user_id) AS interventionCount " +
            "FROM " +
            "    succession_plan sp " +
            "JOIN " +
            "    users u ON sp.current_role_holder_id = u.user_id " +
            "JOIN " +
            "    department_positions pos ON sp.position_id = pos.position_id " +
            "JOIN " +
            "    company_departments d ON sp.department_id = d.department_id " +
            "JOIN " +
            "    succession_drivers sd ON sp.driver_id = sd.driver_id " +
            "JOIN " +
            "    ready_users ru ON sp.plan_id = ru.succession_plan_id " +
            "LEFT JOIN " +
            "    users su ON ru.user_id = su.user_id " +
            "LEFT JOIN " +
            "    proposed_interventions pri ON ru.id = pri.ready_user_id " +
            "LEFT JOIN " +
            "    successor_development_needs sdn ON ru.id = sdn.ready_user_id " +
            "LEFT JOIN " +
            "    external_successor es ON sp.plan_id = es.succession_plan_id " +
            "WHERE " +
            "    sp.position_id = :positionId " +
            "GROUP BY " +

            "    sp.plan_id, u.user_id, d.department_id, pos.position_id, sd.driver_id, su.user_full_name, ru.readiness_level, es.name, pri.description, pri.intervention_type",
            nativeQuery = true)
    List<SuccessionPlanProjection> getSuccessionPlanByPosition(@Param("positionId") int positionId);


    @Query(value = "SELECT \n" +
            "\tspl.plan_id AS planId,\n" +
            "\tra.critical_role_id AS roleId,\n" +
            "\tra.role_name AS roleName,\n" +
            "\tra.current_state AS currentState\n" +
            "FROM " +
            "\t roles_assessment ra\n" +
            "JOIN " +
            "\t succession_plan spl ON ra.role_id = spl.position_id\n" +
            "GROUP BY " +
            "\t ra.critical_role_id", nativeQuery = true)
    List<CriticalRolesMapping> getRolesMapping();

    public interface CriticalRolesMapping{
        int getPlanId();
        Long getRoleId();
        String getRoleName();
        String getCurrentState();
    }
    public interface SuccessionPlanProjection {
        Long getPlanId();
        String getRiskRating();
        Long getCurrentRoleHolderId();
        String getCurrentRoleHolderName();
        Long getDepartmentId();
        String getDepartmentName();
        Long getPositionId();
        String getPositionName();
        Long getDriverId();
        String getDriverName();
        String getReadyUserName();
        String getDevelopmentNeedDescription();
        String getDevelopmentNeedType();
        String getReadinessLevel();
        String getExternalSuccessorContact();
        String getExternalSuccessorCurrentComp();
        String getExternalSuccessorPosition();
        String getExternalSuccessorName();
        String getExternalSuccessorSelectionReason();
        String getInterventionDescription();
        String getInterventionType();
        String getInterventionStatus();
        Date getInterventionStartDate();
        Date getInterventionEndDate();
        Integer getInterventionCount();
    }
}
