package com.example.talent_man.controllers;

import com.example.talent_man.dto.CreateRoleRequest;
import com.example.talent_man.models.Permission;
import com.example.talent_man.repos.RoleRepository;
import com.example.talent_man.utils.ApiResponse;
import com.example.talent_man.models.Role;
import com.example.talent_man.services.PermissionService;
import com.example.talent_man.services.RoleService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Data
@RestController
@RequestMapping("/v1/api/roles")
public class RolePermissionsController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleRepository repo;
    @Autowired
    private PermissionService permissionService;

    @PostMapping("/createRoles")
    public ApiResponse<Role> createRole(@RequestBody CreateRoleRequest request) {
        Role createdRole = roleService.createRole(request.getRoleName(), request.getPermissionIds());
        ApiResponse<Role> res = new ApiResponse<>();
        res.setMessage("Roles created successfully");
        res.setStatus(200);
        res.setItem(createdRole);
        return res;
    }

    @PostMapping("/createPermissions")
    public ApiResponse<List<Permission>> createPermissions(@RequestBody List<Permission> permissions) {
        List<Permission> createdPermissions = permissionService.createPermissionList(permissions);
        ApiResponse<List<Permission>> res = new ApiResponse<>();
        res.setMessage("Permissions Created Successfully");
        res.setStatus(200);
        res.setItem(createdPermissions);
        return res;
    }
    @GetMapping("/getRoles")
    public ApiResponse<List<Role>> GetRole() {
        try {
                ApiResponse<List<Role>> newSet = new ApiResponse<>(200, " successfully");
                List<Role> role = repo.findAll();
                newSet.setItem(role);
                return newSet;

        } catch (Exception e) {
            return new ApiResponse<>(500, e.getMessage());
        }
    }
}

// Class representing the request body


