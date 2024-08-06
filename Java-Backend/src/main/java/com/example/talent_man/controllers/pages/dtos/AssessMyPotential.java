package com.example.talent_man.controllers.pages.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class AssessMyPotential implements Serializable{
    private int numberOfEmployees;
    private int numberOfAssessments;
    private String managerName;
}
