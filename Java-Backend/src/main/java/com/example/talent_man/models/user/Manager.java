package com.example.talent_man.models.user;

import com.example.talent_man.models.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class Manager extends User implements Serializable {
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private Set<PotentialAttribute> potentialAttributeSet;

    @JsonBackReference
    @OneToMany(mappedBy = "manager", fetch = FetchType.EAGER)
    private Set<Employee> employees;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "manager_id")
    private Set<SkillsAsssessment> skillsAssessments;


    @JsonIgnore
    @OneToMany(mappedBy = "addedBy", cascade = CascadeType.ALL)
    private Set<RolesAssessment> rolesAssessments = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "manager_id")
    private Set<Assessment> assessedAssessments;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "department_id", referencedColumnName = "department_id")
    private Department department;

    //constructors
    public Manager(){}


}
