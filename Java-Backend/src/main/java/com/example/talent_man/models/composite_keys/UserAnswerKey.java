package com.example.talent_man.models.composite_keys;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import lombok.Data;

import java.io.Serializable;
@Data
@Embeddable //creating a composite key
public class UserAnswerKey implements Serializable {

    @JoinColumn(name = "user_id")
    private int employeeId;


    @JoinColumn(name = "choice_id")
    private int choiceId;
}
