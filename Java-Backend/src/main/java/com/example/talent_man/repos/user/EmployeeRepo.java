package com.example.talent_man.repos.user;

import com.example.talent_man.models.user.Employee;
import com.example.talent_man.models.user.Manager;
import com.example.talent_man.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface EmployeeRepo extends JpaRepository<Employee, Integer> {
   Employee findByUsername(String username);
   List<User> findByManager(Manager man);

   @Query(value = "select * from users where role_id = '3'", nativeQuery = true)
   List<Employee> findByAllEmployees();
}
