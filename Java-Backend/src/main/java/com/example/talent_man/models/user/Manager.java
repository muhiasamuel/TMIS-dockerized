package com.example.talent_man.models.user;

import com.example.talent_man.models.Department;
import com.example.talent_man.models.PotentialAttribute;
import com.example.talent_man.models.SkillsAsssessment;
import com.example.talent_man.models.RolesAssessment;
import com.example.talent_man.models.Assessment;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class Manager extends User implements Serializable {

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private Set<PotentialAttribute> potentialAttributeSet = new HashSet<>();

    @JsonBackReference // Add this to avoid circular reference
    @OneToMany(mappedBy = "manager", fetch = FetchType.EAGER)
    private Set<Employee> employees = new HashSet<>();


    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "manager_id")
    private Set<SkillsAsssessment> skillsAssessments = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "addedBy", cascade = CascadeType.ALL)
    private Set<RolesAssessment> rolesAssessments = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "manager_id")
    private Set<Assessment> assessedAssessments = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="manager_id")
    @ToString.Exclude
    private Manager manager;


}
