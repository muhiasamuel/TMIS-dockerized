package com.example.talent_man.models;

import com.example.talent_man.models.succession.SuccessionPlan;
import com.example.talent_man.models.user.Manager;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.*;

@Data
@Entity
@NoArgsConstructor
@Table(name = "company_departments")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class Department implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "department_id")
    private int depId;

    @Column(name = "department_name", columnDefinition = "VARCHAR(255)", unique = true)
    private String depName;

    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Position> departmentPositions = new ArrayList<>();


//    @OneToOne(mappedBy = "department", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
//    @JsonBackReference
//    private SuccessionPlan plan;

    @Override
    public int hashCode() {
        return Objects.hash(depId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Department other = (Department) obj;
        return depId == other.depId;
    }
}
