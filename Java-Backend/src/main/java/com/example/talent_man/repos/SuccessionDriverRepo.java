package com.example.talent_man.repos;

import com.example.talent_man.models.succession.SuccessionDrivers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SuccessionDriverRepo extends JpaRepository<SuccessionDrivers, Integer> {

}
