package com.example.talent_man.repos;

import com.example.talent_man.models.CriticalRolesStrategies;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolesStrategiesRepo extends JpaRepository<CriticalRolesStrategies, Long> {
    // You can define custom query methods here if needed
}
