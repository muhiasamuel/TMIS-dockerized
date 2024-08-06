package com.example.talent_man.models;

import com.example.talent_man.models.composite_keys.UserAnswerKey;
import com.example.talent_man.models.user.Employee;
import com.example.talent_man.models.user.Manager;
import com.example.talent_man.models.user.User;
import com.example.talent_man.utils.Constants;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Entity
@NoArgsConstructor
@Table(name = "user_question_answers")
public class UserQuestionAnswer implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "question_id", columnDefinition = "INTEGER")
    private int questionId;

    @Column(name = "employee_id", columnDefinition = "INTEGER")
    private int employeeId;

    @Column(name = "user_selected_choice_id", columnDefinition = "INTEGER")
    private int userChoiceId;

    @Column(name = "manager_id", columnDefinition = "INTEGER")
    private int managerId;

    @Column(name = "manager_selected_choice_id", columnDefinition = "INTEGER")
    private int mangerChoiceId;
    @Column(name = "assessment_id", columnDefinition = "INTEGER")
    private int assessmentId;

    @Column(name = "assessment_status", columnDefinition = "CHAR")
    private char status = Constants.UN_ASSESSED_ASSESSMENT;




//    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL) //We want the selected choice on retrieval
//    @JoinColumn(name = "choice_id", referencedColumnName = "choice_id")  // This means Foreign key will be created only in the Selected_answers table i.e. extra column 'choice_id' will be created in the table
//    private Choice choices;

}
