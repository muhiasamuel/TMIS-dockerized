package com.example.talent_man.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto implements Serializable {
    private String userFullName;
    private String departmentPosition;
    private String password;
    private Long role_id;
}


