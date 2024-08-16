package com.example.talent_man.service_imp;

import com.example.talent_man.dto.assessment.ChoiceDto;
import com.example.talent_man.dto.assessment.QuestionDto;
import com.example.talent_man.models.AssessmentQuestion;
import org.springframework.transaction.annotation.Transactional;

import com.example.talent_man.models.PotentialAttribute;
import com.example.talent_man.repos.AssessmentRepo;
import com.example.talent_man.repos.PotentialAttributeRepo;
import com.example.talent_man.services.PotentialAttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PotentialAttributeServiceImp implements PotentialAttributeService {
    @Autowired
    private PotentialAttributeRepo repo;

    @Autowired
    private AssessmentRepo assessmentRepo;

    @Override
    @Transactional
    public PotentialAttribute addPotentialAttribute(PotentialAttribute potentialAttribute) {
        return repo.save(potentialAttribute);
    }

    public List<PotentialAttribute> saveAllAttributes(Set<PotentialAttribute> attributes) {
        return repo.saveAll(attributes);
    }

    @Override
    public PotentialAttribute getPotentialAttributeById(int id) {
        return repo.getReferenceById(id);
    }

    @Override
    public List<PotentialAttribute> getAllPotentialAttributes() {
        return repo.findAll();
    }

    @Override
    public PotentialAttribute updatePotentialAttributeById(int id, PotentialAttribute potentialAttribute) {
        return null;
    }
    @Override
    public PotentialAttribute deletePotentialAttributeById(int id) {
        return null;
    }

    @Override
    public List<QuestionDto> getQuestionsByAttributeId(int attributeId) {
        PotentialAttribute potentialAttribute = repo.findById(attributeId)
                .orElse(null); // Or use a custom method if needed

        if (potentialAttribute == null) {
            return null; // Handle this case in the controller
        }

        Set<AssessmentQuestion> questions = potentialAttribute.getQuestions();
        return questions.stream()
                .map(question -> {
                    QuestionDto dto = new QuestionDto();
                    dto.setAssessmentQuestionId(question.getAssessmentQuestionId());
                    dto.setAssessmentQuestionDescription(question.getAssessmentQuestionDescription());

                    Set<ChoiceDto> choiceDtos = question.getChoices().stream()
                            .map(choice -> {
                                ChoiceDto choiceDto = new ChoiceDto();
                                choiceDto.setChoiceId(choice.getChoiceId());
                                choiceDto.setChoiceName(choice.getChoiceName());
                                choiceDto.setChoiceValue(Integer.parseInt(choice.getChoiceValue()));
                                return choiceDto;
                            })
                            .collect(Collectors.toSet());

                    dto.setChoices(choiceDtos);
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
