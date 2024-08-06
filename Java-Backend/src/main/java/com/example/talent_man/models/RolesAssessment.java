package com.example.talent_man.models;

import com.example.talent_man.models.user.Manager;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity(name="roles_assessment")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class RolesAssessment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="critical_role_id", columnDefinition = "INTEGER")
    private Long criticalRoleId;

    @Column(name="role_name", columnDefinition = "VARCHAR(255)")
    private String roleName;

    @Column(name="current_state", columnDefinition = "VARCHAR(255)")
    private String currentState;

    @Column(name="strategic_importance", columnDefinition = "VARCHAR(255)")
    private String strategicImportance;

    @Column(name="risk_impact", columnDefinition = "VARCHAR(255)")
    private String riskImpact;

    @Column(name="vacancy_risk", columnDefinition = "VARCHAR(255)")
    private String vacancyRisk;

    @Column(name="impact_on_operation", columnDefinition = "VARCHAR(255)")
    private String impactOnOperation;

    @Column(name="skill_experience", columnDefinition = "VARCHAR(255)")
    private String skillExperience;

    @Column(name="average_rating")
    private double averageRating;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id")
    private Manager addedBy;

    @OneToMany(mappedBy = "roleAssessment", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<CriticalRolesStrategies> RoleDevelopmentStrategies = new ArrayList<>();

    //    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    @JoinColumn(name = "user_id")
//    Set<User> users;
}
