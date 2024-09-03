package com.example.talent_man.models.succession;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "external_successor")
@NoArgsConstructor
public class ExternalSuccessor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;             // Name of the external successor
    private String contactInfo;      // Contact information (email, phone)
    private String currentPosition;  // Current position outside the organization
    private String currentCompany;   // Current company of the external successor
    private String reasonForSelection; // Why this external successor is being considered
    private String expectedStartDate;  // When they might be available to start

    @ManyToOne
    @JoinColumn(name = "succession_plan_id")
    private SuccessionPlan successionPlan;  // Reference to the SuccessionPlan
}
