package com.example.talent_man.dto.hippo;

import com.example.talent_man.dto.potential_attributes.P_HippoScore;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class HippoScore {
    private int empId;
    private String empName;
    private List<P_HippoScore> attScores;
    private double averageScore;
    private char potentialRating = 'B';
    private char performanceRatings = '3';
    private String talentRating = "H1";

    //helper methods
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
        this.averageScore = averageScore;
    }
}
