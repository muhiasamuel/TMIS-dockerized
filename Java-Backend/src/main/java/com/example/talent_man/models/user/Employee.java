package com.example.talent_man.models.user;

import com.example.talent_man.models.Role;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Setter
@Getter
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class Employee extends User  implements Serializable {
    @JsonManagedReference // Keep this to refer to the manager
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="manager_id")
    @ToString.Exclude
    private Manager manager;

    public Employee(){}
    public Employee(String name,String password, Role roleId){
        super(name, password, roleId);
    }

}
