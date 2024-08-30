package com.example.talent_man.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssessmentDtoRes implements Serializable {
    private int assessmentId;
    private String assessmentName;
    private String assessmentDescription;
    private String target;
    private LocalDate createdAt;
    private LocalDate endDate;
    private String status;
}
