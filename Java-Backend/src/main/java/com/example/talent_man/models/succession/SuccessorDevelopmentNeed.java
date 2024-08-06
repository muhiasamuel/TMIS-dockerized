package com.example.talent_man.models.succession;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Entity
@Table(name = "successor_development_needs")
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class SuccessorDevelopmentNeed implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "ready_user_id")
    private ReadyUsers readyUser;

    @ManyToOne
    @JoinColumn(name = "succession_plan_id")
    private SuccessionPlan successionPlan;

    @Column(name = "need_type")
    private String needType;

    @Column(name = "description")
    private String description;
}
