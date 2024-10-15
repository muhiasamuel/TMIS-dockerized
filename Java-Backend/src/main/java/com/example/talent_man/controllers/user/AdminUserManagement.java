package com.example.talent_man.controllers.user;

import com.example.talent_man.services.UserService;
import com.example.talent_man.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api/users/management")
@RequiredArgsConstructor
public class AdminUserManagement {

    @Autowired
    private UserService userService;

    @PatchMapping("/transfer/{userId}/manager/{managerId}")
    public ResponseEntity<ApiResponse<String>> transferUserToManager(@PathVariable int userId, @PathVariable int managerId) {
        ApiResponse<String> response = new ApiResponse<>();
        try {
            userService.transferUserToManager(userId, managerId);
            response.setStatus(HttpStatus.OK.value());
            response.setMessage("User transferred to new manager successfully");
            response.setItem("Success");
        } catch (Exception e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage("Failed to transfer user: " + e.getMessage());
        }
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PatchMapping("/transfer-manager/{userId}/manager/{managerId}")
    public ResponseEntity<ApiResponse<String>> transferManagerToManager(@PathVariable int userId, @PathVariable int managerId) {
        ApiResponse<String> response = new ApiResponse<>();
        try {
            userService.transferManagerToManager(userId, managerId);
            response.setStatus(HttpStatus.OK.value());
            response.setMessage("Manager transferred to new manager successfully");
            response.setItem("Success");
        } catch (Exception e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage("Failed to transfer user: " + e.getMessage());
        }
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PatchMapping("/lock/{userId}")
    public ResponseEntity<ApiResponse<String>> lockUserAccount(@PathVariable int userId) {
        ApiResponse<String> response = new ApiResponse<>();
        try {
            userService.lockUserAccount(userId);
            response.setStatus(HttpStatus.OK.value());
            response.setMessage("User account locked successfully");
            response.setItem("Success");
        } catch (Exception e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage("Failed to lock user account: " + e.getMessage());
        }
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PatchMapping("/unlock/{userId}")
    public ResponseEntity<ApiResponse<String>> unlockUserAccount(@PathVariable int userId) {
        ApiResponse<String> response = new ApiResponse<>();
        try {
            userService.unlockUserAccount(userId);
            response.setStatus(HttpStatus.OK.value());
            response.setMessage("User account unlocked successfully");
            response.setItem("Success");
        } catch (Exception e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage("Failed to unlock user account: " + e.getMessage());
        }
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PatchMapping("/disable/{userId}")
    public ResponseEntity<ApiResponse<String>> disableUserAccount(@PathVariable int userId) {
        ApiResponse<String> response = new ApiResponse<>();
        try {
            userService.disableUserAccount(userId);
            response.setStatus(HttpStatus.OK.value());
            response.setMessage("User account disabled successfully");
            response.setItem("Success");
        } catch (Exception e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage("Failed to disable user account: " + e.getMessage());
        }
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PatchMapping("/enable/{userId}")
    public ResponseEntity<ApiResponse<String>> enableUserAccount(@PathVariable int userId) {
        ApiResponse<String> response = new ApiResponse<>();
        try {
            userService.enableUserAccount(userId);
            response.setStatus(HttpStatus.OK.value());
            response.setMessage("User account enabled successfully");
            response.setItem("Success");
        } catch (Exception e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage("Failed to enable user account: " + e.getMessage());
        }
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
