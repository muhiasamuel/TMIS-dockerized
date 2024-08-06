package com.example.talent_man.services;

import com.example.talent_man.models.PotentialAttribute;

import java.util.List;

public interface PotentialAttributeService {
    //Create
    PotentialAttribute addPotentialAttribute(PotentialAttribute potentialAttribute);
    //Read
    PotentialAttribute getPotentialAttributeById(int id);
    List<PotentialAttribute> getAllPotentialAttributes();
    //update
    PotentialAttribute updatePotentialAttributeById(int id, PotentialAttribute potentialAttribute);
    //delete
    PotentialAttribute deletePotentialAttributeById(int id);

}
