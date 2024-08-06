package com.example.talent_man.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Attributes implements Serializable {
    private String potentialAttributeName;
    private String potentialAttributeDescription;
    private LocalDateTime createdAt;

    public Attributes(String potentialAttributeName, String potentialAttributeDescription) {
    }
}
