package com.example.talent_man.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Logs")
public class Logs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id", columnDefinition = "INTEGER")
    private long log_id;

    @Column(name = "action_type", columnDefinition = "VARCHAR(255)")
    private String action_type;

    @Column(name = "affected_data", columnDefinition = "VARCHAR(255)")
    private String affected_data;

    @Column(name = "timestamps", columnDefinition = "VARCHAR(255)")
    private String timestamps;

}
