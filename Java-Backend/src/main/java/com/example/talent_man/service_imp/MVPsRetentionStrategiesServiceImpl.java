package com.example.talent_man.service_imp;

import com.example.talent_man.dto.HiposInterventionDTO;
import com.example.talent_man.dto.MVPsRetentionStrategiesDto;
import com.example.talent_man.models.HiposIntervention;
import com.example.talent_man.models.MVPsRetentionStrategies;
import com.example.talent_man.models.user.Employee;
import com.example.talent_man.models.user.User;
import com.example.talent_man.repos.MVPsInterventionRepo;
import com.example.talent_man.repos.user.UserRepo;
import com.example.talent_man.services.MVPsRetentionStrategiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MVPsRetentionStrategiesServiceImpl implements MVPsRetentionStrategiesService {

    @Autowired
    MVPsInterventionRepo MVPsRetentionRepo;

    @Autowired
    UserRepo userRepo;
    @Override
    public List<MVPsRetentionStrategies> createMVPsStrategies(List<MVPsRetentionStrategiesDto> dto, int employeeId){
        try {
            User user = userRepo.findById(employeeId)
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + employeeId));

            List<MVPsRetentionStrategies> strategies = new ArrayList<>();

            for (MVPsRetentionStrategiesDto mvPsRetentionStrategiesDto : dto) {
                MVPsRetentionStrategies strategies1 = new MVPsRetentionStrategies();
                strategies1.setRetentionStrategies(mvPsRetentionStrategiesDto.getRetentionStrategies());
                strategies1.setUser(user);
                strategies.add(strategies1);
            }

            return MVPsRetentionRepo.saveAll(strategies);
        } catch (Exception e) {
            throw new IllegalArgumentException("An error occurred when saving MVPs Retention Strategies: " + e.getMessage());
        }
    }

}
