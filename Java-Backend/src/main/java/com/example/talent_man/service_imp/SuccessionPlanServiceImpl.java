package com.example.talent_man.service_imp;

import com.example.talent_man.controllers.succession.CriticalRoleCheckForSuccessionDto;
import com.example.talent_man.controllers.succession.SuccessionPlanResponseDto;
import com.example.talent_man.dto.succession.ProposedInterventionDto;
import com.example.talent_man.dto.succession.ReadyUserDto;
import com.example.talent_man.dto.succession.SuccessionPlanDto;
import com.example.talent_man.dto.succession.SuccessorDevelopmentNeedDto;
import com.example.talent_man.models.Department;
import com.example.talent_man.models.Position;
import com.example.talent_man.models.RolesAssessment;
import com.example.talent_man.models.succession.*;
import com.example.talent_man.models.user.User;
import com.example.talent_man.repos.CriticalRolesAssessmentRepo;
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
import java.util.Collections;
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
    private CriticalRolesAssessmentRepo criticalRolesAssessmentRepo;
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
                    (successionPlanDto.getExternalSuccessor() == null || successionPlanDto.getExternalSuccessor().isEmpty())) {
                throw new IllegalArgumentException("At least one of the Ready User lists or External Successor must be filled.");
            }

            // Fetch the Department
            Department department = departmentRepository.findById(successionPlanDto.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department not found"));

            // Fetch the Position
            Position position = positionRepository.findById(successionPlanDto.getPositionId())
                    .orElseThrow(() -> new ResourceNotFoundException("Position not found"));

            // Check if the Position belongs to the Department
            if (!department.getDepartmentPositions().contains(position)) {
                throw new ResourceNotFoundException("Position does not belong to the Department");
            }

            // Check if a succession plan already exists for this position in the department
            boolean exists = successionPlanRepository.existsByDepartmentAndPosition(department, position);
            if (exists) {
                throw new IllegalArgumentException("A succession plan for this position already exists in the specified department.");
            }

            // Fetch the Succession Driver
            SuccessionDrivers drivers = successionDriverRepo.findById(successionPlanDto.getDriverId())
                    .orElseThrow(() -> new ResourceNotFoundException("Succession Driver not found"));

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
            if (successionPlanDto.getExternalSuccessor() != null && !successionPlanDto.getExternalSuccessor().isEmpty()) {
                List<ExternalSuccessor> externalSuccessors = new ArrayList<>();

                for (ExternalSuccessorDto externalDto : successionPlanDto.getExternalSuccessor()) {
                    ExternalSuccessor externalSuccessor = new ExternalSuccessor();
                    externalSuccessor.setName(externalDto.getName());
                    externalSuccessor.setContactInfo(externalDto.getContactInfo());
                    externalSuccessor.setCurrentPosition(externalDto.getCurrentPosition());
                    externalSuccessor.setCurrentCompany(externalDto.getCurrentCompany());
                    externalSuccessor.setSuccessionPlan(successionPlan);
                    externalSuccessor.setReasonForSelection(externalDto.getReasonForSelection());
                    externalSuccessor.setExpectedStartDate(externalDto.getExpectedStartDate());

                    externalSuccessors.add(externalSuccessor);
                }

                successionPlan.setExternalSuccessors(externalSuccessors);
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
    public List<SuccessionPlanResponseDto> getSuccessionPlanDetails() {
        List<SuccessionPlanRepository.SuccessionPlanProjection> projections = successionPlanRepository.getSuccessionPlanDetails();
        return projections.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    private SuccessionPlanResponseDto mapToDto(SuccessionPlanRepository.SuccessionPlanProjection projection) {
        SuccessionPlanResponseDto dto = new SuccessionPlanResponseDto();

        dto.setPlanId(projection.getPlanId());
        dto.setRiskRating(projection.getRiskRating());
        dto.setCurrentRoleHolderId(projection.getCurrentRoleHolderId());
        dto.setCurrentRoleHolderName(projection.getCurrentRoleHolderName());
        dto.setDepartmentId(projection.getDepartmentId());
        dto.setDepartmentName(projection.getDepartmentName());
        dto.setPositionId(projection.getPositionId());
        dto.setPositionName(projection.getPositionName());
        dto.setDriverId(projection.getDriverId());
        dto.setDriverName(projection.getDriverName());

        // Mapping the nested lists or objects
        SuccessionPlanResponseDto.ReadyUserDto readyUserDto = new SuccessionPlanResponseDto.ReadyUserDto();
        readyUserDto.setReadyUserName(projection.getReadyUserName());
        readyUserDto.setReadinessLevel(projection.getReadinessLevel());
        dto.setReadyUsers(Collections.singletonList(readyUserDto));  // Assuming only one user for now

        SuccessionPlanResponseDto.InterventionDto interventionDto = new SuccessionPlanResponseDto.InterventionDto();
        interventionDto.setInterventionDescription(projection.getInterventionDescription());
        interventionDto.setInterventionType(projection.getInterventionType());
        interventionDto.setStatus(projection.getInterventionStatus());
        interventionDto.setStartDate(projection.getInterventionStartDate());
        interventionDto.setEndDate(projection.getInterventionEndDate());
        //interventionDto.setInterventionCount(projection.getInterventionCount());
        readyUserDto.setInterventions(Collections.singletonList(interventionDto));  // Assuming one intervention

        SuccessionPlanResponseDto.DevelopmentNeedDto developmentNeedDto = new SuccessionPlanResponseDto.DevelopmentNeedDto();
        developmentNeedDto.setDevelopmentNeedDescription(projection.getDevelopmentNeedDescription());
        developmentNeedDto.setDevelopmentNeedType(projection.getDevelopmentNeedType());
        readyUserDto.setDevelopmentNeeds(Collections.singletonList(developmentNeedDto));  // Assuming one development need

        // Mapping ExternalSuccessorDto
        SuccessionPlanResponseDto.ExternalSuccessorDto externalSuccessorDto = new SuccessionPlanResponseDto.ExternalSuccessorDto();
        externalSuccessorDto.setExternalSuccessorContact(projection.getExternalSuccessorContact());
        externalSuccessorDto.setExternalSuccessorCurrentComp(projection.getExternalSuccessorCurrentComp());
        externalSuccessorDto.setExternalSuccessorPosition(projection.getExternalSuccessorPosition());
        externalSuccessorDto.setExternalSuccessorName(projection.getExternalSuccessorName());
        externalSuccessorDto.setExternalSuccessorSelectionReason(projection.getExternalSuccessorSelectionReason());
        dto.setExternalSuccessors(Collections.singletonList(externalSuccessorDto));
        return dto;
    }

    @Override
    public List<SuccessionPlanResponseDto> getSuccessionPlanById(int id) {
            List<SuccessionPlanRepository.SuccessionPlanProjection> projections = successionPlanRepository.getSuccessionPlanById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Succession Plan not found"));
        return projections.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public List<SuccessionPlanResponseDto> getSuccessionPlanByUserId(int userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        List<SuccessionPlanRepository.SuccessionPlanProjection> projections = successionPlanRepository.getSuccessionPlanByUserId(userId);
        return projections.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public List<SuccessionPlanResponseDto> getSuccessionPlanByPosition(int positionId){
        Position position = positionRepository.findById(positionId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        List<SuccessionPlanRepository.SuccessionPlanProjection> projections = successionPlanRepository.getSuccessionPlanByPosition(positionId);
        return projections.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public List<CriticalRoleCheckForSuccessionDto> checkPositionStatus(){
        List<RolesAssessment> roles = criticalRolesAssessmentRepo.findAll();
        List<SuccessionPlanRepository.CriticalRolesMapping> mapping = successionPlanRepository.getRolesMapping();
        return mapping.stream().map(
                role ->{
                    CriticalRoleCheckForSuccessionDto dto = new CriticalRoleCheckForSuccessionDto();
                    dto.setRoleId(role.getRoleId());
                    dto.setPlanId(role.getPlanId());
                    dto.setRoleName(role.getRoleName());
                    dto.setSuccessionStatus("mapped");
                    dto.setCurrentState(role.getCurrentState());
                    return dto;
                }).toList();
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
                                intervention.setSuccessionPlan(successionPlan);
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
                                need.setSuccessionPlan(successionPlan);
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
        if (interventionDtos == null) {
            return new ArrayList<>();
        }

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
        if (needDtos == null) {
            return new ArrayList<>();
        }
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
        if (successionPlan.getExternalSuccessors() != null && !successionPlan.getExternalSuccessors().isEmpty()) {
            List<ExternalSuccessorDto> externalSuccessorDtos = getExternalSuccessorDtos(successionPlan);
            dto.setExternalSuccessor(externalSuccessorDtos);
        }

        return dto;
    }

    private static List<ExternalSuccessorDto> getExternalSuccessorDtos(SuccessionPlan successionPlan) {
        List<ExternalSuccessor> externalSuccessors = successionPlan.getExternalSuccessors();

        return externalSuccessors.stream().map(externalSuccessor -> {
            ExternalSuccessorDto externalSuccessorDto = new ExternalSuccessorDto();
            externalSuccessorDto.setName(externalSuccessor.getName());

            externalSuccessorDto.setContactInfo(externalSuccessor.getContactInfo());
            externalSuccessorDto.setCurrentPosition(externalSuccessor.getCurrentPosition());
            externalSuccessorDto.setCurrentCompany(externalSuccessor.getCurrentCompany());
            externalSuccessorDto.setReasonForSelection(externalSuccessor.getReasonForSelection());
            externalSuccessorDto.setExpectedStartDate(externalSuccessor.getExpectedStartDate());
            return externalSuccessorDto;
        }).collect(Collectors.toList());
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
