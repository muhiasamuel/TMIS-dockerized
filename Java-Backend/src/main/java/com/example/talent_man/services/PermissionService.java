package com.example.talent_man.services;

import com.example.talent_man.models.Permission;
import com.example.talent_man.models.Role;
import com.example.talent_man.repos.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PermissionService {

    @Autowired
    private PermissionRepository permissionRepository;

    public Permission createPermission(String permissionName) {
        Permission permission = new Permission();
        try {
            Optional<Permission> existingPermissionOptional = permissionRepository.findByPermissionName(permissionName);
            if (existingPermissionOptional.isPresent()) {
                // Role with the given role name already exists
                // For example, you can throw an exception or return a custom error message
                throw new RuntimeException("permission with name " + permissionName + " already exists");
            }
            permission.setPermissionName(permissionName);
        }catch (Exception e){
            throw new RuntimeException("Error occurred while saving permissions: " + e.getMessage(), e);
        }

        return permissionRepository.save(permission);
    }

    public List<Permission> createPermissionList(List<Permission> permissions) {

        List<Permission> savedPermissions = new ArrayList<>();
        // Get all existing permissions

        try {
            List<Permission> existingPermissions = permissionRepository.findAll();
            for (Permission permission : permissions) {
                boolean permissionExists = existingPermissions.stream()
                        .anyMatch(existingPermission -> existingPermission.getPermissionName().equals(permission.getPermissionName()));

                if (!permissionExists) {
                    // Permission does not already exist, so save it
                    Permission savedPermission = permissionRepository.save(permission);
                    savedPermissions.add(savedPermission);

                }else {
                    throw new RuntimeException("Permission with name " + permission.getPermissionName() + " already exists");
                }
            }
        } catch (Exception e) {
            // Handle any exceptions that might occur during the saving process
            // For example, you can log the exception or return an error message
            throw new RuntimeException("Error occurred while saving permissions: " + e.getMessage(), e);
            // Here, we're rethrowing the exception as a RuntimeException
        }


        return savedPermissions;
    }



}
