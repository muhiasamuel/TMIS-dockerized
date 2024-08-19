package com.example.talent_man.models.user;

import com.example.talent_man.models.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.*;

@Data
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@JsonIgnoreProperties({"hibernateLazyInitializer"})
@NoArgsConstructor
public abstract class User implements Serializable, UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", columnDefinition = "INTEGER")
    private int userId;

    @Column(name = "user_type", columnDefinition = "VARCHAR(255)")
    private String userType;

    @Column(name = "PF_NO", columnDefinition = "VARCHAR(255)", unique = true)
    private String pf;

    @Column(name = "user_full_name", columnDefinition = "VARCHAR(255)")
    private String userFullName;

    @Column(name = "email", columnDefinition = "VARCHAR(255)", unique = true)
    private String email;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "position_id")
    private Position position;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "department_id")
    private Department department;

    @Column(name = "username", columnDefinition = "VARCHAR(255)", unique = true)
    private String username;

    @Column(name = "password", columnDefinition = "VARCHAR(255)")
    private String password;

    private Boolean locked = false;
    private Boolean enabled = true;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "role_id")
    private Role role;



    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_assessments",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "assessment_id")
    )
    private Set<Assessment> completeAssessments = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        role.getPermissions().forEach(permission ->
                authorities.add(new SimpleGrantedAuthority(permission.getPermissionName())));
        return authorities;
    }

    public User(String userFullName, String password, Role role) {
        this.userFullName = userFullName;
        this.password = password;
        this.role = role;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }



}
