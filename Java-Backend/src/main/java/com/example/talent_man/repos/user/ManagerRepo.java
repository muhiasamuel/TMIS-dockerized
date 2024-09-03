package com.example.talent_man.repos.user;

import com.example.talent_man.models.user.Employee;
import com.example.talent_man.models.user.Manager;
import com.example.talent_man.models.user.User;
import com.example.talent_man.models.user.UserDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface ManagerRepo extends JpaRepository<Manager, Integer> {
    Manager findByUsername(String username);
    @Query(value = "select * from users where role_id = '1'", nativeQuery = true)
    List<Manager> findByAllUserType();

    @Query(value = "select * from users where manager_id = :manId", nativeQuery = true)
    List<User> findMangerEmployees(@Param("manId") int manId);

    @Query(value = "select distinct user_id from users where manager_id = :manId", nativeQuery = true)
    List<Integer> getManagerEmployees(@Param("manId") int manId);


}
