package com.example.talent_man.repos.succession;

import com.example.talent_man.models.succession.ProposedIntervention;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProposedInterventionRepository extends JpaRepository<ProposedIntervention, Integer> {
    // You can define custom query methods here if needed
}