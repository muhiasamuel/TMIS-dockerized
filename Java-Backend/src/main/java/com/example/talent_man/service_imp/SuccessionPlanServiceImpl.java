package com.example.talent_man.service_imp;

import com.example.talent_man.dto.succession.ProposedInterventionDto;
import com.example.talent_man.dto.succession.ReadyUserDto;
import com.example.talent_man.dto.succession.SuccessionPlanDto;
import com.example.talent_man.dto.succession.SuccessorDevelopmentNeedDto;
import com.example.talent_man.models.Department;
import com.example.talent_man.models.Position;
import com.example.talent_man.models.succession.*;
import com.example.talent_man.models.user.User;
import com.example.talent_man.repos.DepartmentRepo;
import com.example.talent_man.repos.PositionRepo;
import com.example.talent_man.repos.SuccessionDriverRepo;
import com.example.talent_man.repos.succession.ProposedInterventionRepository;
import com.example.talent_man.repos.succession.ReadyUsersRepository;
import com.example.talent_man.repos.succession.SuccessionPlanRepository;
import com.example.talent_man.repos.succession.SuccessorDevelopmentNeedRepository;
import com.example.talent_man.repos.user.UserRepo;
import com.example.talent_man.services.SuccessionPlanService;
import com.example.talent_man.utils.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SuccessionPlanServiceImpl implements SuccessionPlanService {

    @Autowired
    private SuccessionPlanRepository successionPlanRepository;

    @Autowired
    private DepartmentRepo departmentRepository;

    @Autowired
    private UserRepo userRepository;

    @Autowired
    private ProposedInterventionRepository proposedInterventionRepository;

    @Autowired
    private SuccessorDevelopmentNeedRepository successorDevelopmentNeedRepository;

    @Autowired
    private PositionRepo positionRepository;

    @Autowired
    private SuccessionDriverRepo successionDriverRepo;

    @Autowired
    private ReadyUsersRepository readyUsersRepository;

    @Override
    public SuccessionPlanDto createSuccessionPlan(SuccessionPlanDto successionPlanDto) {
        try {
            // Validate that at least one of the ReadyUser lists or External Successor is filled
            if ((successionPlanDto.getReadyNow() == null || successionPlanDto.getReadyNow().isEmpty()) &&
                    (successionPlanDto.getReadyAfterTwoYears() == null || successionPlanDto.getReadyAfterTwoYears().isEmpty()) &&
                    (successionPlanDto.getReadyMoreThanTwoYears() == null || successionPlanDto.getReadyMoreThanTwoYears().isEmpty()) &&
                    (successionPlanDto.getExternalSuccessor() == null || isExternalSuccessorEmpty(successionPlanDto.getExternalSuccessor()))) {
                throw new IllegalArgumentException("At least one of the Ready User lists or External Successor must be filled.");
            }

            // Fetch the Department
            Department department = departmentRepository.findById(successionPlanDto.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department not found"));

            // Fetch the Position
            Position position = positionRepository.findById(successionPlanDto.getPositionId())
                    .orElseThrow(() -> new ResourceNotFoundException("Position not found"));

            // Fetch the Succession Driver
            SuccessionDrivers drivers = successionDriverRepo.findById(successionPlanDto.getDriverId())
                    .orElseThrow(() -> new ResourceNotFoundException("Succession Driver not found"));

            // Check if the Position belongs to the Department
            if (!department.getDepartmentPositions().contains(position)) {
                throw new ResourceNotFoundException("Position does not belong to the Department");
            }

            // Fetch the Current Role Holder
            User currentRoleHolder = userRepository.findById(successionPlanDto.getCurrentRoleHolderId())
                    .orElseThrow(() -> new ResourceNotFoundException("Current Role Holder not found"));

            if (!currentRoleHolder.getPosition().equals(position)) {
                throw new ResourceNotFoundException("Current Role Holder does not hold the specified Position");
            }

            // Create and populate SuccessionPlan entity
            SuccessionPlan successionPlan = new SuccessionPlan();
            successionPlan.setDepartment(department);
            successionPlan.setPosition(position);
            successionPlan.setPlanDriver(drivers);
            successionPlan.setRetentionRiskRating(successionPlanDto.getRetentionRiskRating());
            successionPlan.setCurrentRoleHolder(currentRoleHolder);
            successionPlan.setReadyNow(new ArrayList<>());
            successionPlan.setReadyAfterTwoYears(new ArrayList<>());
            successionPlan.setReadyMoreThanTwoYears(new ArrayList<>());
            successionPlan.setProposedInterventions(new ArrayList<>());
            successionPlan.setSuccessorDevelopmentNeeds(new ArrayList<>());

            // Set External Successor if provided
            if (successionPlanDto.getExternalSuccessor() != null) {
                ExternalSuccessor externalSuccessor = new ExternalSuccessor();
                ExternalSuccessorDto externalDto = successionPlanDto.getExternalSuccessor();
                externalSuccessor.setName(externalDto.getName());
                externalSuccessor.setContactInfo(externalDto.getContactInfo());
                externalSuccessor.setCurrentPosition(externalDto.getCurrentPosition());
                externalSuccessor.setCurrentCompany(externalDto.getCurrentCompany());
                externalSuccessor.setReasonForSelection(externalDto.getReasonForSelection());
                externalSuccessor.setExpectedStartDate(externalDto.getExpectedStartDate());
                successionPlan.setExternalSuccessor(externalSuccessor);
            }

            // Save and link ReadyUsers
            List<ReadyUsers> readyNow = saveReadyUsers(successionPlanDto.getReadyNow(), successionPlan, "Now");
            List<ReadyUsers> readyAfterTwoYears = saveReadyUsers(successionPlanDto.getReadyAfterTwoYears(), successionPlan, "1-2 Years");
            List<ReadyUsers> readyMoreThanTwoYears = saveReadyUsers(successionPlanDto.getReadyMoreThanTwoYears(), successionPlan, "More than 2 Years");

            // Save Succession Plan first to get an ID for its relationships
            SuccessionPlan savedSuccessionPlan = successionPlanRepository.save(successionPlan);

            // Save ReadyUsers with the updated Succession Plan
            readyNow.forEach(readyUser -> readyUser.setSuccessionPlan(savedSuccessionPlan));
            readyAfterTwoYears.forEach(readyUser -> readyUser.setSuccessionPlan(savedSuccessionPlan));
            readyMoreThanTwoYears.forEach(readyUser -> readyUser.setSuccessionPlan(savedSuccessionPlan));

            readyUsersRepository.saveAll(readyNow);
            readyUsersRepository.saveAll(readyAfterTwoYears);
            readyUsersRepository.saveAll(readyMoreThanTwoYears);

            // Save Proposed Interventions
            List<ProposedIntervention> interventions = saveProposedInterventions(successionPlanDto.getProposedInterventions(), savedSuccessionPlan);
            proposedInterventionRepository.saveAll(interventions);

            // Save Successor Development Needs
            List<SuccessorDevelopmentNeed> developmentNeeds = saveSuccessorDevelopmentNeeds(successionPlanDto.getSuccessorDevelopmentNeeds(), savedSuccessionPlan);
            successorDevelopmentNeedRepository.saveAll(developmentNeeds);

            return convertToDto(savedSuccessionPlan);
        } catch (Exception e) {
            throw new IllegalArgumentException("There was an error adding the succession plan: " + e.getMessage(), e);
        }
    }

    // Helper method to check if the ExternalSuccessorDto is empty
    private boolean isExternalSuccessorEmpty(ExternalSuccessorDto dto) {
        return dto.getName() == null || dto.getName().isEmpty() &&
                dto.getContactInfo() == null || dto.getContactInfo().isEmpty() &&
                dto.getCurrentPosition() == null || dto.getCurrentPosition().isEmpty() &&
                dto.getCurrentCompany() == null || dto.getCurrentCompany().isEmpty();
    }

    @Override
    public List<SuccessionPlanDto> getAllSuccessionPlans() {
        return successionPlanRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public SuccessionPlanDto getSuccessionPlanById(int id) {
        SuccessionPlan successionPlan = successionPlanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Succession Plan not found"));
        return convertToDto(successionPlan);
    }

    @Override
    public SuccessionPlanDto updateSuccessionPlan(int id, SuccessionPlanDto successionPlanDto) {
        SuccessionPlan existingPlan = successionPlanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Succession Plan not found"));

        Department department = departmentRepository.findById(successionPlanDto.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));

        Position position = positionRepository.findById(successionPlanDto.getPositionId())
                .orElseThrow(() -> new ResourceNotFoundException("Position not found"));

        SuccessionDrivers drivers = successionDriverRepo.findById(successionPlanDto.getDriverId())
                .orElseThrow(() -> new ResourceNotFoundException("Succession Driver not found"));

        if (!department.getDepartmentPositions().contains(position)) {
            throw new ResourceNotFoundException("Position does not belong to the Department");
        }

        User currentRoleHolder = userRepository.findById(successionPlanDto.getCurrentRoleHolderId())
                .orElseThrow(() -> new ResourceNotFoundException("Current Role Holder not found"));

        if (!currentRoleHolder.getPosition().equals(position)) {
            throw new ResourceNotFoundException("Current Role Holder does not hold the specified Position");
        }

        existingPlan.setDepartment(department);
        existingPlan.setPosition(position);
        existingPlan.setPlanDriver(drivers);
        existingPlan.setRetentionRiskRating(successionPlanDto.getRetentionRiskRating());
        existingPlan.setCurrentRoleHolder(currentRoleHolder);

        existingPlan.getReadyNow().clear();
        existingPlan.getReadyAfterTwoYears().clear();
        existingPlan.getReadyMoreThanTwoYears().clear();
        existingPlan.getProposedInterventions().clear();
        existingPlan.getSuccessorDevelopmentNeeds().clear();

        List<ReadyUsers> readyNow = saveReadyUsers(successionPlanDto.getReadyNow(), existingPlan, "Now");
        List<ReadyUsers> readyAfterTwoYears = saveReadyUsers(successionPlanDto.getReadyAfterTwoYears(), existingPlan, "1-2 Years");
        List<ReadyUsers> readyMoreThanTwoYears = saveReadyUsers(successionPlanDto.getReadyMoreThanTwoYears(), existingPlan, "More than 2 Years");

        readyUsersRepository.saveAll(readyNow);
        readyUsersRepository.saveAll(readyAfterTwoYears);
        readyUsersRepository.saveAll(readyMoreThanTwoYears);

        List<ProposedIntervention> interventions = saveProposedInterventions(successionPlanDto.getProposedInterventions(), existingPlan);
        proposedInterventionRepository.saveAll(interventions);

        List<SuccessorDevelopmentNeed> developmentNeeds = saveSuccessorDevelopmentNeeds(successionPlanDto.getSuccessorDevelopmentNeeds(), existingPlan);
        successorDevelopmentNeedRepository.saveAll(developmentNeeds);

        return convertToDto(existingPlan);
    }

    @Override
    public void deleteSuccessionPlan(int id) {
        SuccessionPlan successionPlan = successionPlanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Succession Plan not found"));
        successionPlanRepository.delete(successionPlan);
    }

    private List<ReadyUsers> saveReadyUsers(List<ReadyUserDto> readyUserDtos, SuccessionPlan successionPlan, String readinessLevel) {
        return readyUserDtos.stream()
                .map(dto -> {
                    User user = userRepository.findById(dto.getUserId())
                            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

                    ReadyUsers readyUser = new ReadyUsers();
                    readyUser.setUser(user);
                    readyUser.setReadinessLevel(readinessLevel);
                    readyUser.setSuccessionPlan(successionPlan);

                    // Save the readyUser first to get an ID for its relationships
                    ReadyUsers savedReadyUser = readyUsersRepository.save(readyUser);

                    // Link ProposedInterventions to the saved readyUser
                    List<ProposedIntervention> interventions = dto.getProposedInterventions().stream()
                            .map(interventionDto -> {
                                ProposedIntervention intervention = new ProposedIntervention();
                                intervention.setType(interventionDto.getType());
                                intervention.setDescription(interventionDto.getDescription());
                                intervention.setStatus(interventionDto.getStatus());
                                intervention.setStartDate(interventionDto.getStartDate());
                                intervention.setEndDate(interventionDto.getEndDate());
                                intervention.setReadyUser(savedReadyUser);
                                return intervention;
                            })
                            .collect(Collectors.toList());

                    // Save interventions and set to the readyUser
                    readyUser.setProposedInterventions(proposedInterventionRepository.saveAll(interventions));

                    // Link SuccessorDevelopmentNeeds to the saved readyUser
                    List<SuccessorDevelopmentNeed> developmentNeeds = dto.getDevelopmentNeeds().stream()
                            .map(needDto -> {
                                SuccessorDevelopmentNeed need = new SuccessorDevelopmentNeed();
                                need.setNeedType(needDto.getNeedType());
                                need.setDescription(needDto.getDescription());
                                need.setReadyUser(savedReadyUser);
                                return need;
                            })
                            .collect(Collectors.toList());

                    // Save development needs and set to the readyUser
                    readyUser.setDevelopmentNeeds(successorDevelopmentNeedRepository.saveAll(developmentNeeds));

                    return readyUser;
                })
                .collect(Collectors.toList());
    }

    private List<ProposedIntervention> saveProposedInterventions(List<ProposedInterventionDto> interventionDtos, SuccessionPlan successionPlan) {
        return interventionDtos.stream()
                .map(dto -> {
                    ProposedIntervention intervention = new ProposedIntervention();
                    intervention.setType(dto.getType());
                    intervention.setDescription(dto.getDescription());
                    intervention.setStatus(dto.getStatus());
                    intervention.setStartDate(dto.getStartDate());
                    intervention.setEndDate(dto.getEndDate());
                    intervention.setSuccessionPlan(successionPlan);
                    return intervention;
                })
                .collect(Collectors.toList());
    }

    private List<SuccessorDevelopmentNeed> saveSuccessorDevelopmentNeeds(List<SuccessorDevelopmentNeedDto> needDtos, SuccessionPlan successionPlan) {
        return needDtos.stream()
                .map(dto -> {
                    SuccessorDevelopmentNeed need = new SuccessorDevelopmentNeed();
                    need.setNeedType(dto.getNeedType());
                    need.setDescription(dto.getDescription());
                    need.setSuccessionPlan(successionPlan);
                    return need;
                })
                .collect(Collectors.toList());
    }

    private SuccessionPlanDto convertToDto(SuccessionPlan successionPlan) {
        SuccessionPlanDto dto = new SuccessionPlanDto();
        dto.setDepartmentId(successionPlan.getDepartment().getDepId());
        dto.setPositionId(successionPlan.getPosition().getPId());
        dto.setRetentionRiskRating(successionPlan.getRetentionRiskRating());
        dto.setCurrentRoleHolderId(successionPlan.getCurrentRoleHolder().getUserId());
        dto.setDriverId(successionPlan.getPlanDriver().getDriverId());

        dto.setReadyNow(successionPlan.getReadyNow() != null ?
                successionPlan.getReadyNow().stream().map(this::convertToReadyUserDto).collect(Collectors.toList()) :
                new ArrayList<>());

        dto.setReadyAfterTwoYears(successionPlan.getReadyAfterTwoYears() != null ?
                successionPlan.getReadyAfterTwoYears().stream().map(this::convertToReadyUserDto).collect(Collectors.toList()) :
                new ArrayList<>());

        dto.setReadyMoreThanTwoYears(successionPlan.getReadyMoreThanTwoYears() != null ?
                successionPlan.getReadyMoreThanTwoYears().stream().map(this::convertToReadyUserDto).collect(Collectors.toList()) :
                new ArrayList<>());

        dto.setProposedInterventions(successionPlan.getProposedInterventions() != null ?
                successionPlan.getProposedInterventions().stream().map(this::convertToProposedInterventionDto).collect(Collectors.toList()) :
                new ArrayList<>());

        dto.setSuccessorDevelopmentNeeds(successionPlan.getSuccessorDevelopmentNeeds() != null ?
                successionPlan.getSuccessorDevelopmentNeeds().stream().map(this::convertToSuccessorDevelopmentNeedDto).collect(Collectors.toList()) :
                new ArrayList<>());

        // Set External Successor if present
        if (successionPlan.getExternalSuccessor() != null) {
            ExternalSuccessorDto externalSuccessorDto = getExternalSuccessorDto(successionPlan);
            dto.setExternalSuccessor(externalSuccessorDto);
        }

        return dto;
    }

    private static ExternalSuccessorDto getExternalSuccessorDto(SuccessionPlan successionPlan) {
        ExternalSuccessor externalSuccessor = successionPlan.getExternalSuccessor();
        ExternalSuccessorDto externalSuccessorDto = new ExternalSuccessorDto();
        externalSuccessorDto.setName(externalSuccessor.getName());
        externalSuccessorDto.setContactInfo(externalSuccessor.getContactInfo());
        externalSuccessorDto.setCurrentPosition(externalSuccessor.getCurrentPosition());
        externalSuccessorDto.setCurrentCompany(externalSuccessor.getCurrentCompany());
        externalSuccessorDto.setReasonForSelection(externalSuccessor.getReasonForSelection());
        externalSuccessorDto.setExpectedStartDate(externalSuccessor.getExpectedStartDate());
        return externalSuccessorDto;
    }

    private ReadyUserDto convertToReadyUserDto(ReadyUsers readyUser) {
        ReadyUserDto dto = new ReadyUserDto();
        dto.setId(readyUser.getId());
        dto.setUserId(readyUser.getUser().getUserId());
        dto.setReadinessLevel(readyUser.getReadinessLevel());

        dto.setProposedInterventions(readyUser.getProposedInterventions() != null ?
                readyUser.getProposedInterventions().stream().map(this::convertToProposedInterventionDto).collect(Collectors.toList()) :
                new ArrayList<>());

        dto.setDevelopmentNeeds(readyUser.getDevelopmentNeeds() != null ?
                readyUser.getDevelopmentNeeds().stream().map(this::convertToSuccessorDevelopmentNeedDto).collect(Collectors.toList()) :
                new ArrayList<>());

        return dto;
    }

    private ProposedInterventionDto convertToProposedInterventionDto(ProposedIntervention intervention) {
        ProposedInterventionDto dto = new ProposedInterventionDto();
        dto.setId(intervention.getId());
        dto.setType(intervention.getType());
        dto.setDescription(intervention.getDescription());
        dto.setStatus(intervention.getStatus());
        dto.setStartDate(intervention.getStartDate());
        dto.setEndDate(intervention.getEndDate());
        return dto;
    }

    private SuccessorDevelopmentNeedDto convertToSuccessorDevelopmentNeedDto(SuccessorDevelopmentNeed need) {
        SuccessorDevelopmentNeedDto dto = new SuccessorDevelopmentNeedDto();
        dto.setId(need.getId());
        dto.setNeedType(need.getNeedType());
        dto.setDescription(need.getDescription());
        return dto;
    }
}
