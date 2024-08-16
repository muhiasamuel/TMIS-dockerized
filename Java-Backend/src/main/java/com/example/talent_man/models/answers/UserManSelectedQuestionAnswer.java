package com.example.talent_man.models.answers;

import com.example.talent_man.models.AssessmentQuestion;
import com.example.talent_man.models.Choice;
import com.example.talent_man.models.user.User;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Entity
@Data
@Table(name = "user_manager_question_answers")
public class UserManSelectedQuestionAnswer implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assessment_question_id", nullable = false)
    private AssessmentQuestion assessmentQuestion; // Reference to the assessment question

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // User who answered

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "choice_id")
    private Choice userChoice; // Choice selected by the user

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_choice_id")
    private Choice managerChoice; // Choice selected by the manager (if applicable)

    @Column(name = "assessment_id", columnDefinition = "INTEGER")
    private int assessmentId;

    @Column(name = "is_self_assessed", columnDefinition = "BOOLEAN")
    private boolean isSelfAssessed; // Indicates if this answer is from self-assessment

    @Column(name = "is_manager_assessed", columnDefinition = "BOOLEAN")
    private boolean isManagerAssessed; // Indicates if this answer is from manager assessment

    @Column(name = "user_score", columnDefinition = "INTEGER")
    private int userScore; // Store the score for the user's selected choice

    @Column(name = "manager_score", columnDefinition = "INTEGER")
    private int managerScore; // Store the score for the manager's selected choice

    // Additional fields, such as timestamps, can be added as needed
}