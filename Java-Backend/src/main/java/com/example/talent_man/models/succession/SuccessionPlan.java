package com.example.talent_man.models.succession;

import com.example.talent_man.models.Department;
import com.example.talent_man.models.Position;
import com.example.talent_man.models.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Entity
@Table(name = "succession_plan")
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class SuccessionPlan implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int planId;

    @ManyToOne
    @JoinColumn(name = "department_id", referencedColumnName = "department_id")
    @JsonIgnore
    private Department department;

    @ManyToOne
    @JoinColumn(name = "position_id", referencedColumnName = "position_id")
    @JsonIgnore
    private Position position;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "driver_id", referencedColumnName = "driver_id")
    private SuccessionDrivers planDriver;

    @Column(name = "risk_rating")
    private String retentionRiskRating;

    @ManyToOne
    @JoinColumn(name = "current_role_holder_id", referencedColumnName = "user_id")
    private User currentRoleHolder;

    @OneToMany(mappedBy = "successionPlan", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<ReadyUsers> readyNow;

    @OneToMany(mappedBy = "successionPlan", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<ReadyUsers> readyAfterTwoYears;

    @OneToMany(mappedBy = "successionPlan", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<ReadyUsers> readyMoreThanTwoYears;

    @OneToMany(mappedBy = "successionPlan", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<ProposedIntervention> proposedInterventions;

    @OneToMany(mappedBy = "successionPlan", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<SuccessorDevelopmentNeed> successorDevelopmentNeeds;

    @Embedded
    private ExternalSuccessor externalSuccessor; // New field for External Successor
}
