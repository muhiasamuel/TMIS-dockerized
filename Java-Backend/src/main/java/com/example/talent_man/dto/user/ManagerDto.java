package com.example.talent_man.dto.user;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class ManagerDto implements Serializable {
    private Long id;
    private int roleId;
    private String userType;
    private String userFullName;
}
