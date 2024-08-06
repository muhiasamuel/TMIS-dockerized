package com.example.talent_man.services;

import com.example.talent_man.models.Choice;

public interface ChoiceService {

    Choice saveChoice(Choice c);
    //Read
    Choice getById(int choiceId);
}
