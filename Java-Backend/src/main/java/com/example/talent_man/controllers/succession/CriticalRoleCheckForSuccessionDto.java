package com.example.talent_man.controllers.succession;

import lombok.Data;

@Data
public class CriticalRoleCheckForSuccessionDto {

    private Long roleId;
    private int planId;
    private String roleName;
    private String currentState;
    private String successionStatus;
}
