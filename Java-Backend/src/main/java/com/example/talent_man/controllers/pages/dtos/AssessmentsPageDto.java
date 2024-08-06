package com.example.talent_man.controllers.pages.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class AssessmentsPageDto implements Serializable {
    private int attId;
    private int totalEmployees;
    private String attName;
    private String attDescription;
    List<PageAssessment> assessments = new ArrayList<>();


}
