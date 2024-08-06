package com.example.talent_man.models;

import com.example.talent_man.models.user.Employee;
import com.example.talent_man.models.user.User;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class MVPsAssessment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private User user;

    @Column(name = "impact_of_attrition")
    private String impactOfAttrition;
    @Column(name = "market_exposure")
    private String marketExposure;

    @Column(name = "career_priorities")
    private String careerPriorities;

    @Column(name = "retention_assessment_state")
    private String retentionAssessmentState;
}
