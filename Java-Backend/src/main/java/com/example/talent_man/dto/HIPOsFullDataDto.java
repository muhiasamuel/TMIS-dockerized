package com.example.talent_man.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HIPOsFullDataDto {

    private int userId;
    private String userName;
    private String userFullName;
    private String potentialAttribute;
    private double averagePerformance;
    private int userAssessmentAvg;
    private double manAssessmentAvg;
    private String assessmentDate;
    private int performanceYear;
    private double averageManAssessmentAvg;
    private String nextPotentialRole;
    private List<String> interventions; // Combined field for developmentInterventions and howToAchieve
    private int potentialRatingSum;
    private int potentialRating;

    public void setDevelopmentInterventionsAndHowToAchieve(String developmentInterventions, String howToAchieve) {
        // Create a new set for interventions to ensure uniqueness
        Set<String> interventionSet = new HashSet<>();

        // Add developmentInterventions and howToAchieve to the set
        interventionSet.add(developmentInterventions);
        interventionSet.add(howToAchieve);

        // Convert the set back to a list to preserve order
        interventions = new ArrayList<>(interventionSet);
    }
}
