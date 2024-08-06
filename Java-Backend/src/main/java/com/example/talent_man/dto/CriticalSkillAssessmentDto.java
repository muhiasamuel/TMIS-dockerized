package com.example.talent_man.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class CriticalSkillAssessmentDto {

    @NotBlank(message = "Skill name is required")
    @Size(max = 255, message = "Skill name must be less than or equal to 255 characters")
    private String skillName;

    @NotBlank(message = "Description is required")
    @Size(max = 1000, message = "Description must be less than or equal to 1000 characters")
    private String description;

    private List<String> skillDevelopmentStrategy;

    @NotBlank(message = "businessPriority is required")
    private String businessPriority;

    @NotBlank(message = "currentSkillState is required")
    private String currentSkillState;

    @NotBlank(message = "scarcityParameter is required")
    private String scarcityParameter;

    @NotBlank(message = "marketFluidity is required")
    private String marketFluidity;

    @NotBlank(message = "developmentCostAndTimeCommitment is required")
    private String developmentCostAndTimeCommitment;

    @NotBlank(message = "futureMarketAndTechRelevance is required")
    private String futureMarketAndTechRelevance;

    @NotBlank(message = "averageRating is required")
    private Double averageRating;
    // Other fields with validation annotations as needed
}
