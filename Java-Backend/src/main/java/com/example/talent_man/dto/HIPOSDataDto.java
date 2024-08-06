package com.example.talent_man.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HIPOSDataDto {
    private int userId;
    private String userFullName;
    private String nextPotentialRole;
    private List<InterventionDto> interventions;
}

