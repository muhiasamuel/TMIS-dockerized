package com.example.talent_man.dto.user;

import com.example.talent_man.dto.potential_attributes.P_AttributeDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class UserPotentialMatrix implements Serializable {
    private int userId;
    private String userName;
    private List<P_AttributeDto> pAttributes;
}
