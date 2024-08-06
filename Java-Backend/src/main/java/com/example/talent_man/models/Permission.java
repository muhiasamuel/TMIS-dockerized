package com.example.talent_man.models;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.util.Lazy;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String permissionName;

    @Column(nullable = false)
    private String resource; // New field to define the affected resource

    private String description;
    // Add other relevant permission attributes as needed

//    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL) // ManyToMany relationship with Role entity
// // Define join table
//    private Set<Role> roles = new HashSet<>();

    // Getters, setters, and other methods
}