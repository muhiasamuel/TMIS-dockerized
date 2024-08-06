package com.example.talent_man.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class CreateRoleRequest {
    private String roleName;
    private List<Long> permissionIds;

    // Getters and setters (omitted for brevity)
}
