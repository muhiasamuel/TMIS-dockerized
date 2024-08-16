package com.example.talent_man.models;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

@Data
@Entity
@Table(name = "assessment_questions")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "assessmentQuestionId")

@NoArgsConstructor
public class AssessmentQuestion implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "assessment_question_id", columnDefinition = "INTEGER")
    private int assessmentQuestionId;

    @Column(name = "assessment_question_description", columnDefinition = "TEXT")
    private String assessmentQuestionDescription;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "potential_attribute_id", nullable = false) // Foreign key to PotentialAttribute
    @JsonBackReference // Prevents recursive serialization
    private PotentialAttribute potentialAttribute;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "assessment_question_id")
    private Set<UserQuestionAnswer> selectedAnswers;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "assessment_question_id")
    private Set<Choice> choices; // Questions have choices

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AssessmentQuestion)) return false;
        AssessmentQuestion that = (AssessmentQuestion) o;
        return assessmentQuestionId == that.assessmentQuestionId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(assessmentQuestionId);
    }
}
