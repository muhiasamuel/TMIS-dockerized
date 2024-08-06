package com.example.talent_man.controllers.user;

import com.example.talent_man.models.PotentialAttribute;
import com.example.talent_man.models.user.Employee;
import com.example.talent_man.models.user.Manager;
import com.example.talent_man.models.user.User;
import com.example.talent_man.repos.user.UserRepo;
import com.example.talent_man.services.UserService;
import com.example.talent_man.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/v1/api/manager")
public class ManagerController {
    @Autowired
    private UserService service;

    @GetMapping("/employees")
    public ApiResponse<Set<Employee>> getManagerEmployees(@RequestParam int managerId) {

        try {
            if (managerId == 0) {
                return new ApiResponse<>(300, "Enter a valid id");
            } else {
                ApiResponse<Set<Employee>> newSet = new ApiResponse<>(200, " successfully");
                Manager manager = service.getManagerById(managerId);
                newSet.setItem(manager.getEmployees());
                return newSet;
            }
        } catch (Exception e) {
            return new ApiResponse<>(500, e.getMessage());
        }
    }
    @GetMapping("/employee/byId/")
    public ApiResponse<UserRepo.UserWithManagerDetails> getEmployeeById(@RequestParam int employeeId) {
        try {
            if (employeeId == 0) {
                return new ApiResponse<>(300, "Enter a valid id");
            } else {
                ApiResponse<UserRepo.UserWithManagerDetails> emp = new ApiResponse<>(200, " successfully");
                UserRepo.UserWithManagerDetails user = service.getUserPerformancesByManagerId(employeeId);
                emp.setItem(user);
                return emp;
            }
        } catch (Exception e) {
            return new ApiResponse<>(500, e.getMessage());
        }
    }

    //get potential mapping for employees
    @GetMapping("employees/talent/mapping")
    public ApiResponse<Employee> getEmployeeTalentMapping(@RequestParam int managerId) {
        try {


            return null;
        } catch (Exception e) {
            return new ApiResponse<>(500, e.getMessage());
        }
    }
}
