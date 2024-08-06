package com.example.talent_man.models;

import com.example.talent_man.models.user.User;
import com.example.talent_man.utils.YearDeserializer;
import com.example.talent_man.utils.YearSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Year;

@Entity
@Data
public class Performance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private Year year;
    private int quarter; // 1 for Q1, 2 for Q2, 3 for Q3
    private double performanceMetric;

    public Performance() {
    }

    public Performance(Long id, User user, Year year, int quarter, double performanceMetric) {
        this.id = id;
        this.user = user;
        this.year = year;
        this.quarter = quarter;
        this.performanceMetric = performanceMetric;
    }
    // Constructors, getters, setters
}