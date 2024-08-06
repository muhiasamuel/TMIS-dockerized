package com.example.talent_man.controllers.succession;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SuccAssDto {
    private String assName;
    private String assDescription;
    private double userScore;
    private double managerScore;
}
