package com.example.talent_man.models.succession;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "proposed_interventions")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
@NoArgsConstructor
public class ProposedIntervention implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "ready_user_id")
    private ReadyUsers readyUser;

    @ManyToOne
    @JoinColumn(name = "plan_id")
    private SuccessionPlan successionPlan;

    @Column(name = "intervention_type")
    private String type;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    private String status;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;
}
