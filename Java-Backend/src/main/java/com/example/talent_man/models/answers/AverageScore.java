package com.example.talent_man.models.answers;

import com.example.talent_man.models.Assessment;
import com.example.talent_man.models.PotentialAttribute;
import com.example.talent_man.models.user.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Entity
@Table(name = "average_scores")
@NoArgsConstructor
public class AverageScore implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "average_score_id")
    private int averageScoreId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Reference to the user

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "assessment_id", nullable = false)
    private Assessment assessment; // Reference to the assessment

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "potential_attribute_id", nullable = false)
    private PotentialAttribute potentialAttribute; // Reference to the potential attribute

    private double averageScore; // The calculated average score
}
