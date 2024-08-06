package com.example.talent_man.controllers.succession;

import com.example.talent_man.models.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SuccAttDto {
    private User user;
    private String attName;
    private List<SuccAssDto> assess;
    private double score;
}
