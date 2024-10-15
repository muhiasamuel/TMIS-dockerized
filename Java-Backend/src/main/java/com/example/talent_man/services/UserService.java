package com.example.talent_man.services;

import com.example.talent_man.dto.user.*;
import com.example.talent_man.models.user.Employee;
import com.example.talent_man.models.user.Manager;
import com.example.talent_man.models.user.User;

import com.example.talent_man.models.user.UserDTO;
import com.example.talent_man.repos.PerformanceRepository;
import com.example.talent_man.repos.user.UserRepo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface UserService {
    //Create
    User addUser(User user);
    User getByUsername(String username);
    //Read
    Manager getManagerById(int id);

     Manager save(Manager manager);
    Manager getManagerByUserName(String username);
    Employee getEmployeeByUserName(String username);

    List<Manager> findAllManagers();
    List<Employee> findAllEmployees();

    List<UserDTO> findAllUsers();

    User getUserById(int id);

    UserRepo.UserWithManagerDetails getUserPerformancesByManagerId(int employeeId);
    List<User> getManagerEmployees(Manager manId);
    List<Integer> getManEmployees(int manId);

    User createEmployee(UserRequestDto userDto, int managerId);

    List<UserDetailsDto> getAllUserDetails();

    User createTopManager(UserRequestDto userDto);


    List<User> createEmployees(List<UserRequestDto> userDtos, int managerId);
    List<User> uploadEmployees(MultipartFile file, int managerId);
    User createManager(UserRequestDto userDto, int managerId);
    // Method to update an employee
    User updateEmployee(int employeeId, int managerId, UserRequestDto userDto);

    // Method to update a manager
    User updateManager(int managerId, UserRequestDto userDto);
    AuthResponse authenticate(AuthRequest authRequest) throws Exception;
    AuthResponse validateOtp(OtpRequest otpRequest);
    //User addManagerSkill(User manager);
   // User addManagerSkills(int managerId, List<SkillsAsssessment> skills);

    List<UserDTO> getUsersByPosition(int positionId);

    Optional<User> findUserById(int userId);

    void transferUserToManager(int userId, int managerId);
    void transferManagerToManager(int userId, int managerId);
    void lockUserAccount(int userId);

    void unlockUserAccount(int userId);

    void disableUserAccount(int userId);

    void enableUserAccount(int userId);
}
