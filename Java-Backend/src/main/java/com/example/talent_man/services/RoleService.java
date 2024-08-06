package com.example.talent_man.services;

import com.example.talent_man.models.user.Manager;
import com.example.talent_man.models.user.User;
import com.example.talent_man.utils.ResourceNotFoundException;
import com.example.talent_man.models.Permission;
import com.example.talent_man.models.Role;
import com.example.talent_man.repos.PermissionRepository;
import com.example.talent_man.repos.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PermissionRepository permissionRepository;

    public Role createRole(String roleName, List<Long> permissionIds) {
        Set<Permission> permissions = new HashSet<>();
        for (Long permissionId : permissionIds) {
            Permission permission = permissionRepository.findById(permissionId).orElse(null);
            if (permission != null) {
                permissions.add(permission);
            }
        }
        Optional<Role> existingRoleOptional = roleRepository.findByRoleName(roleName);

        if (existingRoleOptional.isPresent()) {
            // Role with the given role name already exists
            // For example, you can throw an exception or return a custom error message
            throw new RuntimeException("Role with name " + roleName + " already exists");
        }

        // Role with the given role name does not exist, proceed with creating a new role
        Role role = new Role();
        role.setRoleName(roleName);
        role.setPermissions(permissions);

        return roleRepository.save(role);
    }


}
