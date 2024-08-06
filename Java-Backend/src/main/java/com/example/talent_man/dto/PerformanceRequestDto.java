package com.example.talent_man.dto;

import com.example.talent_man.utils.YearDeserializer;
import com.example.talent_man.utils.YearSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Year;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PerformanceRequestDto {
    private YearDto year;
    private int quarter;
    private double performanceMetric;

    // Getters and setters
}
