package com.example.talent_man.utils;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class CombinedDataResponse {
    private Map<String, Map<String, List<Double>>> combinedData;
    private List<Item> items;
    private int status;
    private String message;

    @Data
    public static class Item {

        private Double driveScore;
        private Double changeAgilityScore;
        private Double judgmentScore;
        private Double AspirationScore;
        private String userId;
        private String username;
        private String userFullName;
        private int managerId;
        private double averagePerformance;
        private double averagePotential;
        private double userAssessmentAvg;
        private double manAssessmentAvg;
        private String assessmentName;
        private String potentialAttributeName;
        private String talentRating;
        private String potentialRating;
        private int performanceRating;
        private int performanceYear;
    }
}
