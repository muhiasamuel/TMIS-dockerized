package com.example.talent_man.dto.succession;


import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class ProposedInterventionDto {
    private int id;
    private String type;
    private String description;
    private String status;
    private Date startDate;
    private Date endDate;

    // Constructor with all parameters


    // Getters and setters
}
