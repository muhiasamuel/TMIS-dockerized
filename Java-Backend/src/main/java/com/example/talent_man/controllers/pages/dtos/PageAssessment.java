package com.example.talent_man.controllers.pages.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PageAssessment {
    private int assId;
    private int doneBy;
    private int assScore;
    private double empAverageScore;
    private String assName;
    private String assDescription;

    public void calculateAverageScore(List<Integer> scores){
        int score = 0;
        double averageScore = 0.0;
        if(!scores.isEmpty()){
            for (Integer s:scores) {
                score += s;
            }
            //calculating average score
            averageScore = (double) score /scores.size();
        }
        this.empAverageScore = averageScore;
    }

}
