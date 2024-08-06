package com.example.talent_man.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Answer implements Serializable {
    private int userId;
    private int choiceId;
    private int managerId;
    private int questionId;

}
