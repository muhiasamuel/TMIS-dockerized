package com.example.talent_man.services;

import com.example.talent_man.dto.user.AuthRequest;
import com.example.talent_man.dto.user.UserRequestDto;
import com.example.talent_man.models.user.Employee;
import com.example.talent_man.models.user.Manager;
import com.example.talent_man.models.user.User;

import com.example.talent_man.dto.user.AuthResponse;
import com.example.talent_man.dto.user.OtpRequest;
import com.example.talent_man.repos.PerformanceRepository;
import com.example.talent_man.repos.user.UserRepo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    //Create
    User addUser(User user);
    User getByUsername(String username);
    //Read
    Manager getManagerById(int id);
    Manager getManagerByUserName(String username);
    Employee getEmployeeByUserName(String username);

    List<Manager> findAllManagers();
    List<Employee> findAllEmployees();

    User getUserById(int id);

    UserRepo.UserWithManagerDetails getUserPerformancesByManagerId(int employeeId);
    List<User> getManagerEmployees(Manager manId);
    List<Integer> getManEmployees(int manId);

    User createEmployee(UserRequestDto userDto, int managerId);

    User createTopManager(UserRequestDto userDto);

    List<User> createEmployees(List<UserRequestDto> userDtos, int managerId);
    List<User> uploadEmployees(MultipartFile file, int managerId);
    User createManager(UserRequestDto userDto, int managerId);
    AuthResponse authenticate(AuthRequest authRequest) throws Exception;
    AuthResponse validateOtp(OtpRequest otpRequest);
    //User addManagerSkill(User manager);
   // User addManagerSkills(int managerId, List<SkillsAsssessment> skills);
}
