package com.example.talent_man.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class PotentialReqAttributeDto implements Serializable {
    private int potentialAttributeId;
    private String potentialAttributeName;
    private String potentialAttributeDescription;
    private LocalDateTime createdAt;
}
