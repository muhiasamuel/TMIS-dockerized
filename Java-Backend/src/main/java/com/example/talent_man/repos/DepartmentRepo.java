package com.example.talent_man.repos;

import com.example.talent_man.models.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface DepartmentRepo extends JpaRepository<Department, Integer> {
}
