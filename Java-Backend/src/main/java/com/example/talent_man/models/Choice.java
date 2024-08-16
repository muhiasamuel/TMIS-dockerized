package com.example.talent_man.models;

import com.example.talent_man.models.composite_keys.UserAnswerKey;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

@Data
@Entity
@Table(name = "choices")
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer"})
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "choiceId")

public class Choice implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "choice_id", columnDefinition = "INTEGER")
    private int choiceId;

    @Column(name = "choice_name", columnDefinition = "VARCHAR(255)")
    private String choiceName;

    @Column(name = "choice_value", columnDefinition = "VARCHAR(255)")
    private String choiceValue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assessment_question_id", nullable = false) // Foreign key to AssessmentQuestion
    private AssessmentQuestion assessmentQuestion; // Reference to AssessmentQuestion
}