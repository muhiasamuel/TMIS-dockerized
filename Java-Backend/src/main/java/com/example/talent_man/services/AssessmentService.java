package com.example.talent_man.services;

import com.example.talent_man.models.Assessment;

import java.util.List;

public interface AssessmentService {
    //create
    Assessment addAss(Assessment ass);

    //Read
    Assessment getById(int id);

    //REad all assignments of an attribute
    List<Assessment> getAttAssess(int attId);
}
