package com.example.talent_man.repos;

import com.example.talent_man.models.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission, Long> {


    Optional<Permission> findByPermissionName(String permissionName);

    // ... other methods ...
}
