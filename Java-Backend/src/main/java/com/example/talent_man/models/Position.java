package com.example.talent_man.models;

import com.example.talent_man.models.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Data
@Entity
@Table(name = "department_positions")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
@NoArgsConstructor
public class Position implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "position_id")
    private int pId;

    @Column(name = "position_name", columnDefinition = "VARCHAR(255)", unique = true)
    private String positionName;

    @ManyToOne
    @JoinColumn(name = "department_id")
    @JsonIgnore
    private Department department;

    @Override
    public int hashCode() {
        return Objects.hash(pId);
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Position other = (Position) obj;
        return pId == other.pId;
    }

    @Override
    public String toString() {
        return "Position{" +
                "pId=" + pId +
                ", positionName='" + positionName + '\'' +
                '}';
    }
}
