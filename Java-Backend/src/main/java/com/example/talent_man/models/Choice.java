package com.example.talent_man.models;

import com.example.talent_man.models.composite_keys.UserAnswerKey;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class Choice implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "choice_id", columnDefinition = "INTEGER")
    private int choiceId;

    @Column(name = "choice_name", columnDefinition = "VARCHAR(255)")
    private String choiceName;
    @Column(name = "choice_value", columnDefinition = "VARCHAR(255)")
    private String choiceValue; //Rem to convert back to ints.

   // @OneToMany(mappedBy = "choice")
   // Set<UserQuestionAnswer> userAnswers;


}
