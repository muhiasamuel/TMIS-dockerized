package com.example.talent_man.dto.assessment;

import lombok.Data;

import java.util.List;

@Data
public class ManagerAssessmentResponse {
    private int userId;
    private String username;
    private String userFullName;
    private List<AssessmentDto> questions;

    // Getters and Setters
}