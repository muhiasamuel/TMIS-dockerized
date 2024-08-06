package com.example.talent_man.controllers.pages.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class PerformedAttribute implements Serializable {
    private int attId;
    private int attScore;
   // private int doneBy;
    private String attName;
    //private String assName;
    private double empAverageScore;


    //creating helper methods
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
