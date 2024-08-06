package com.example.talent_man.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PerformanceYearlyDto {
    private int userId;
    private String userFullName;
    private int yearsCount;
    private double totalPerformanceRating;
    private List<PerformanceDto> performances;
}

