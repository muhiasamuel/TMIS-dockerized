package com.example.talent_man.dto.assessment;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PotentialAttributeDto implements Serializable {
    private int potentialAttributeId;
    private String attributeName;
    private List<QuestionDto> questions;
}
