package com.example.talent_man.models;

import com.example.talent_man.models.user.Manager;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Data
@Entity
@Table(name = "potential_attributes")
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "potentialAttributeId")

@AllArgsConstructor
public class PotentialAttribute implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "potential_attribute_id", columnDefinition = "INTEGER")
    private int potentialAttributeId;

    @Column(name = "potential_attribute_name", columnDefinition = "VARCHAR(255)", unique = true)
    private String potentialAttributeName;

    @Column(name = "potential_attribute_description", columnDefinition = "TEXT")
    private String potentialAttributeDescription;

    @Column(name = "date_created", columnDefinition = "TIMESTAMP")
    private LocalDateTime createdAt;

    @ManyToMany(mappedBy = "potentialAttributes", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Assessment> assessments;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manager_id", nullable = false)
    @JsonBackReference
    private Manager manager;


    @OneToMany(mappedBy = "potentialAttribute", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference // Manages the relationship for serialization
    private Set<AssessmentQuestion> questions;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PotentialAttribute)) return false;
        PotentialAttribute that = (PotentialAttribute) o;
        return potentialAttributeId == that.potentialAttributeId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(potentialAttributeId);
    }
}
