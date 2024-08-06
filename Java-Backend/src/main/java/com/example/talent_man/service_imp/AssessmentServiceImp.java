package com.example.talent_man.service_imp;

import com.example.talent_man.models.Assessment;
import com.example.talent_man.models.PotentialAttribute;
import com.example.talent_man.repos.AssessmentRepo;
import com.example.talent_man.repos.PotentialAttributeRepo;
import com.example.talent_man.services.AssessmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AssessmentServiceImp implements AssessmentService {
    @Autowired
    private AssessmentRepo repo;
    private PotentialAttributeRepo pRepo;
    @Override
    public Assessment addAss(Assessment ass) {
        return repo.save(ass);
    }

    @Override
    public Assessment getById(int id) {
        return repo.getReferenceById(id);
    }

    @Override
    public List<Assessment> getAttAssess(int attId) {
        PotentialAttribute att = pRepo.getReferenceById(attId);
        return new ArrayList<>(att.getAssessments());
    }

}
