package com.example.talent_man.repos;

import com.example.talent_man.dto.HIPOsFullDataDto;
import com.example.talent_man.models.MappedSuccession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HipoNextPotentialRolesRepo extends JpaRepository<MappedSuccession, Long> {

    @Query(value = "SELECT NEW com.example.talent_man.dto.HIPOsFullDataDto(" +
            "main.userId, main.userName, main.userFullName, main.potentialAttribute, main.averagePerformance, " +
            "main.userAssessmentAvg, main.manAssessmentAvg, main.assessmentDate, main.performanceYear, " +
            "main.averageManAssessmentAvg, main.nextPotentialRole, main.developmentInterventions, main.howToAchieve, " +
            "main.potentialRatingSum, main.potentialRating) " +
            "FROM (SELECT u.user_id AS userId, u.user_full_name AS userFullName, u.username AS userName, " +
            "pa.potential_attribute_name AS potentialAttribute, AVG(p.performance_metric) AS averagePerformance, " +
            "AVG(c.choice_value) AS userAssessmentAvg, AVG(cm.choice_value) AS manAssessmentAvg, " +
            "AVG(cm.choice_value + c.choice_value) AS a, a.date_created AS assessmentDate, " +
            "p.year AS performanceYear, ms.potential_next_role AS nextPotentialRole, " +
            "hi.development_interventions AS developmentInterventions, hi.how_to_achieve AS howToAchieve, " +
            "SUM(main.manAssessmentAvg) OVER (PARTITION BY u.user_id) AS potentialRatingSum, " +
            "AVG(main.manAssessmentAvg) OVER (PARTITION BY u.user_id) AS potentialRating " +
            "FROM User u " +
            "LEFT JOIN u.performance p " +
            "LEFT JOIN u.userQuestionAnswers uqa " +
            "LEFT JOIN uqa.userSelectedChoice c " +
            "LEFT JOIN uqa.managerSelectedChoice cm " +
            "LEFT JOIN uqa.assessment a " +
            "LEFT JOIN a.potentialAttribute pa " +
            "LEFT JOIN u.mappedSuccession ms " +
            "LEFT JOIN u.hiposIntervention hi " +
            "WHERE uqa.managerId = :managerId " +
            "GROUP BY u.userId, hi.developmentInterventions, pa.potentialAttributeName, a.dateCreated) AS main " +
            "ORDER BY main.performanceYear, main.assessmentDate", nativeQuery = true)
    List<HIPOsFullDataDto> getAllHIPOsData(int managerId);




}
