package com.example.talent_man.dto.user;
import com.example.talent_man.models.Position;
import lombok.Data;

@Data
public class UserRequestDto {
    private String userFullName;
    private String pf;
    private String username;
    private String email;
    private Long roleId;
    private Integer positionId;
    private Boolean locked;
    private Boolean enabled;
}