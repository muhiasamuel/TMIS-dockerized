package com.example.talent_man.repos.user;

import com.example.talent_man.models.user.User;
import com.example.talent_man.models.user.UserDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {
    User findByUsername(String username);

    Optional<User> findByPf(String pf);

    User findFirstByEmail(String email);

    @Query(value = "SELECT * FROM users u WHERE u.manager_id = :managerId", nativeQuery = true)
    List<User> findUsersByManagerId(@Param("managerId") int managerId);
    @Query(value = "SELECT u.user_id FROM users u WHERE u.manager_id = :managerId", nativeQuery = true)
    List<Integer> findUserIdsByManagerId(@Param("managerId") int managerId);
    @Query(value = "SELECT " +
            "u.user_full_name AS userFullName, " +
            "u.user_type AS employeeLevel, " +
            "u.pf_no AS employeePf, " +
            "u.email AS employeeEmail, " +
            "dp.position_name AS positionName, " +
            "cd.department_name AS departmentName, " +
            "m.user_full_name AS managerFullName, " +
            "m.user_type AS managerLevel, " +
            "m.pf_no AS managerPf, " +
            "m.email AS managerEmail, " +
            "mdp.position_name AS managerPositionName, " +
            "mcd.department_name AS managerDepartmentName " +
            "FROM users u " +
            "LEFT JOIN department_positions dp ON u.position_id = dp.position_id " +
            "LEFT JOIN company_departments cd ON dp.department_id = cd.department_id " +
            "LEFT JOIN users m ON u.manager_id = m.user_id " +
            "LEFT JOIN department_positions mdp ON m.position_id = mdp.position_id " +
            "LEFT JOIN company_departments mcd ON mdp.department_id = mcd.department_id " +
            "WHERE u.user_id = :employeeId", nativeQuery = true
    )
    UserWithManagerDetails getUserWithManagerDetails(@Param("employeeId") int employeeId);

    @Query(value = "select * from users where position_id = :positionId", nativeQuery = true)
    List<User> getUserByPosition(@Param("positionId") int positionId);
    public interface UserWithManagerDetails {
        String getUserFullName();
        String getEmployeeLevel();
        String getEmployeePf();
        String getEmployeeEmail();
        String getPositionName();
        String getDepartmentName();
        String getManagerFullName();
        String getManagerLevel();
        String getManagerPf();
        String getManagerEmail();
        String getManagerPositionName();
        String getManagerDepartmentName();
    }
}
