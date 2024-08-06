package com.example.talent_man.models;

import com.example.talent_man.models.user.Manager;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Entity
@Table(name = "potential_attributes")
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer"})
@AllArgsConstructor
public class PotentialAttribute implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "potential_attribute_id", columnDefinition = "INTEGER")
    private int potentialAttributeId;

    @Column(name = "potential_attribute_name", columnDefinition = "VARCHAR(255)", unique = true )
    private String potentialAttributeName;
    @Column(name = "potential_attribute_description", columnDefinition = "TEXT")
    private String potentialAttributeDescription;

    @Column(name = "date_created", columnDefinition = "DATE")
    private LocalDateTime createdAt;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "potential_attribute_id")
    Set<Assessment> assessments;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manager_id", nullable = false)
    @JsonBackReference
    private Manager manager;

}
