package com.example.talent_man.models.succession;

import com.example.talent_man.models.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Entity
@Table(name = "ready_users")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
@NoArgsConstructor
public class ReadyUsers implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "succession_plan_id")
    private SuccessionPlan successionPlan;

    @Column(name = "readiness_level")
    private String readinessLevel; // E.g., "Now", "1-2 Years", "More than 2 Years"

    @OneToMany(mappedBy = "readyUser")
    private List<ProposedIntervention> proposedInterventions;

    @OneToMany(mappedBy = "readyUser")
    private List<SuccessorDevelopmentNeed> developmentNeeds;
}
