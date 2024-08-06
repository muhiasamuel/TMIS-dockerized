package com.example.talent_man.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignInRequest implements Serializable {
    private String UserFullName;
    private String password;
    private int roleId;
}
