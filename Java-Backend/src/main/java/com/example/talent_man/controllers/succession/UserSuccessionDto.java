package com.example.talent_man.controllers.succession;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSuccessionDto {
    private int id;
    private String name;
    private double attScore;
}
