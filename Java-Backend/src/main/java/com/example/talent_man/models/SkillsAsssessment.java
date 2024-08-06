package com.example.talent_man.models;

import com.example.talent_man.models.user.Manager;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Entity
@NoArgsConstructor
@Table(name = "Skills_Assessment")
public class SkillsAsssessment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "skill_assessment_id", columnDefinition = "INTEGER")
    private Long skillAssessmentId;

    @Column(name = "skill_name", columnDefinition = "VARCHAR(255)")
    private String skillName;

    @Column(name = "skill_description", columnDefinition = "VARCHAR(255)")
    private String skillDescription;

    @Column(name = "skill_development_strategy", columnDefinition = "VARCHAR(255)")
    private String skillDevelopmentStrategy;

    @Column(name = "business_priority", columnDefinition = "VARCHAR(255)")
    private String businessPriority;

    @Column(name = "current_skill_state", columnDefinition = "VARCHAR(255)")
    private String currentSkillState;

    @Column(name = "scarcity_parameter", columnDefinition = "VARCHAR(255)")
    private String scarcityParameter;

    @Column(name = "market_fluidity", columnDefinition = "VARCHAR(255)")
    private String marketFluidity;

    @Column(name = "development_cost_and_time_commitment", columnDefinition = "VARCHAR(255)")
    private String developmentCostAndTimeCommitment;

    @Column(name = "future_market_and_tech_relevance", columnDefinition = "VARCHAR(255)")
    private String futureMarketAndTechRelevance;

    @Column(name = "average_rating", columnDefinition = "VARCHAR(255)")
    private String averageRating;


    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Manager addedBy;
    //We omitted development strategy from the secondary constructor because critical skills don't one
    public SkillsAsssessment(String skillName, String skillDescription,
                             String businessPriority, String currentSkillState,
                             String scarcityParameter, String marketFluidity,
                             String developmentCostAndTimeCommitment,
                             String futureMarketAndTechRelevance, String averageRating) {

        this.skillName = skillName;
        this.skillDescription = skillDescription;
        this.businessPriority = businessPriority;
        this.currentSkillState = currentSkillState;
        this.scarcityParameter = scarcityParameter;
        this.marketFluidity = marketFluidity;
        this.developmentCostAndTimeCommitment = developmentCostAndTimeCommitment;
        this.futureMarketAndTechRelevance = futureMarketAndTechRelevance;
        this.averageRating = averageRating;
    }
}
