package com.example.talent_man.repos;


import com.example.talent_man.dto.HIPOsFullDataDto;
import com.example.talent_man.models.Performance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;


import java.util.Date;
import java.util.List;

@Repository
public interface PerformanceRepository extends JpaRepository<Performance, Long> {


    @Query(value = "SELECT " +
            "    u.user_id AS userId, " +
            "    u.username AS userName, " +
            "    u.user_full_name AS userFullName, " +
            "    u.manager_id AS managerId, " +
            "    u.role_id AS roleId, " +
            "    AVG(p.performance_metric) AS averagePerformance, " +
            "    c.choice_value AS choiceValue, " +
            "    AVG(c.choice_value) AS userAssessmentAvg, " +
            "    cm.choice_value AS manChoiceValue, " +
            "    AVG(cm.choice_value) AS manAssessmentAvg, " +
            "    a.assessment_name AS assessmentName, " +
            "    pa.potential_attribute_name AS potentialAttributeName, " +
            "    p.year AS performanceYear " +
            "FROM " +
            "    users u " +
            "LEFT JOIN " +
            "    performance p ON u.user_id = p.user_id " +
            "LEFT JOIN " +
            "    user_question_answers uqa ON u.user_id = uqa.employee_id " +
            "LEFT JOIN " +
            "    choices c ON uqa.user_selected_choice_id = c.choice_id " +
            "LEFT JOIN " +
            "    choices cm ON uqa.manager_selected_choice_id = cm.choice_id " +
            "LEFT JOIN " +
            "    assessments a ON uqa.assessment_id = a.assessment_id " +
            "LEFT JOIN " +
            "    potential_attributes pa ON a.potential_attribute_id = pa.potential_attribute_id " +
            "WHERE " +
            "    uqa.manager_id = :managerId " +
            "GROUP BY " +
            "   u.user_id, u.username, u.manager_id, u.role_id, pa.potential_attribute_id, pa.potential_attribute_name",nativeQuery = true)
    List<TalentInterface> getUserPerformancesByManagerId(@Param("managerId") int managerId);


    @Query( value = "SELECT " +
            "    p.user_id AS userId, " +
            "    u.user_full_name AS userFullName, " +
            "    p.year AS performanceYear, " +
            "    AVG(p.performance_metric) AS performanceRating, " +
            "    lp.averagePerformanceLastThreeYears AS averagePerformanceLastThreeYears, " +
            "    lp.yearsCount AS yearsCount " +
            "FROM " +
            "    performance p " +
            "LEFT JOIN " +
            "    users u ON u.user_id = p.user_id " +
            "LEFT JOIN ( " +
            "    SELECT " +
            "        user_id, " +
            "        AVG(performance_metric) AS averagePerformanceLastThreeYears, " +
            "        COUNT(DISTINCT year) AS yearsCount " +
            "    FROM " +
            "        performance " +
            "    WHERE " +
            "        year >= YEAR(CURRENT_DATE) - 2 " +
            "    GROUP BY " +
            "        user_id " +
            ") lp ON p.user_id = lp.user_id " +
            "WHERE " +
            "    u.user_id = :employeeId " +
            "GROUP BY " +
            "    p.user_id, " +
            "    p.year, " +
            "    lp.averagePerformanceLastThreeYears, " +
            "    lp.yearsCount", nativeQuery = true)
    List<performanceInterface> getPerformanceByEmployeeId(@Param("employeeId") int employeeId);

    @Query(value = "SELECT " +
            "    u.user_id AS userId, " +
            "    u.username AS userName, " +
            "    u.user_full_name AS userFullName, " +
            "    u.manager_id AS managerId, " +
            "    u.role_id AS roleId, " +
            "    AVG(p.performance_metric) AS averagePerformance, " +
            "    c.choice_value AS choiceValue, " +
            "    AVG(c.choice_value) AS userAssessmentAvg, " +
            "    cm.choice_value AS manChoiceValue, " +
            "    AVG(cm.choice_value) AS manAssessmentAvg, " +
            "    a.assessment_name AS assessmentName, " +
            "    pa.potential_attribute_name AS potentialAttributeName, " +
            "    p.year AS performanceYear " +
            "FROM " +
            "    users u " +
            "LEFT JOIN " +
            "    performance p ON u.user_id = p.user_id " +
            "LEFT JOIN " +
            "    user_question_answers uqa ON u.user_id = uqa.employee_id " +
            "LEFT JOIN " +
            "    choices c ON uqa.user_selected_choice_id = c.choice_id " +
            "LEFT JOIN " +
            "    choices cm ON uqa.manager_selected_choice_id = cm.choice_id " +
            "LEFT JOIN " +
            "    assessments a ON uqa.assessment_id = a.assessment_id " +
            "LEFT JOIN " +
            "    potential_attributes pa ON a.potential_attribute_id = pa.potential_attribute_id " +

            "GROUP BY " +
            "    pa.potential_attribute_id, pa.potential_attribute_name",nativeQuery = true)
    List<TalentInterface> getAllUserPerformances();

    @Query(value = "SELECT main.userId, main.userName, main.userFullName, main.potentialAttribute, main.averagePerformance, " +
            "main.userAssessmentAvg, main.manAssessmentAvg, main.a, main.assessmentDate, main.performanceYear, " +
            "main.userClass, main.averageManAssessmentAvg, SUM(main.manAssessmentAvg) OVER (PARTITION BY main.userId) AS potentialRatingSum, " +
            "AVG(main.manAssessmentAvg) OVER (PARTITION BY main.userId) AS potentialRating " +
            "FROM (SELECT u.user_id AS userId, u.user_full_name as userFullName, u.username AS userName, pa.potential_attribute_name AS potentialAttribute, " +
            "AVG(p.performance_metric) AS averagePerformance, AVG(c.choice_value) AS userAssessmentAvg, " +
            "AVG(cm.choice_value) AS manAssessmentAvg, AVG(cm.choice_value + c.choice_value) AS a, a.date_created AS assessmentDate, " +
            "p.year AS performanceYear, CASE WHEN (AVG(cm.choice_value + c.choice_value)) >= 14 THEN 'Class A' " +
            "WHEN (AVG(cm.choice_value + c.choice_value)) < 14 AND (AVG(cm.choice_value + c.choice_value)) >= 12 THEN 'Class B' " +
            "WHEN (AVG(cm.choice_value + c.choice_value)) < 12 AND (AVG(cm.choice_value + c.choice_value)) >= 10 THEN 'Class C' " +
            "WHEN AVG(cm.choice_value) BETWEEN 4 AND 6 AND AVG(p.performance_metric) BETWEEN 4 AND 5 THEN 'Class A' ELSE 'Other' END AS userClass, " +
            "AVG(cm.choice_value) AS averageManAssessmentAvg FROM users u LEFT JOIN performance p ON u.user_id = p.user_id " +
            "LEFT JOIN user_question_answers uqa ON u.user_id = uqa.employee_id LEFT JOIN choices c ON uqa.user_selected_choice_id = c.choice_id " +
            "LEFT JOIN choices cm ON uqa.manager_selected_choice_id = cm.choice_id LEFT JOIN assessments a ON uqa.assessment_id = a.assessment_id " +
            "LEFT JOIN potential_attributes pa ON a.potential_attribute_id = pa.potential_attribute_id WHERE uqa.manager_id = :managerId " +
            " GROUP BY u.user_id, pa.potential_attribute_name,  a.date_created) AS main " +
            "ORDER BY main.performanceYear, main.assessmentDate", nativeQuery = true)
    List<UserPerformanceData> getAllUserHIPOs(int managerId);

    @Query(value = "SELECT main.userId, main.userName, main.potentialAttribute, main.averagePerformance, " +
            "main.userAssessmentAvg, main.manAssessmentAvg, main.a, main.assessmentDate, main.performanceYear, " +
            "main.userClass, main.averageManAssessmentAvg, " +
            "SUM(main.manAssessmentAvg) OVER (PARTITION BY main.userId) AS potentialRatingSum, " +
            "AVG(main.manAssessmentAvg) OVER (PARTITION BY main.userId) AS potentialRating " +
            "FROM (SELECT u.user_id AS userId, u.username AS userName, pa.potential_attribute_name AS potentialAttribute, " +
            "AVG(p.performance_metric) AS averagePerformance, AVG(c.choice_value) AS userAssessmentAvg, " +
            "AVG(cm.choice_value) AS manAssessmentAvg, AVG(cm.choice_value + c.choice_value) AS a, a.date_created AS assessmentDate, " +
            "p.year AS performanceYear, " +

            "CASE WHEN (AVG(cm.choice_value + c.choice_value)) >= 14 THEN 'Class A' " +
            "WHEN (AVG(cm.choice_value + c.choice_value)) < 14 AND (AVG(cm.choice_value + c.choice_value)) >= 12 THEN 'Class B' " +
            "WHEN (AVG(cm.choice_value + c.choice_value)) < 12 AND (AVG(cm.choice_value + c.choice_value)) >= 10 THEN 'Class C' " +
            "WHEN AVG(cm.choice_value) BETWEEN 4 AND 6 AND AVG(p.performance_metric) BETWEEN 4 AND 5 THEN 'Class A' " +
            "ELSE 'Other' END AS userClass, " +
            "AVG(cm.choice_value) AS averageManAssessmentAvg FROM users u " +
            "LEFT JOIN performance p ON u.user_id = p.user_id " +
            "LEFT JOIN user_question_answers uqa ON u.user_id = uqa.employee_id " +
            "LEFT JOIN choices c ON uqa.user_selected_choice_id = c.choice_id " +
            "LEFT JOIN choices cm ON uqa.manager_selected_choice_id = cm.choice_id " +
            "LEFT JOIN assessments a ON uqa.assessment_id = a.assessment_id " +
            "LEFT JOIN potential_attributes pa ON a.potential_attribute_id = pa.potential_attribute_id " +
            "WHERE uqa.manager_id = :managerId  AND p.year = :year AND YEAR(a.date_created) = :year " +
            "GROUP BY u.user_id, pa.potential_attribute_name, p.year, a.date_created) AS main " +
            "ORDER BY main.performanceYear, main.assessmentDate", nativeQuery = true)
    List<UserPerformanceData> getAllUserHIPOsByYear(int managerId, int year);

    @Query( value = "SELECT " +
            "    p.user_id AS userId, " +
            "    u.user_full_name AS userFullName, " +
            "    p.year AS performanceYear, " +
            "    AVG(p.performance_metric) AS performanceRating, " +
            "    lp.averagePerformanceLastThreeYears AS averagePerformanceLastThreeYears, " +
            "    lp.yearsCount AS yearsCount " +
            "FROM " +
            "    performance p " +
            "LEFT JOIN " +
            "    users u ON u.user_id = p.user_id " +
            "LEFT JOIN ( " +
            "    SELECT " +
            "        user_id, " +
            "        AVG(performance_metric) AS averagePerformanceLastThreeYears, " +
            "        COUNT(DISTINCT year) AS yearsCount " +
            "    FROM " +
            "        performance " +
            "    WHERE " +
            "        year >= YEAR(CURRENT_DATE) - 2 " +
            "    GROUP BY " +
            "        user_id " +
            ") lp ON p.user_id = lp.user_id " +
            "WHERE " +
            "    u.manager_id = :managerId " +
            "GROUP BY " +
            "    p.user_id, " +
            "    p.year, " +
            "    lp.averagePerformanceLastThreeYears, " +
            "    lp.yearsCount", nativeQuery = true)
    List <performanceInterface> getAllUsersPerformances(int managerId);

    interface performanceInterface{
        int getUserId();
        String getUserFullName();

        int getYearsCount();
        @Nullable
        Double getPerformanceRating();
        @Nullable
        Integer getPerformanceYear();
        @Nullable
        Double getAveragePerformanceLastThreeYears();
    }

    interface TalentInterface{
        
            int getUserId();
            String getUserName();
            String getUserFullName();
            @Nullable
            Integer getManagerId();
            Long getRoleId();
            @Nullable
            Double getAveragePerformance();
            @Nullable
            String getChoiceValue();
            @Nullable
            Double getUserAssessmentAvg();
            @Nullable
            String getManChoiceValue();
            @Nullable
            Integer getPerformanceYear();
            @Nullable
            Double getManAssessmentAvg();
            @Nullable
            String getAssessmentName();
            @Nullable
            String getPotentialAttributeName();

        }

    public interface UserPerformanceData {
        int getUserId();
        String getUsername();

        String getUserFullName();
        @Nullable

        String getPotentialAttribute();
        @Nullable
        Double getAveragePerformance();
        @Nullable
        Double getUserAssessmentAvg();
        @Nullable
        Double getManAssessmentAvg();
        @Nullable
        Integer geta();
        @Nullable
        Date getAssessmentDate();

        @Nullable
        Integer getPerformanceYear();
        @Nullable
        String getUserClass();
        @Nullable
        Double getAverageManAssessmentAvg();
        @Nullable
        Double getPotentialRatingSum();
        @Nullable
        Double getPotentialRating();

    }
    public interface UserHIPOsData {
        int getUserId();
        String getUsername();

        String getUserFullName();
        @Nullable

        String getPotentialAttribute();
        @Nullable
        Double getAveragePerformance();
        @Nullable
        Double getUserAssessmentAvg();
        @Nullable
        Double getManAssessmentAvg();
        @Nullable
        Integer geta();
        @Nullable
        Date getAssessmentDate();

        @Nullable
        Integer getPerformanceYear();
        @Nullable
        String getUserClass();
        @Nullable
        Double getAverageManAssessmentAvg();
        @Nullable
        Double getPotentialRatingSum();
        @Nullable
        Double getPotentialRating();
        @Nullable
        Double getDevelopmentInterventions();
        @Nullable
        Double getHowToAchieve();
        @Nullable
        Double getNextPotentialRole();

    }
}
