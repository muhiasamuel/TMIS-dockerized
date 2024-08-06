package com.example.talent_man.dto.potential_attributes;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class P_HippoScore {
    private int id;
    private String pAttName;
    private int score;

    //creating helper methods
    public void calculateTotalScore(List<Integer> questionScores){
        int qScores = 0;

        if (questionScores != null && !questionScores.isEmpty()){
            for (Integer s:questionScores) {
                qScores += s;
            }
        }

        this.score = qScores;
    }
}
