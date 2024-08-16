package com.example.talent_man.service_imp;

import com.example.talent_man.dto.answers.ManagerAnswerDTO;
import com.example.talent_man.dto.answers.UserAnswerDTO;
import com.example.talent_man.models.AssessmentQuestion;
import com.example.talent_man.models.Choice;
import com.example.talent_man.models.PotentialAttribute;
import com.example.talent_man.models.answers.AverageScore;
import com.example.talent_man.models.answers.UserManSelectedQuestionAnswer;
import com.example.talent_man.models.user.Employee;
import com.example.talent_man.models.user.Manager;
import com.example.talent_man.models.user.User;
import com.example.talent_man.repos.*;
import com.example.talent_man.repos.user.UserRepo;
import com.example.talent_man.services.AssessmentAnswerService;
import com.example.talent_man.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AssessmentAnswerServiceImp implements AssessmentAnswerService {

    @Autowired
    private UserManSelectedQuestionAnswerRepository userManSelectedQuestionAnswerRepository;

    @Autowired
    private UserRepo userRepository;

    @Autowired
    PotentialAttributeRepo potentialAttributeRepo;
    @Autowired
    private AssessmentQuestionsRepo assessmentQuestionRepository;

    @Autowired
    private ChoiceRepo choiceRepository;

    @Autowired
    AverageScoreRepo averageScoreRepo;

    @Autowired
    AssessmentRepo assessmentRepo;

    @Override
    public ApiResponse<List<UserManSelectedQuestionAnswer>> submitUserAnswers(UserAnswerDTO userAnswerDTO) {
        List<UserManSelectedQuestionAnswer> savedAnswers = new ArrayList<>();
        Map<Integer, List<Integer>> scoresByAttribute = new HashMap<>(); // attributeId -> scores

        // Validate that answers are provided
        if (userAnswerDTO.getAnswers().isEmpty()) {
            return new ApiResponse<>(400, "No answers provided for the assessment.");
        }

        // Retrieve potential attributes linked to the assessment
        List<PotentialAttribute> potentialAttributes = potentialAttributeRepo.findByAssessments_AssessmentId(userAnswerDTO.getAssessmentId());

        // Validate that potential attributes exist for the assessment
        if (potentialAttributes.isEmpty()) {
            return new ApiResponse<>(400, "No potential attributes found for the assessment.");
        }

        // Retrieve assessment questions based on the potential attributes
        List<AssessmentQuestion> assessmentQuestions = assessmentQuestionRepository.findByPotentialAttributeIn(potentialAttributes);

        // Check if the number of answers matches the number of questions
        if (assessmentQuestions.size() != userAnswerDTO.getAnswers().size()) {
            return new ApiResponse<>(400, "Incomplete assessment. Please answer all questions.");
        }

        for (UserAnswerDTO.UserAnswer answerDTO : userAnswerDTO.getAnswers()) {
            // Retrieve the user based on the ID from answerDTO
            User user = userRepository.findById(answerDTO.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            // Check if the user has already submitted this assessment
//            if (userManSelectedQuestionAnswerRepository.existsByUserAndAssessmentId(user, userAnswerDTO.getAssessmentId())) {
//                return new ApiResponse<>(400, "User has already submitted answers for this assessment.");
//            }

            // Validate that each individual answer has a choice selected
            if (answerDTO.getChoiceId() <= 0) {
                return new ApiResponse<>(400, "All questions must be answered.");
            }

            AssessmentQuestion assessmentQuestion = assessmentQuestionRepository.findById(answerDTO.getAssessmentQuestionId())
                    .orElseThrow(() -> new IllegalArgumentException("Question not found"));
            Choice selectedChoice = choiceRepository.findById(answerDTO.getChoiceId())
                    .orElseThrow(() -> new IllegalArgumentException("Choice not found"));

            UserManSelectedQuestionAnswer answer = new UserManSelectedQuestionAnswer();
            answer.setUser(user);
            answer.setAssessmentQuestion(assessmentQuestion);
            answer.setUserChoice(selectedChoice);
            answer.setAssessmentId(userAnswerDTO.getAssessmentId());
            answer.setSelfAssessed(answerDTO.isSelfAssessed());
            answer.setUserScore(Integer.parseInt(selectedChoice.getChoiceValue())); // Assuming choice value holds the score

            savedAnswers.add(userManSelectedQuestionAnswerRepository.save(answer));

            // Collect scores by attribute
            int attributeId = assessmentQuestion.getPotentialAttribute().getPotentialAttributeId();
            scoresByAttribute.putIfAbsent(attributeId, new ArrayList<>());
            scoresByAttribute.get(attributeId).add(Integer.parseInt(selectedChoice.getChoiceValue()));
        }

        // Calculate and save average scores per attribute for the user
        for (Map.Entry<Integer, List<Integer>> entry : scoresByAttribute.entrySet()) {
            int attributeId = entry.getKey();
            List<Integer> scores = entry.getValue();
            double averageScore = scores.stream().mapToInt(Integer::intValue).average().orElse(0);

            AverageScore averageScoreEntity = new AverageScore();
            averageScoreEntity.setUser(savedAnswers.get(0).getUser()); // Use the user from the first saved answer
            averageScoreEntity.setAssessment(assessmentRepo.findById(userAnswerDTO.getAssessmentId()).orElse(null)); // Set assessment
            averageScoreEntity.setPotentialAttribute(potentialAttributeRepo.findById(attributeId).orElse(null)); // Set potential attribute
            averageScoreEntity.setAverageScore(averageScore);

            averageScoreRepo.save(averageScoreEntity);
        }

        // Wrap the saved answers in an ApiResponse and return it
        ApiResponse<List<UserManSelectedQuestionAnswer>> response = new ApiResponse<>(200, "User answers submitted successfully");
        response.setItem(savedAnswers);
        return response;
    }


    @Override
    public ApiResponse<List<UserManSelectedQuestionAnswer>> submitManagerAnswers(ManagerAnswerDTO managerAnswerDTO) {
        List<UserManSelectedQuestionAnswer> savedAnswers = new ArrayList<>();
        Map<Integer, Employee> assessedEmployees = new HashMap<>();

        // Retrieve the manager who is submitting the answers
        Manager manager = (Manager) userRepository.findById(managerAnswerDTO.getManagerId()).orElseThrow(() -> new IllegalArgumentException("Manager not found"));

        // Validate that answers are provided
        if (managerAnswerDTO.getAnswers().isEmpty()) {
            return new ApiResponse<>(400, "No answers provided for the assessment.");
        }

        // Create a set to track the assessment question IDs
        Set<Integer> questionIds = new HashSet<>();

        for (ManagerAnswerDTO.ManagerAnswer answerDTO : managerAnswerDTO.getAnswers()) {
            // Retrieve the user being assessed
            Employee employee = (Employee) userRepository.findById(answerDTO.getUserId()).orElseThrow(() -> new IllegalArgumentException("Employee not found"));

            // Check if the employee belongs to the manager
            if (!employee.getManager().equals(manager)) {
                return new ApiResponse<>(400, "Employee " + employee.getUserFullName() + " does not belong to this manager.");
            }

            // Check if the employee has completed their self-assessment
            if (!userManSelectedQuestionAnswerRepository.existsByUserAndAssessmentId(employee, managerAnswerDTO.getAssessmentId())) {
                return new ApiResponse<>(400, "User " + employee.getUserFullName() + " must complete their self-assessment before you can assess them.");
            }

            // Retrieve the assessment question
            AssessmentQuestion assessmentQuestion = assessmentQuestionRepository.findById(answerDTO.getAssessmentQuestionId())
                    .orElseThrow(() -> new IllegalArgumentException("Question not found"));

            // Validate the selected choice
            Choice selectedChoice = choiceRepository.findById(answerDTO.getChoiceId())
                    .orElseThrow(() -> new IllegalArgumentException("Choice not found"));

            // Ensure the selected choice belongs to the question
            if (!assessmentQuestion.getChoices().contains(selectedChoice)) {
                return new ApiResponse<>(400, "Selected choice does not belong to the question.");
            }

            // Create and save the answer
            UserManSelectedQuestionAnswer answer = new UserManSelectedQuestionAnswer();
            answer.setUser(employee);
            answer.setAssessmentQuestion(assessmentQuestion);
            answer.setManagerChoice(selectedChoice); // Set the manager's choice
            answer.setAssessmentId(managerAnswerDTO.getAssessmentId());
            answer.setManagerAssessed(answerDTO.isManagerAssessed());
            answer.setManagerScore(Integer.parseInt(selectedChoice.getChoiceValue())); // Assuming choice value holds the score

            savedAnswers.add(userManSelectedQuestionAnswerRepository.save(answer));

            // Store the employee in the map for later average score calculation
            assessedEmployees.put(employee.getUserId(), employee);

            // Add the question ID to the set
            questionIds.add(answerDTO.getAssessmentQuestionId());
        }

        // Retrieve potential attributes linked to the assessment
        List<PotentialAttribute> potentialAttributes = potentialAttributeRepo.findByAssessments_AssessmentId(managerAnswerDTO.getAssessmentId());

        // Validate that potential attributes exist for the assessment
        if (potentialAttributes.isEmpty()) {
            return new ApiResponse<>(400, "No potential attributes found for the assessment.");
        }
        // Check if all questions have been assessed
        // Retrieve assessment questions based on the potential attributes
        List<AssessmentQuestion> assessmentQuestions = assessmentQuestionRepository.findByPotentialAttributeIn(potentialAttributes);

        // Check if the number of answers matches the number of questions
        if (assessmentQuestions.size() != managerAnswerDTO.getAnswers().size()) {
            return new ApiResponse<>(400, "Incomplete assessment. Please answer all questions.");
        }


        // Calculate average scores per attribute for manager's assessment
        for (ManagerAnswerDTO.ManagerAnswer answerDTO : managerAnswerDTO.getAnswers()) {
            AssessmentQuestion assessmentQuestion = assessmentQuestionRepository.findById(answerDTO.getAssessmentQuestionId()).orElseThrow();
            PotentialAttribute potentialAttribute = assessmentQuestion.getPotentialAttribute();

            // Calculate average score for this potential attribute
            double averageScore = savedAnswers.stream()
                    .filter(a -> a.getAssessmentQuestion().getPotentialAttribute().equals(potentialAttribute))
                    .mapToDouble(UserManSelectedQuestionAnswer::getManagerScore)
                    .average()
                    .orElse(0.0);

            // Use the last employee in the map for the average score entity
            Employee employee = assessedEmployees.get(answerDTO.getUserId());

            AverageScore averageScoreEntity = new AverageScore();
            averageScoreEntity.setUser(employee);
            averageScoreEntity.setAssessment(assessmentRepo.findById(managerAnswerDTO.getAssessmentId()).orElseThrow());
            averageScoreEntity.setPotentialAttribute(potentialAttribute);
            averageScoreEntity.setAverageScore(averageScore);

            averageScoreRepo.save(averageScoreEntity);
        }

        // Wrap the saved answers in an ApiResponse and return it
        ApiResponse<List<UserManSelectedQuestionAnswer>> response = new ApiResponse<>(200, "Manager answers submitted successfully.");
        response.setItem(savedAnswers);
        return response;
    }

}
