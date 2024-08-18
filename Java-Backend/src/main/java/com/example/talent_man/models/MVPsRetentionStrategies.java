package com.example.talent_man.models;

import com.example.talent_man.models.user.Employee;
import com.example.talent_man.models.user.User;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class MVPsRetentionStrategies {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private User user;

    @Column(name = "development_interventions")
    private String retentionStrategies;

//    @ManyToOne
//    @JoinColumn(name = "mvp_assessment_id")
//    private MVPsAssessment mvPsAssessment;
}
