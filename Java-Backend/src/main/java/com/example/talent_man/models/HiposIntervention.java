package com.example.talent_man.models;

import com.example.talent_man.models.user.Employee;
import com.example.talent_man.models.user.User;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class HiposIntervention {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private User employee;

    @Column(name = "development_interventions")
    private String developmentInterventions;


    @Column(name = "how_to_achieve")
    private String howToAchieveInterventions;
}
