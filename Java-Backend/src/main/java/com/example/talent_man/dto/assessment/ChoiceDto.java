package com.example.talent_man.dto.assessment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChoiceDto implements Serializable {
    private int choiceId;
    private int choiceValue;
    private String choiceName;
}
