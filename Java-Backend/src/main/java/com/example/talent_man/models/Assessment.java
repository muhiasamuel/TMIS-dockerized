package com.example.talent_man.models;

import com.example.talent_man.models.user.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "assessments")
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class Assessment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "assessment_id", columnDefinition = "INTEGER")
    private int assessmentId;
    @Column(name = "assessment_name", columnDefinition = "VARCHAR(255)")
    private String assessmentName;
    @Column(name = "Assessment_description", columnDefinition = "TEXT")
    private String assessmentDescription;

    @Column(name = "target_group", columnDefinition = "VARCHAR(255)")
    private String target;

    @Column(name = "end_date", columnDefinition = "DATE")
    private LocalDate endDate;

    @Column(name = "date_created", columnDefinition = "DATE")
    private LocalDate createdAt;

    @Column(name = "average_score")
    private Float averageScore = 0.0F;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "assessment_id")
    private Set<AssessmentQuestion> assessmentQuestions;

    @ManyToMany(mappedBy = "completeAssessments",fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<User> users;

    //creating helper methods
    public void calculateAverageScore(List<Integer> questionScores) {
        if (questionScores == null || questionScores.isEmpty()) {
            this.averageScore = null; // Set averageScore to null if no scores are provided
            return;
        }

        int totalScore = 0;
        for (Integer score : questionScores) {
            totalScore += score;
        }

        this.averageScore = (float) totalScore / (questionScores.size() * 6);
    }








//    @Column(name = "Assessment_question_score", columnDefinition = "INTEGER")
//    private int AssessmentQuestionScore;
//    @Column(name = "employee_assessment_question_score", columnDefinition = "INTEGER")
//    private int EmployeeAssessmentQuestionScore;
//    @Column(name = "employee_manager_assessment_question_score", columnDefinition = "INTEGER")
//    private int EmployeeManagerAssessmentQuestionScore;
}
