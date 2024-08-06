package com.example.talent_man.dto.user;
import com.example.talent_man.dto.PerformanceDto;
import com.example.talent_man.dto.PermissionResDto;
import com.example.talent_man.models.Permission;
import com.example.talent_man.models.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String authToken;
    private User user;
    private Set<Permission> permissions;
    private String message;
    private int status;

    public AuthResponse( String mess, int status){

        this.status = status;
        this.message = mess;

    }
}