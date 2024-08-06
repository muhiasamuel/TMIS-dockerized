package com.example.talent_man.service_imp;

import com.example.talent_man.models.Choice;
import com.example.talent_man.repos.ChoiceRepo;
import com.example.talent_man.services.ChoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChoiceServiceImp implements ChoiceService {
    @Autowired
    private ChoiceRepo cRepo;

    @Override
    public Choice saveChoice(Choice c) {
        return cRepo.save(c);
    }

    @Override
    public Choice getById(int choiceId) {
        return cRepo.getReferenceById(choiceId);
    }
}
