package com.example.talent_man.service_imp;

import com.example.talent_man.dto.CriticalRolesAssessmentDto;
import com.example.talent_man.dto.RoleDetailsWithStrategiesDTO;
import com.example.talent_man.dto.RolesStrategiesDto;
import com.example.talent_man.models.CriticalRolesStrategies;
import com.example.talent_man.models.Position;
import com.example.talent_man.models.RolesAssessment;
import com.example.talent_man.models.user.Manager;
import com.example.talent_man.repos.CriticalRolesAssessmentRepo;
import com.example.talent_man.repos.PositionRepo;
import com.example.talent_man.repos.RolesStrategiesRepo;
import com.example.talent_man.repos.user.ManagerRepo;
import com.example.talent_man.services.CriticalRolesAssessmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CriticalRolesServiceImpl implements CriticalRolesAssessmentService {

    @Autowired
    private CriticalRolesAssessmentRepo rolesAssessmentRepository;

    @Autowired
    private ManagerRepo managerRepo;

    @Autowired
    private PositionRepo positionRepo;
    @Autowired
    private RolesStrategiesRepo repo;
    public RolesAssessment createRolesAssessment(CriticalRolesAssessmentDto requestDto, int ManagerId) {
        RolesAssessment assessment = new RolesAssessment();
        Position position = positionRepo.findByPositionName(requestDto.getRoleName());
        try {
            assessment.setRoleId(position.getPId());
            assessment.setRoleName(requestDto.getRoleName());
            assessment.setCurrentState(requestDto.getCurrentState());
            assessment.setAverageRating(requestDto.getAverageRating());
            assessment.setRiskImpact(requestDto.getRiskImpact());
            assessment.setStrategicImportance(requestDto.getStrategicImportance());
            assessment.setVacancyRisk(requestDto.getVacancyRisk());
            assessment.setImpactOnOperation(requestDto.getImpactOnOperation());
            assessment.setSkillExperience(requestDto.getSkillExperience());
            Manager manager = managerRepo.findById(ManagerId)
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + ManagerId));
            assessment.setAddedBy(manager);
            if (requestDto.getAverageRating() < 0 || requestDto.getAverageRating() > 5) {
                throw new IllegalArgumentException("Average rating must be between 0 and 5");
            }
            if (isAnyFieldEmpty(requestDto)) {
                throw new IllegalArgumentException("One or more fields in the updated skill data are empty");
            }
            if (requestDto.getAverageRating() > 3.5) {
                // Add strategies
                    // Add strategies
                    for (RolesStrategiesDto strategyDto : requestDto.getRoleDevelopmentStrategies()) {
                        CriticalRolesStrategies strategy = new CriticalRolesStrategies();
                        strategy.setStrategyName(strategyDto.getStrategyName());
                        strategy.setAddedBy(manager);
                        // Set other strategy properties if needed
                        // Associate the strategy with the assessment
                        strategy.setRoleAssessment(assessment);
                        assessment.getRoleDevelopmentStrategies().add(strategy);
                    }
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Validation failed: " + e.getMessage());
        }
        return rolesAssessmentRepository.save(assessment);
    }

    public List<CriticalRolesStrategies> addStrategiesToRolesAssessment(Long rolesAssessmentId, int managerId, List<RolesStrategiesDto> strategiesDtoList) {
        try {
            Manager manager = managerRepo.findById(managerId)
                    .orElseThrow(() -> new RuntimeException("User not found with ID: " + managerId));

            RolesAssessment assessment = rolesAssessmentRepository.findById(rolesAssessmentId)
                    .orElseThrow(() -> new IllegalArgumentException("No critical role found with ID: " + rolesAssessmentId));

            if (assessment == null) {
                throw new IllegalArgumentException("No critical role found with ID: " + rolesAssessmentId);
            }

            if (assessment.getAverageRating() < 3.5) {
                throw new IllegalArgumentException("this role is considered not critical " + rolesAssessmentId);
            }

            List<CriticalRolesStrategies> strategiesList = new ArrayList<>();

            for (RolesStrategiesDto strategyDto : strategiesDtoList) {
                CriticalRolesStrategies strategy = new CriticalRolesStrategies();
                strategy.setStrategyName(strategyDto.getStrategyName());
                strategy.setRoleAssessment(assessment);
                strategy.setAddedBy(manager);
                strategiesList.add(strategy);
            }

            return repo.saveAll(strategiesList);
        } catch (Exception e) {
            throw new IllegalArgumentException("An error occurred when saving strategies: " + e.getMessage());
        }
    }

    // get critical role by id

    public RoleDetailsWithStrategiesDTO getRoleById(Long roleId) {
        Optional<RolesAssessment> roleOptional = rolesAssessmentRepository.findById(roleId);
        if (roleOptional.isEmpty()) {
            throw new IllegalArgumentException("Critical role not found with ID: " + roleId);
        }

        RolesAssessment role = roleOptional.get();
        List<CriticalRolesStrategies> strategies = role.getRoleDevelopmentStrategies();

        RoleDetailsWithStrategiesDTO dto = new RoleDetailsWithStrategiesDTO();
        dto.setRole(role);
        dto.setStrategies(strategies);


        return dto;
    }


    //edit a role

    public RolesAssessment editRolesAssessment(CriticalRolesAssessmentDto requestDto, int userId, Long criticalRoleId) {
        RolesAssessment assessment = rolesAssessmentRepository.findById(criticalRoleId)
                .orElseThrow(() -> new IllegalArgumentException("Role not found with ID: " + criticalRoleId));
        Position position = positionRepo.findByPositionName(requestDto.getRoleName());
        try {
            assessment.setRoleId(position.getPId());

            assessment.setRoleName(requestDto.getRoleName());
            assessment.setCurrentState(requestDto.getCurrentState());
            assessment.setAverageRating(requestDto.getAverageRating());
            assessment.setRiskImpact(requestDto.getRiskImpact());
            assessment.setStrategicImportance(requestDto.getStrategicImportance());
            assessment.setVacancyRisk(requestDto.getVacancyRisk());
            assessment.setImpactOnOperation(requestDto.getImpactOnOperation());
            assessment.setSkillExperience(requestDto.getSkillExperience());

            Manager manager = managerRepo.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
            if (!assessment.getAddedBy().equals(manager)) {
                throw new IllegalArgumentException("You are not authorized to update this skill");
            }
            if (requestDto.getAverageRating() < 0 || requestDto.getAverageRating() > 5) {
                throw new IllegalArgumentException("Average rating must be between 0 and 5");
            }
            if (isAnyFieldEmpty(requestDto)) {
                throw new IllegalArgumentException("One or more fields in the updated skill data are empty");
            }
            assessment.setAddedBy(manager);
            if (requestDto.getAverageRating() > 3.5) {
                // Add strategies
                // Add strategies
                for (RolesStrategiesDto strategyDto : requestDto.getRoleDevelopmentStrategies()) {
                    CriticalRolesStrategies strategy = new CriticalRolesStrategies();
                    strategy.setStrategyName(strategyDto.getStrategyName());
                    strategy.setAddedBy(manager);
                    // Set other strategy properties if needed
                    // Associate the strategy with the assessment
                    strategy.setRoleAssessment(assessment);
                    assessment.getRoleDevelopmentStrategies().add(strategy);
                }
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Validation failed: " + e.getMessage());
        }
        return rolesAssessmentRepository.save(assessment);
    }


    //validating if all fields are filled
    private boolean isAnyFieldEmpty(CriticalRolesAssessmentDto role) {
        return role.getRoleName() == null || role.getRoleName().isEmpty() ||
                role.getCurrentState() == null || role.getCurrentState().isEmpty()||
                role.getRiskImpact() == null || role.getRiskImpact().isEmpty()||
                role.getStrategicImportance() == null || role.getStrategicImportance().isEmpty()||
                role.getVacancyRisk() == null || role.getVacancyRisk().isEmpty()||
                role.getImpactOnOperation() ==null || role.getImpactOnOperation().isEmpty()||
                role.getSkillExperience() == null || role.getSkillExperience().isEmpty();
        // Add conditions for other fields as needed
    }


}