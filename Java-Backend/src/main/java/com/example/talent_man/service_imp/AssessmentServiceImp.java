package com.example.talent_man.service_imp;

import com.example.talent_man.dto.assessment.*;
import com.example.talent_man.models.Assessment;
import com.example.talent_man.models.AssessmentQuestion;
import com.example.talent_man.models.Choice;
import com.example.talent_man.models.PotentialAttribute;
import com.example.talent_man.models.user.User;
import com.example.talent_man.repos.AssessmentRepo;
import com.example.talent_man.repos.PotentialAttributeRepo;
import com.example.talent_man.repos.UserManSelectedQuestionAnswerRepository;
import com.example.talent_man.repos.user.UserRepo;
import com.example.talent_man.services.AssessmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AssessmentServiceImp implements AssessmentService {
    @Autowired
    private AssessmentRepo repo;

    @Autowired
    UserRepo userRepo;

    @Autowired
    private UserManSelectedQuestionAnswerRepository userManSelectedQuestionAnswerRepo;
    @Override
    public Assessment addAss(Assessment ass) {
        return repo.save(ass);
    }

    @Override
    public Assessment getById(int id) {
        return repo.getReferenceById(id);
    }


//    @Override
//    public List<Assessment> getAttAssess(int attId) {
//        PotentialAttribute att = pRepo.getReferenceById(attId);
//        return new ArrayList<>(att.getAssessments());
//    }

    @Override
    public List<Assessment> findAssessmentsByPotentialAttribute(PotentialAttribute attribute) {
        return repo.findByPotentialAttributes(attribute);
    }

    @Override
    public boolean doesUserExist(int userId) {
        // Assuming you have a user repository to check for user existence
        return userRepo.existsById(userId); // Adjust based on your user repository implementation
    }
    @Override
    public List<Assessment> getAttAssess(int attId) {
        return null;
    }


    @Override
    public AssessmentDto getAssessmentWithAttributesAndQuestions(int assessmentId) {
        Assessment assessment = repo.findById(assessmentId)
                .orElse(null);

        if (assessment == null) {
            return null; // Handle this case in the controller
        }

        AssessmentDto assessmentDto = new AssessmentDto();
        assessmentDto.setAssessmentId(assessment.getAssessmentId());
        assessmentDto.setAssessmentName(assessment.getAssessmentName());
        assessmentDto.setAssessmentDescription(assessment.getAssessmentDescription());
        assessmentDto.setEndDate(assessment.getEndDate());

        List<PotentialAttributeDto> potentialAttributeDtos = assessment.getPotentialAttributes().stream()
                .map(attribute -> {
                    PotentialAttributeDto attributeDto = new PotentialAttributeDto();
                    attributeDto.setPotentialAttributeId(attribute.getPotentialAttributeId());
                    attributeDto.setAttributeName(attribute.getPotentialAttributeName());

                    List<QuestionDto> questionDtos = attribute.getQuestions().stream()
                            .map(question -> {
                                QuestionDto questionDto = new QuestionDto();
                                questionDto.setAssessmentQuestionId(question.getAssessmentQuestionId());
                                questionDto.setAssessmentQuestionDescription(question.getAssessmentQuestionDescription());

                                // Convert List<ChoiceDto> to Set<ChoiceDto>
                                Set<ChoiceDto> choiceDtos = question.getChoices().stream()
                                        .map(choice -> {
                                            ChoiceDto choiceDto = new ChoiceDto();
                                            choiceDto.setChoiceId(choice.getChoiceId());
                                            choiceDto.setChoiceName(choice.getChoiceName());
                                            choiceDto.setChoiceValue(Integer.parseInt(choice.getChoiceValue()));
                                            return choiceDto;
                                        })
                                        .collect(Collectors.toSet());

                                questionDto.setChoices(choiceDtos); // Set<ChoiceDto> expected

                                return questionDto;
                            })
                            .collect(Collectors.toList());

                    attributeDto.setQuestions(questionDtos);
                    return attributeDto;
                })
                .collect(Collectors.toList());

        assessmentDto.setPotentialAttributes(potentialAttributeDtos);

        return assessmentDto;
    }


    @Override
    public List<AssessmentDto> getActiveAssessments() {
        LocalDate now = LocalDate.from(LocalDateTime.now());
        List<Assessment> activeAssessments = repo.findActiveAssessments(now); // Repository method to get active assessments

        List<AssessmentDto> assessmentDtos = new ArrayList<>();

        for (Assessment assessment : activeAssessments) {
            AssessmentDto assessmentDto = new AssessmentDto();
            assessmentDto.setAssessmentId(assessment.getAssessmentId());
            assessmentDto.setAssessmentName(assessment.getAssessmentName());
            assessmentDto.setAssessmentDescription(assessment.getAssessmentDescription());
            assessmentDto.setEndDate(assessment.getEndDate());

            List<PotentialAttributeDto> potentialAttributeDtos = assessment.getPotentialAttributes().stream()
                    .map(attribute -> {
                        PotentialAttributeDto attributeDto = new PotentialAttributeDto();
                        attributeDto.setPotentialAttributeId(attribute.getPotentialAttributeId());
                        attributeDto.setAttributeName(attribute.getPotentialAttributeName());

                        List<QuestionDto> questionDtos = attribute.getQuestions().stream()
                                .map(question -> {
                                    QuestionDto questionDto = new QuestionDto();
                                    questionDto.setAssessmentQuestionId(question.getAssessmentQuestionId());
                                    questionDto.setAssessmentQuestionDescription(question.getAssessmentQuestionDescription());

                                    // Convert List<ChoiceDto> to Set<ChoiceDto>
                                    Set<ChoiceDto> choiceDtos = question.getChoices().stream()
                                            .map(choice -> {
                                                ChoiceDto choiceDto = new ChoiceDto();
                                                choiceDto.setChoiceId(choice.getChoiceId());
                                                choiceDto.setChoiceName(choice.getChoiceName());
                                                choiceDto.setChoiceValue(Integer.parseInt(choice.getChoiceValue()));
                                                return choiceDto;
                                            })
                                            .collect(Collectors.toSet());

                                    questionDto.setChoices(choiceDtos); // Set<ChoiceDto> expected
                                    return questionDto;
                                })
                                .collect(Collectors.toList());

                        attributeDto.setQuestions(questionDtos);
                        return attributeDto;
                    })
                    .collect(Collectors.toList());

            assessmentDto.setPotentialAttributes(potentialAttributeDtos);
            assessmentDtos.add(assessmentDto);
        }

        return assessmentDtos;
    }

    //active but user has not attempted






    @Override
    public List<AssessmentResponse> getActiveAssessmentsNotAttemptedByUser(int userId) {
        LocalDate now = LocalDate.now();
        List<Assessment> activeAssessments = repo.findActiveAssessments(now); // Repository method to get active assessments

        // Retrieve the IDs of assessments the user has already attempted
        List<Integer> attemptedAssessmentIds = userManSelectedQuestionAnswerRepo.findAssessmentIdsByUserId(userId);

        // Debugging: log attempted assessment IDs
        System.out.println("Attempted Assessment IDs for User ID " + userId + ": " + attemptedAssessmentIds);

        List<AssessmentResponse> assessmentResponses = new ArrayList<>();

        // Log Active Assessments
        System.out.println("Active Assessments IDs: " + activeAssessments.stream()
                .map(Assessment::getAssessmentId)
                .collect(Collectors.toList()));

        for (Assessment assessment : activeAssessments) {
            System.out.println("Processing Assessment ID: " + assessment.getAssessmentId());

            // Create the assessment DTO
            AssessmentDto assessmentDto = new AssessmentDto();
            assessmentDto.setAssessmentId(assessment.getAssessmentId());
            assessmentDto.setAssessmentName(assessment.getAssessmentName());
            assessmentDto.setAssessmentDescription(assessment.getAssessmentDescription());
            assessmentDto.setEndDate(assessment.getEndDate());

            // Map potential attributes
            List<PotentialAttributeDto> potentialAttributeDtos = assessment.getPotentialAttributes().stream()
                    .map(attribute -> {
                        PotentialAttributeDto attributeDto = new PotentialAttributeDto();
                        attributeDto.setPotentialAttributeId(attribute.getPotentialAttributeId());
                        attributeDto.setAttributeName(attribute.getPotentialAttributeName());

                        List<QuestionDto> questionDtos = attribute.getQuestions().stream()
                                .map(question -> {
                                    QuestionDto questionDto = new QuestionDto();
                                    questionDto.setAssessmentQuestionId(question.getAssessmentQuestionId());
                                    questionDto.setAssessmentQuestionDescription(question.getAssessmentQuestionDescription());

                                    // Convert List<ChoiceDto> to Set<ChoiceDto>
                                    Set<ChoiceDto> choiceDtos = question.getChoices().stream()
                                            .map(choice -> {
                                                ChoiceDto choiceDto = new ChoiceDto();
                                                choiceDto.setChoiceId(choice.getChoiceId());
                                                choiceDto.setChoiceName(choice.getChoiceName());
                                                choiceDto.setChoiceValue(Integer.parseInt(choice.getChoiceValue()));
                                                return choiceDto;
                                            })
                                            .collect(Collectors.toSet());

                                    questionDto.setChoices(choiceDtos);
                                    return questionDto;
                                })
                                .collect(Collectors.toList());

                        attributeDto.setQuestions(questionDtos);
                        return attributeDto;
                    })
                    .collect(Collectors.toList());

            assessmentDto.setPotentialAttributes(potentialAttributeDtos);

            // Check if any questions in the assessment have been attempted
            boolean isAttempted = assessment.getPotentialAttributes().stream()
                    .flatMap(attribute -> attribute.getQuestions().stream())
                    .anyMatch(question -> attemptedAssessmentIds.contains(question.getAssessmentQuestionId())); // Adjust this if your logic differs

            // Create AssessmentResponse and set attempted status
            AssessmentResponse response = new AssessmentResponse();
            response.setAssessment(assessmentDto);
            response.setAttempted(isAttempted); // Set attempted status based on questions

            assessmentResponses.add(response);

            // Log the status
            System.out.println("Assessment ID: " + assessment.getAssessmentId() + ", Attempted: " + isAttempted);
        }

        return assessmentResponses;
    }
    //  manager assessment for his employees
    @Override
    public List<ManagerAssessmentResponse> getAssessmentsNotAssessedByManager(int managerId) {
        LocalDate now = LocalDate.now();
        List<Assessment> activeAssessments = repo.findActiveAssessments(now);
        List<Integer> userIds = userRepo.findUserIdsByManagerId(managerId);
        List<Integer> attemptedQuestionIds = userManSelectedQuestionAnswerRepo.findAssessmentIdsByUserIds(userIds);

        System.out.println("Active Assessments: " + activeAssessments);
        System.out.println("User IDs: " + userIds);
        System.out.println("Attempted Question IDs: " + attemptedQuestionIds);

        List<ManagerAssessmentResponse> response = new ArrayList<>();

        for (Assessment assessment : activeAssessments) {
            ManagerAssessmentResponse.AssessmentDto assessmentDto = new ManagerAssessmentResponse.AssessmentDto();
            assessmentDto.setAssessmentId(assessment.getAssessmentId());
            assessmentDto.setAssessmentName(assessment.getAssessmentName());
            assessmentDto.setAssessmentDescription(assessment.getAssessmentDescription());
            assessmentDto.setEndDate(assessment.getEndDate());

            List<ManagerAssessmentResponse.AssessmentDto.PotentialAttributeDto> potentialAttributes = assessment.getPotentialAttributes().stream()
                    .map(attribute -> {
                        ManagerAssessmentResponse.AssessmentDto.PotentialAttributeDto attributeDto = new ManagerAssessmentResponse.AssessmentDto.PotentialAttributeDto();
                        attributeDto.setPotentialAttributeId(attribute.getPotentialAttributeId());
                        attributeDto.setAttributeName(attribute.getPotentialAttributeName());

                        List<ManagerAssessmentResponse.AssessmentDto.QuestionResponseDto> questions = attribute.getQuestions().stream()
                                .map(question -> {
                                    ManagerAssessmentResponse.AssessmentDto.QuestionResponseDto questionDto = new ManagerAssessmentResponse.AssessmentDto.QuestionResponseDto();
                                    questionDto.setAssessmentQuestionId(question.getAssessmentQuestionId());
                                    questionDto.setAssessmentQuestionDescription(question.getAssessmentQuestionDescription());

                                    List<UserManSelectedQuestionAnswerRepository.ChoiceProjection> choiceProjections = userManSelectedQuestionAnswerRepo.findSelectedChoiceByQuestionIdAndUserIds(question.getAssessmentQuestionId(), userIds);
                                    Set<ManagerAssessmentResponse.AssessmentDto.ChoiceDto> choiceDtos = choiceProjections.stream()
                                            .map(choice -> {
                                                ManagerAssessmentResponse.AssessmentDto.ChoiceDto choiceDto = new ManagerAssessmentResponse.AssessmentDto.ChoiceDto();
                                                choiceDto.setChoiceId(choice.getChoiceId());
                                                choiceDto.setChoiceValue(choice.getChoiceValue());
                                                choiceDto.setChoiceName(choice.getChoiceName());
                                                return choiceDto;
                                            })
                                            .collect(Collectors.toSet());

                                    questionDto.setChoices(choiceDtos);

                                    UserManSelectedQuestionAnswerRepository.ChoiceProjection selectedChoiceProjection = userManSelectedQuestionAnswerRepo.findSelectedChoiceByQuestionIdAndUserId(question.getAssessmentQuestionId(), userIds.get(0));
                                    if (selectedChoiceProjection != null) {
                                        ManagerAssessmentResponse.AssessmentDto.ChoiceDto selectedChoiceDto = new ManagerAssessmentResponse.AssessmentDto.ChoiceDto();
                                        selectedChoiceDto.setChoiceId(selectedChoiceProjection.getChoiceId());
                                        selectedChoiceDto.setChoiceValue(selectedChoiceProjection.getChoiceValue());
                                        selectedChoiceDto.setChoiceName(selectedChoiceProjection.getChoiceName());
                                        questionDto.setSelectedChoice(selectedChoiceDto);
                                    }

                                    return questionDto;
                                })
                                .collect(Collectors.toList());

                        attributeDto.setQuestions(questions);
                        return attributeDto;
                    })
                    .collect(Collectors.toList());

            assessmentDto.setPotentialAttributes(potentialAttributes);

            // Debugging statements to trace values
            boolean isAttempted = attemptedQuestionIds.contains(assessment.getAssessmentId());
            System.out.println("Assessment ID: " + assessment.getAssessmentId() + " isAttempted: " + isAttempted);

            List<Integer> assessmentsForUsers = userManSelectedQuestionAnswerRepo.findAssessmentIdsByUserIds(userIds);
            boolean isAssessed = assessmentsForUsers.contains(assessment.getAssessmentId());
            System.out.println("Assessment ID: " + assessment.getAssessmentId() + " isAssessed: " + isAssessed);

            if (isAttempted && !isAssessed) {
                for (Integer userId : userIds) {
                    ManagerAssessmentResponse userResponse = new ManagerAssessmentResponse();
                    userResponse.setAssessment(assessmentDto);

                    User user = userRepo.findById(userId).orElse(null);
                    if (user != null) {
                        userResponse.setUserId(user.getUserId());
                        userResponse.setUsername(user.getUsername());
                        userResponse.setUserFullName(user.getUserFullName());

                        List<ManagerAssessmentResponse.AssessmentDto.QuestionResponseDto> userQuestions = assessment.getPotentialAttributes().stream()
                                .flatMap(attribute -> attribute.getQuestions().stream())
                                .map(question -> {
                                    ManagerAssessmentResponse.AssessmentDto.QuestionResponseDto questionResponseDto = new ManagerAssessmentResponse.AssessmentDto.QuestionResponseDto();
                                    questionResponseDto.setAssessmentQuestionId(question.getAssessmentQuestionId());
                                    questionResponseDto.setAssessmentQuestionDescription(question.getAssessmentQuestionDescription());

                                    UserManSelectedQuestionAnswerRepository.ChoiceProjection selectedChoiceProjection = userManSelectedQuestionAnswerRepo.findSelectedChoiceByQuestionIdAndUserId(question.getAssessmentQuestionId(), user.getUserId());
                                    if (selectedChoiceProjection != null) {
                                        ManagerAssessmentResponse.AssessmentDto.ChoiceDto selectedChoiceDto = new ManagerAssessmentResponse.AssessmentDto.ChoiceDto();
                                        selectedChoiceDto.setChoiceId(selectedChoiceProjection.getChoiceId());
                                        selectedChoiceDto.setChoiceValue(selectedChoiceProjection.getChoiceValue());
                                        selectedChoiceDto.setChoiceName(selectedChoiceProjection.getChoiceName());
                                        questionResponseDto.setSelectedChoice(selectedChoiceDto);
                                    }

                                    return questionResponseDto;
                                })
                                .collect(Collectors.toList());

                        userResponse.setQuestions(userQuestions);
                    }

                    response.add(userResponse);
                }
            }
        }
        return response;
    }

//check if manager has assessed an employee
    @Override
public List<ManagerUserAssessmentStatusDto> getManagerUsersAssessmentStatus(int managerId) {
    // Get the list of users managed by the manager
    List<User> managerUsers = userRepo.findUsersByManagerId(managerId);
    List<ManagerUserAssessmentStatusDto> response = new ArrayList<>();

    // Loop through each user and check their self-assessment and manager assessment status
    for (User user : managerUsers) {
        ManagerUserAssessmentStatusDto dto = new ManagerUserAssessmentStatusDto();
        dto.setUserId(user.getUserId());
        dto.setUsername(user.getUsername());
        dto.setUserFullName(user.getUserFullName());
        dto.setPf(user.getPf());

        // Check if the user has self-assessed in any active assessment
        int selfAssessmentCount = userManSelectedQuestionAnswerRepo.isSelfAssessedInActiveAssessment(user.getUserId());
        dto.setSelfAssessed(selfAssessmentCount > 0);

        // Check if the manager has assessed the user in any active assessment
        int managerAssessmentCount = userManSelectedQuestionAnswerRepo.isManagerAssessedInActiveAssessment(user.getUserId(), managerId);
        dto.setManagerAssessed(managerAssessmentCount > 0);

        // Add the user's assessment status to the response list
        response.add(dto);
    }

    return response;
}
}

