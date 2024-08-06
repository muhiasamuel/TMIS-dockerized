package com.example.talent_man.models;

import com.example.talent_man.models.user.Manager;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity(name = "critical_roles_strategies")
public class CriticalRolesStrategies {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String StrategyName;

//    @Column(name = "description", columnDefinition = "LONGTEXT")
//    private String HowToAchieveStrategy;


    @ManyToOne
    @JoinColumn(name = "role_assessment_id")
    private RolesAssessment roleAssessment;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Manager addedBy;

    // Constructors, getters, setters, etc.
    public CriticalRolesStrategies(Long id, String strategyName, RolesAssessment roleAssessment) {
        this.id = id;
        StrategyName = strategyName;
        this.roleAssessment = roleAssessment;
    }

    public CriticalRolesStrategies() {

    }
}
