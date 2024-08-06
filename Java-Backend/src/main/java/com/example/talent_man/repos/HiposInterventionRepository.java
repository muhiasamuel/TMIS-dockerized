// HiposInterventionRepository.java
package com.example.talent_man.repos;

import com.example.talent_man.models.HiposIntervention;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HiposInterventionRepository extends JpaRepository<HiposIntervention, Long> {

    @Query(value = "SELECT " +
            "hp.employee_id AS userId, " +
            "hp.development_interventions AS developmentInterventions, " +
            "hp.how_to_achieve AS howToAchieve, " +
            "ms.potential_next_role AS potentialNextRoles, " +
            "u.user_full_name AS userFullName " +
            "FROM " +
            "hipos_intervention hp " +
            "LEFT JOIN  users u ON u.user_id = hp.employee_id " +
            "LEFT JOIN mapped_succession ms ON hp.employee_id = ms.employee_id " +
            "WHERE u.manager_id = :managerId " +

            "GROUP BY hp.development_interventions", nativeQuery = true)
    List<HIPOSInterventionInterface> getHIPOSInterventionsByManagerId(int managerId);

    // HIPOS Intervention Interface representing the structure of query result
    interface HIPOSInterventionInterface {
        int getUserId();
        String getDevelopmentInterventions();
        String getHowToAchieve();
        String getPotentialNextRoles();
        String getUserFullName();
    }
}
