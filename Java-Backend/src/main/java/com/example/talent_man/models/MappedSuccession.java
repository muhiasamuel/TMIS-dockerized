package com.example.talent_man.models;

import com.example.talent_man.models.user.Employee;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity

public class MappedSuccession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
    @Column(name = "potential_next_role")
    private String PotentialNextRole;
}
