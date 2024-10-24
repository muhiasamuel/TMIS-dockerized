package com.example.talent_man.controllers.user;

import com.example.talent_man.dto.user.UserDetailsDto;
import com.example.talent_man.dto.user.UserRequestDto;
import com.example.talent_man.models.user.User;
import com.example.talent_man.models.user.UserDTO;
import com.example.talent_man.services.UserService;
import com.example.talent_man.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/v1/api/users")
@RequiredArgsConstructor

public class EmployeeController {

    private final UserService userService;

    @PostMapping("/create-employee/{managerId}")
    public ApiResponse<User> createEmployee(@RequestBody UserRequestDto userDto, @PathVariable int managerId) {
        User createdEmployee = userService.createEmployee(userDto, managerId);
        ApiResponse res = new ApiResponse<>();
        res.setItem(createdEmployee);
        res.setMessage("Employee created Successfully");
        res.setStatus(200);
        return res;
    }

    @PostMapping("/top-manager")
    public ApiResponse<User> createTopManager(@RequestBody UserRequestDto userDto) {
        User createdEmployee = userService.createTopManager(userDto);
        ApiResponse res = new ApiResponse<>();
        res.setItem(createdEmployee);
        res.setMessage("Top-Manager created Successfully");
        res.setStatus(200);
        return res;
    }

    @PostMapping("/manager/{managerId}")
    public ApiResponse<User> createManager(@RequestBody UserRequestDto userDto, @PathVariable int managerId) {
        User createdEmployee = userService.createManager(userDto, managerId);
        ApiResponse res = new ApiResponse<>();
        res.setItem(createdEmployee);
        res.setMessage("Manager created Successfully");
        res.setStatus(200);
        return res;
    }

    @PostMapping("/create-employees/{managerId}")
    public ApiResponse<List<User>> createEmployees(@RequestBody List<UserRequestDto> userDtos, @PathVariable int managerId) {
        List<User> createdEmployees = userService.createEmployees(userDtos, managerId);
        ApiResponse<List<User>> res = new ApiResponse<>(200, "Employees created successfully");
        res.setItem(createdEmployees);
        return res;
    }

    @PostMapping("/upload-employees/{managerId}")
    public ApiResponse<List<User>> uploadEmployees(@RequestParam("file") MultipartFile file, @PathVariable int managerId) {
        List<User> createdEmployees = userService.uploadEmployees(file, managerId);
        ApiResponse<List<User>> res = new ApiResponse<>(200, "Employees uploaded successfully");
        res.setItem(createdEmployees);
        return res;
    }

    //get employees by position they hold
    @GetMapping("get/employees/{positionId}")
    public ApiResponse<List<UserDTO>> getEmployeesByPosition(@PathVariable int positionId) {
        List<UserDTO> users = userService.getUsersByPosition(positionId);
        ApiResponse<List<UserDTO>> res = new ApiResponse<>();

        if (users.isEmpty()) {
            res.setMessage("No employees found for the given position");
            res.setStatus(404); // You can use 404 to indicate "Not Found" or 200 for a successful empty result
        } else {
            res.setMessage("Employees retrieved successfully");
            res.setStatus(200);
        }

        res.setItem(users);
        return res;
    }

    //GetAllEmployees
    @GetMapping("get/all_employees")
    public ApiResponse<List<UserDTO>> getAllEmployees() {
        List<UserDTO> users = userService.findAllUsers();
        ApiResponse<List<UserDTO>> res = new ApiResponse<>();

        if (users.isEmpty()) {
            res.setMessage("No employees found for the given position");
            res.setStatus(404); // You can use 404 to indicate "Not Found" or 200 for a successful empty result
        } else {
            res.setMessage("Employees retrieved successfully");
            res.setStatus(200);
        }

        res.setItem(users);
        return res;
    }

    @GetMapping("/details")
    public ApiResponse<List<UserDetailsDto>> getAllUserDetails() {
        // Call the service to get user details
        List<UserDetailsDto> userDetails = userService.getAllUserDetails();
        ApiResponse<List<UserDetailsDto>> res = new ApiResponse<>();

        if (userDetails.isEmpty()) {
            res.setMessage("No employees found");
            res.setStatus(HttpStatus.NOT_FOUND.value()); // 404 if no employees are found
        } else {
            res.setMessage("Employees retrieved successfully");
            res.setStatus(HttpStatus.OK.value()); // 200 if employees are found
        }
        res.setItem(userDetails);
        return res;
    }
    // Update an employee
    @PatchMapping("update/employee-details/{employeeId}/{managerId}")
    public ResponseEntity<ApiResponse<String>> updateEmployee(
            @PathVariable int employeeId,@PathVariable int managerId, @RequestBody UserRequestDto userDto) {

        ApiResponse<String> response = new ApiResponse<>();
        try {
            userService.updateEmployee(employeeId,managerId, userDto);
            response.setStatus(HttpStatus.OK.value());
            response.setMessage("Employee updated successfully");
            response.setItem("Success");
        } catch (Exception e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage("Failed to update employee: " + e.getMessage());
        }
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    // Update a manager
    @PatchMapping("update/manager-details/{managerId}")
    public ResponseEntity<ApiResponse<String>> updateManager(
            @PathVariable int managerId, @RequestBody UserRequestDto userDto) {

        ApiResponse<String> response = new ApiResponse<>();
        try {
            userService.updateManager(managerId, userDto);
            response.setStatus(HttpStatus.OK.value());
            response.setMessage("Manager updated successfully");
            response.setItem("Success");
        } catch (Exception e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage("Failed to update manager: " + e.getMessage());
        }
        return ResponseEntity.status(response.getStatus()).body(response);
    }

}
