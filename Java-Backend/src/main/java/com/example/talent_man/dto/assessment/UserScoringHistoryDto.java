package com.example.talent_man.dto.assessment;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;


@Data
public class UserScoringHistoryDto {
    private int assessmentId;
    private String assessmentName;
    private String assessmentDescription;
    private LocalDate assessmentDate;
    private Double overallScore; // Use Double instead of double
    private boolean isManagerAssessed;
    private List<scoring> assessmentStatuses;

    @Data
    public static class scoring implements Serializable {
        private Double managerScore; // Use Double instead of double
        private String potentialAttributeName;
        private Double userScore;

    }
}


