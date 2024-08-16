package com.example.talent_man.controllers;


import com.example.talent_man.dto.DoneAssessmentDto;
import com.example.talent_man.dto.assessment.*;
import com.example.talent_man.models.Assessment;
import com.example.talent_man.models.AssessmentQuestion;
import com.example.talent_man.models.Choice;
import com.example.talent_man.models.PotentialAttribute;
import com.example.talent_man.models.user.Manager;
import com.example.talent_man.models.user.User;
import com.example.talent_man.repos.AssessmentQuestionsRepo;
import com.example.talent_man.service_imp.AssessmentQuestionServiceImp;
import com.example.talent_man.service_imp.UserQuestionAnswerImp;
import com.example.talent_man.services.AssessmentService;
import com.example.talent_man.services.PotentialAttributeService;
import com.example.talent_man.services.UserService;
import com.example.talent_man.utils.ApiResponse;
import org.hibernate.sql.ast.tree.update.Assignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

//@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/v1/api")
public class AssessmentController {
    @Autowired
    private AssessmentService service;

    @Autowired
    private UserService userService;
    @Autowired
    private UserQuestionAnswerImp qService;

    @Autowired
    private AssessmentQuestionServiceImp assQuestService;

    @Autowired
    private PotentialAttributeService pService;

    @PostMapping("/addQuestion")
    public ApiResponse<?> addQuestion(@RequestParam int assId, @RequestBody AssessmentQuestion assQuestion){
        try{
            if(assId == 0){
                return new ApiResponse<>(300, "Enter a valid assignment Id");
            }else if(assQuestion.getAssessmentQuestionDescription() == null || assQuestion.getAssessmentQuestionDescription().equals("")){
                return new ApiResponse<>(300, "Please enter question description");
            } else if (assQuestion.getChoices() == null || assQuestion.getChoices().isEmpty()) {
                return new ApiResponse<>(300, "Question must have Choices");
            } else{
                Assessment ass = service.getById(assId);

                if(ass.getAssessmentQuestions() == null || ass.getAssessmentQuestions().isEmpty()){
                    Set<AssessmentQuestion> questions = new HashSet<>();
                    questions.add(assQuestion);
                    ass.setAssessmentQuestions(questions);
                }else{
                    ass.getAssessmentQuestions().add(assQuestion);
                }

                Assessment one = service.addAss(ass);
                return new ApiResponse<>(200, one.toString() + "success");

            }
        }catch (Exception e){
            return new ApiResponse<>(200, e.getMessage());

        }
    }

    @PostMapping("/addQuestionToExisting")
    public ApiResponse<?> addQuestionToExisting(@RequestParam int assId, @RequestBody AssessmentQuestion assQuestion){
        try{
            if(assId == 0){
                return new ApiResponse<>(300, "Enter a valid assignment Id");
            } else if(assQuestion.getAssessmentQuestionDescription() == null || assQuestion.getAssessmentQuestionDescription().isEmpty()){
                return new ApiResponse<>(300, "Please enter question description");
            } else if (assQuestion.getChoices() == null || assQuestion.getChoices().isEmpty()) {
                return new ApiResponse<>(300, "Question must have Choices");
            } else {
                Assessment assessment = service.getById(assId);

                if(assessment == null) {
                    return new ApiResponse<>(404, "Assessment not found");
                }

                if(assessment.getAssessmentQuestions() == null) {
                    assessment.setAssessmentQuestions(new HashSet<>());
                }

                assessment.getAssessmentQuestions().add(assQuestion);

                Assessment updatedAss = service.addAss(assessment);
                return new ApiResponse<>(200, updatedAss.toString() + " success");

            }
        } catch (Exception e) {
            return new ApiResponse<>(500, e.getMessage());
        }
    }
    @PostMapping("/addQuestionsToAttributes")
    public ApiResponse<List<PotentialAttribute>> addQuestionsToAttributes(@RequestBody List<AttributeQuestionDto> attributeQuestionDtos) {
        try {
            List<PotentialAttribute> updatedAttributes = new ArrayList<>();

            for (AttributeQuestionDto dto : attributeQuestionDtos) {
                PotentialAttribute potentialAttribute = pService.getPotentialAttributeById(dto.getAttributeId());

                if (potentialAttribute == null) {
                    return new ApiResponse<>(300, "Potential attribute not found for ID: " + dto.getAttributeId());
                }

                // Initialize a set to hold existing questions for comparison
                Set<AssessmentQuestion> existingQuestions = potentialAttribute.getQuestions() != null ?
                        new HashSet<>(potentialAttribute.getQuestions()) :
                        new HashSet<>();

                // Iterate over the incoming questions
                for (AttributeQuestionDto.QuestionDto questionDto : dto.getQuestions()) {
                    // Check if the question already exists
                    boolean questionExists = existingQuestions.stream()
                            .anyMatch(q -> q.getAssessmentQuestionDescription().equals(questionDto.getAssessmentQuestionDescription()));

                    if (questionExists) {
                        // Log a message indicating the question is being skipped
                        System.out.println("Skipping existing question: " + questionDto.getAssessmentQuestionDescription());
                        continue; // Skip to the next question
                    }

                    // Create a new AssessmentQuestion
                    AssessmentQuestion question = new AssessmentQuestion();
                    question.setAssessmentQuestionDescription(questionDto.getAssessmentQuestionDescription());
                    question.setPotentialAttribute(potentialAttribute); // Set the potential attribute

                    // Create a set for choices
                    Set<Choice> choices = new HashSet<>();
                    for (AttributeQuestionDto.ChoiceDto choiceDto : questionDto.getChoices()) {
                        Choice choice = new Choice();
                        choice.setChoiceValue(choiceDto.getChoiceValue());
                        choice.setChoiceName(choiceDto.getChoiceName());
                        choice.setAssessmentQuestion(question); // Set the reference to the assessment question
                        choices.add(choice);
                    }
                    question.setChoices(choices);

                    // Add the new question to the existing questions
                    existingQuestions.add(question);
                }

                // Update the potential attribute with the new questions
                potentialAttribute.setQuestions(existingQuestions);

                // Persist the updated potential attribute
                PotentialAttribute updatedAttribute = pService.addPotentialAttribute(potentialAttribute);
                updatedAttributes.add(updatedAttribute);
            }

            ApiResponse<List<PotentialAttribute>> response = new ApiResponse<>(200, "Successfully added questions to attributes");
            response.setItem(updatedAttributes);
            return response;

        } catch (Exception e) {
            e.printStackTrace(); // Log the exception stack trace for better debugging
            return new ApiResponse<>(500, e.getMessage());
        }
    }

    @PostMapping("/addAQuestionList")
    public ApiResponse<Assessment> addQuestionList(@RequestParam Integer assId, @RequestBody List<AssessmentQuestion> quests){
        try{
            if (assId == null || assId == 0) {
                return new ApiResponse<>(300, "enter a valid id");
            }else if(quests == null || quests.isEmpty()){
                return new ApiResponse<>(300, "Enter valid questions");
            }else {
                Assessment ass = service.getById(assId);
                Set<AssessmentQuestion> q = new HashSet<>();

                for (AssessmentQuestion a : quests) {
                    if (a.getChoices() == null || a.getChoices().isEmpty()) {
                        return new ApiResponse<>(300, "Question must have Choices");
                    } else {
                        for (Choice choice: a.getChoices()){
                            if (choice.getChoiceValue() == null || choice.getChoiceValue().isEmpty()){
                                return new ApiResponse<>(300, "choice must have Choice value");
                            }
                            if(choice.getChoiceName() == null || choice.getChoiceName().isEmpty() || choice.getChoiceName().isBlank()){
                                return new ApiResponse<>(300, "choice must have name");

                            }
                            q.add(a);
                        }
                    }
                }
                if(ass.getAssessmentQuestions() == null || ass.getAssessmentQuestions().isEmpty()){
                    ass.setAssessmentQuestions(q);

                }else{
                    ass.getAssessmentQuestions().addAll(quests);
                }
                Assessment dbAss = service.addAss(ass);
                ApiResponse<Assessment> res = new ApiResponse<>(200, "Successfully added " + quests.size() + "questions");
                res.setItem(dbAss);

                return res;
            }

        }catch (Exception e){
            return new ApiResponse<>(500, e.getMessage());
        }
    }

    @GetMapping("/getAssessmentByAttr")
    public ApiResponse<List<Assessment>> getAttributeAssessments(@RequestParam int attId) {
        // Validate the provided ID
        if (attId == 0) {
            return new ApiResponse<>(300, "Enter a valid id");
        }

        // Retrieve the PotentialAttribute by ID
        PotentialAttribute att = pService.getPotentialAttributeById(attId);
        if (att == null) {
            return new ApiResponse<>(300, "Id not found");
        }

        // Retrieve assessments associated with this attribute
        List<Assessment> assess = service.findAssessmentsByPotentialAttribute(att);
        if (assess.isEmpty()) {
            return new ApiResponse<>(401, "No assessments found");
        }

        // Return the assessments in the response
        ApiResponse<List<Assessment>> response = new ApiResponse<>(200, "Successful");
        response.setItem(assess);
        return response;
    }

    @GetMapping("/getAssessment")
    public ApiResponse<List<Assessment>> getAttributeAssignments(@RequestParam int attId){
        PotentialAttribute att = pService.getPotentialAttributeById(attId);
        if(attId == 0){
            return new ApiResponse<>(300, "Enter a valid id");
        }else if(pService.getPotentialAttributeById(attId) == null ){
            return new ApiResponse<>(300, "Id not found");
        }else if(att.getAssessments().isEmpty()){
            return new ApiResponse<>(401, "No assessments found");

        }else{
            List<Assessment> assess = new ArrayList<>(att.getAssessments());
            ApiResponse<List<Assessment>> response = new ApiResponse<>(200,"Successful");
            response.setItem(assess);
            return response;
        }

    }

    @GetMapping("/getAssess")
    public ApiResponse<Assessment> getAssessment(@RequestParam int assId){
        if(assId == 0){
            return new ApiResponse<>(300, "Enter a valid id");
        }else if(service.getById(assId) == null ){
            return new ApiResponse<>(300, "Assessment not found");
        }else{
            Assessment ass = service.getById(assId);

            ApiResponse<Assessment> response = new ApiResponse<>(200,"Successful");
            response.setItem(ass);
            return response;
        }
    }

    @GetMapping("/getPageAssessment")
    public ApiResponse<AllAssDto> getPageAssessment(@RequestParam Integer assId, @RequestParam Integer manId){
        try{
            if(assId == null || assId == 0 || manId == null || manId == 0){
                return new ApiResponse<>(301, "Please enter a valid id");
            }else if(service.getById(assId) == null){
                return new ApiResponse<>(301, "No assessment found with the specified id");

            }else if(userService.getUserById(manId) == null){
                return new ApiResponse<>(301, "No user found with the specified id");
            }else{
                //initialising our response instance
                AllAssDto allAssDto = new AllAssDto();

                Manager manager = userService.getManagerById(manId);

                //getting the assessment from the db
                Assessment assessment = service.getById(assId);

                //assigning the response object values
                allAssDto.setId(assessment.getAssessmentId());
                allAssDto.setAssessmentName(assessment.getAssessmentName());
                allAssDto.setAssessmentDescription(assessment.getAssessmentDescription());
                allAssDto.setTotalQuestions(assessment.getAssessmentQuestions().size());

                // getting list of employees who have done the assessment
                List<Integer> empsDoneAss = qService.userDoneAssessIds(assId, manId);

                //getting managers employees size
                List<Integer> managerEmployees = userService.getManEmployees(manId);

                if(empsDoneAss.isEmpty()){
                    allAssDto.setNotDoneBy(managerEmployees.size());
                    allAssDto.setDoneBy(0);
                }
                allAssDto.setDoneBy(empsDoneAss.size());
                allAssDto.setNotDoneBy(managerEmployees.size() - empsDoneAss.size());

                if(!empsDoneAss.isEmpty()){
                    //calculating the employees average
                    List<Float> assessementAverages = new ArrayList<>();
                    for (Integer i:empsDoneAss) {
                        User user = userService.getUserById(i);

                        // getting questions attempted by the user.
                        List<Integer> userQuestionIds = qService.userDoneAssQuestionsIds(assId, user.getUserId());
                        List<AssQuestionDto> assQuestions = new ArrayList<>();

                        //storing the employees average
                        int empAverage = 0;
                        Float employeeAverage = 0.F;
                        List<Integer> empValues = new ArrayList<>();

                        //getting the choices of the user
                        for (Integer q : userQuestionIds) {
                            AssessmentQuestion question = assQuestService.getAssessmentQuestionById(q);

                            //getting the choice selected by the user
                            List<Integer> selectedChoiceIds = qService.getEmployeeSelectedChoice(user.getUserId(), q, assId);
                            int selectedChoiceId = selectedChoiceIds.get(selectedChoiceIds.size() - 1);

                            for (Choice c : question.getChoices()) {
                                if (c.getChoiceId() == selectedChoiceId) {
                                    empValues.add(Integer.parseInt(c.getChoiceValue()));
                                }
                            }
                        }

                        //calculating the employees average
                        for (Integer a:empValues) {
                            empAverage += a;
                        }

                        employeeAverage = (float) (empAverage/(userQuestionIds.size() * 6));
                        assessementAverages.add(employeeAverage);

                    }


                    float assessmentAverage = calculateAverage(assessementAverages);

                    allAssDto.setAverageScore(assessmentAverage);
                    assessment.setAverageScore(assessmentAverage);
                    service.addAss(assessment);
                }
                allAssDto.setAverageScore(0.F);

                ApiResponse<AllAssDto> response = new ApiResponse<>(200, "successful");
                response.setItem(allAssDto);
                return response;
            }
        }catch(Exception e){

        }
        return null;
    }

    @GetMapping("/getAllAssess")
    public ApiResponse<List<AllAssDto>> getAllAssignments(@RequestParam Integer manId){
        try{
            if(manId == null || manId == 0){
                return new ApiResponse<>(301, "Enter a valid id");
            } else if (userService.getUserById(manId) == null) {
                return new ApiResponse<>(301, "No user exists with the given id");
            }else{
               // List<Assessment> assess = new ArrayList<>();
                List<AllAssDto> responseAssess = new ArrayList<>();
                Manager manager = userService.getManagerById(manId);
                Set<PotentialAttribute> manAttributes = manager.getPotentialAttributeSet();

                if(manAttributes == null || manAttributes.isEmpty()){
                    return new ApiResponse<>(301, "No assessments");
                }else{
                    for(PotentialAttribute att: manAttributes){
                        Set<Assessment> manAssess = att.getAssessments();
                        if(manAssess != null && !manAssess.isEmpty()){
                            for (Assessment ass:manAssess) {
                                AllAssDto currentAss = new AllAssDto();

                               currentAss.setId(ass.getAssessmentId());
                                currentAss.setPotentialAttributeName(att.getPotentialAttributeName());
                                currentAss.setAssessmentName(ass.getAssessmentName());
                                currentAss.setAssessmentDescription(ass.getAssessmentDescription());
                                currentAss.setTotalQuestions(ass.getAssessmentQuestions().size());

                                // getting list of employees who have done the assessment
                                List<Integer> empsDoneAss = qService.userDoneAssessIds(currentAss.getId(), manId);

                                //getting managers employees size
                                List<Integer> managerEmployees = userService.getManEmployees(manId);

                                currentAss.setDoneBy(empsDoneAss.size());
                                int notDoneBy = 0;

                                //getting emp count not done the assessment
                                if(empsDoneAss.size() == 0){
                                    notDoneBy = managerEmployees.size();
                                }else {
                                    if(!managerEmployees.isEmpty()){
                                        for (int i = 0; i < managerEmployees.size(); i++) {
                                            for (int j = 0; j < empsDoneAss.size(); j++) {
                                                if(!Objects.equals(managerEmployees.get(i), empsDoneAss.get(j))){
                                                    notDoneBy += 1;
                                                }

                                            }
                                        }
                                    }
                                }


                                currentAss.setNotDoneBy(notDoneBy);
                                responseAssess.add(currentAss);
                            }
                        }
                    }
                    ApiResponse<List<AllAssDto>> response = new ApiResponse<>(200, "successful");
                    response.setItem(responseAssess);
                    return response;
                }

            }
        }catch (Exception e){
            return new ApiResponse<>(500, e.getMessage());
        }
    }

    @GetMapping("/getNotDoneEmpAssess")
    public ApiResponse<Set<AllAssDto>> getNotDoneAssignments(@RequestParam Integer empId, @RequestParam Integer manId) {
        try{
            if (manId == null || empId == null || manId == 0 || empId == 0) {
                return new ApiResponse<>(301, "Enter a valid id");
            } else if (userService.getUserById(manId) == null) {
                return new ApiResponse<>(301, "No user exists with the given id");
            } else {
                // List<Assessment> assess = new ArrayList<>();
                Set<AllAssDto> responseAssess = new HashSet<>();
                Manager manager = userService.getManagerById(manId);
                Set<PotentialAttribute> manAttributes = manager.getPotentialAttributeSet();

                if (manAttributes == null || manAttributes.isEmpty()) {
                    return new ApiResponse<>(301, "No assessments");
                } else {
                    //getting emp done assess
                    List<Integer> empDoneAsses = qService.getUserDoneAssessments(empId);
                    List<Integer> allAssessId = new ArrayList<>();
                    for (PotentialAttribute att : manAttributes) {
                        Set<Assessment> manAssess = att.getAssessments();
                        if (manAssess != null && !manAssess.isEmpty()) {
                            for (Assessment ass : manAssess) {
                                allAssessId.add(ass.getAssessmentId());
                            }
                        }
                    }


                    Collections.sort(allAssessId);
                    Collections.sort(empDoneAsses);

                    Map<Integer, Integer> occurrences = new HashMap<>();

                    // Combine the two lists
                    List<Integer> combinedList = new ArrayList<>();
                    combinedList.addAll(allAssessId);
                    combinedList.addAll(empDoneAsses);

                    // Count occurrences of each integer
                    for (Integer num : combinedList) {
                        occurrences.put(num, occurrences.getOrDefault(num, 0) + 1);
                    }

                    // Retrieve integers with only one occurrence
                    List<Integer> nonRepeatedIntegers = new ArrayList<>();
                    for (Map.Entry<Integer, Integer> entry : occurrences.entrySet()) {
                        if (entry.getValue() == 1) {
                            nonRepeatedIntegers.add(entry.getKey());
                        }
                    }

                    if (!nonRepeatedIntegers.isEmpty()){
                        for (Integer a: nonRepeatedIntegers) {
                            AllAssDto notDoneAss = new AllAssDto();
                            Assessment ass = service.getById(a);
                            notDoneAss.setId(ass.getAssessmentId());
                            notDoneAss.setAssessmentName(ass.getAssessmentName());
                            notDoneAss.setAssessmentDescription(ass.getAssessmentDescription());
                            notDoneAss.setTotalQuestions(ass.getAssessmentQuestions().size());
                            // getting list of employees who have done the assessment

                            List<Integer> empsDoneAss = qService.userDoneAssessIds(notDoneAss.getId(), manId);


                            //calculating the employees average
                            List<Float> assessmentAverages = new ArrayList<>();
                            for (Integer i:empsDoneAss) {
                                User u = userService.getUserById(i);

                                // getting questions attempted by the user.
                                List<Integer> userQuestionIds = qService.userDoneAssQuestionsIds(a, u.getUserId());
                                List<AssQuestionDto> assQuestions = new ArrayList<>();

                                //storing the employees average
                                int empAverage = 0;
                                float employeeAverage = 0.0F;
                                List<Integer> empValues = new ArrayList<>();

                                //getting the choices of the user
                                for (Integer q : userQuestionIds) {
                                    AssessmentQuestion question = assQuestService.getAssessmentQuestionById(q);

                                    //getting the choice selected by the user
                                    List<Integer> selectedChoiceIds = qService.getEmployeeSelectedChoice(u.getUserId(), q, a);
                                    int selectedChoiceId = selectedChoiceIds.get(selectedChoiceIds.size() - 1);

                                    for (Choice c : question.getChoices()) {
                                        if (c.getChoiceId() == selectedChoiceId) {
                                            empValues.add(Integer.parseInt(c.getChoiceValue()));
                                        }
                                    }
                                }

                                //calculating the employees average
                                for (Integer e:empValues) {
                                    empAverage += e;
                                }

                                employeeAverage = (float) (empAverage/empValues.size());
                                assessmentAverages.add(employeeAverage);

                            }


                            float assessmentAverage = calculateAverage(assessmentAverages);

                            notDoneAss.setAverageScore(assessmentAverage);



                            notDoneAss.setDoneBy(empsDoneAss.size());

                            //getting managers employees
                            List<Integer> managerEmployees = userService.getManEmployees(manId);

                            notDoneAss.setDoneBy(empsDoneAss.size());
                            int notDoneBy = 0;

                            //getting emp count not done the assessment
                            if(empsDoneAss.size() == 0){
                                notDoneBy = managerEmployees.size();
                            }else {
                                if(!managerEmployees.isEmpty()){
                                    for (int i = 0; i < managerEmployees.size(); i++) {
                                        for (int j = 0; j < empsDoneAss.size(); j++) {
                                            if(!Objects.equals(managerEmployees.get(i), empsDoneAss.get(j))){
                                                notDoneBy += 1;
                                            }

                                        }
                                    }
                                }
                            }
                            notDoneAss.setNotDoneBy(notDoneBy);
                            responseAssess.add(notDoneAss);

                        }
                        ApiResponse<Set<AllAssDto>> response = new ApiResponse<>(200, "successful");
                        response.setItem(responseAssess);
                        return response;
                    }else{
                        return new ApiResponse<>(200, "user has done all assessments");

                    }



                }
            }

        }catch(Exception e){
            return new ApiResponse<>(500, e.getMessage());

        }
    }
    @GetMapping("/getUserDoneAssessment")
    public ApiResponse<List<AllAssDto>> getUserDoneAssessments(@RequestParam Integer empId, @RequestParam Integer manId){
        try {
            if (empId == null || empId == 0 || manId == null || manId == 0) {
                return new ApiResponse<>(301, "Enter a valid id");
            } else if (userService.getUserById(empId) == null) {
                return new ApiResponse<>(301, "No user exists with the given id");
            } else {
                User user = userService.getUserById(empId);
                Manager manager = userService.getManagerById(manId);


                    //getting the users done assessments
                    List<Integer> userDoneAssess = qService.getUserDoneAssessments(empId);
                    List<AllAssDto> quests = new ArrayList<>();
                    if (userDoneAssess != null && !userDoneAssess.isEmpty()) {
                        for (Integer a : userDoneAssess) {
                            Assessment ass = service.getById(a);
                            AllAssDto doneAss = new AllAssDto();
                            doneAss.setId(ass.getAssessmentId());
                            doneAss.setAssessmentName(ass.getAssessmentName());
                            doneAss.setAssessmentDescription(ass.getAssessmentDescription());
                            doneAss.setTotalQuestions(ass.getAssessmentQuestions().size());

                            //getting employees who have done the assessment
                            List<Integer> empsDoneAss = qService.userDoneAssessIds(a, manId);

                            //calculating the employees average
                            List<Float> assessmentAverages = new ArrayList<>();
                            for (Integer i:empsDoneAss) {
                                User u = userService.getUserById(i);

                                // getting questions attempted by the user.
                                List<Integer> userQuestionIds = qService.userDoneAssQuestionsIds(a, u.getUserId());
                                List<AssQuestionDto> assQuestions = new ArrayList<>();

                                //storing the employees average
                                int empAverage = 0;
                                float employeeAverage = 0.0F;
                                List<Integer> empValues = new ArrayList<>();

                                //getting the choices of the user
                                for (Integer q : userQuestionIds) {
                                    AssessmentQuestion question = assQuestService.getAssessmentQuestionById(q);

                                    //getting the choice selected by the user
                                    List<Integer> selectedChoiceIds = qService.getEmployeeSelectedChoice(u.getUserId(), q, a);
                                    int selectedChoiceId = selectedChoiceIds.get(selectedChoiceIds.size() - 1);

                                    for (Choice c : question.getChoices()) {
                                        if (c.getChoiceId() == selectedChoiceId) {
                                            empValues.add(Integer.parseInt(c.getChoiceValue()));
                                        }
                                    }
                                }

                                //calculating the employees average
                                for (Integer e:empValues) {
                                    empAverage += e;
                                }

                                employeeAverage = (float) (empAverage/empValues.size());
                                assessmentAverages.add(employeeAverage);

                            }


                            float assessmentAverage = calculateAverage(assessmentAverages);

                            doneAss.setDoneBy(empsDoneAss.size());
                            doneAss.setAverageScore(assessmentAverage);

                            //getting number of employees of a manager
                            List<Integer> managerEmployees = userService.getManEmployees(manId);

                            int notDoneBy = 0;

                            //getting emp count not done the assessment
                            if(empsDoneAss.size() == 0){
                                notDoneBy = managerEmployees.size();
                            }else {
                                if(!managerEmployees.isEmpty()){
                                    for (int i = 0; i < managerEmployees.size(); i++) {
                                        for (int j = 0; j < empsDoneAss.size(); j++) {
                                            if(!Objects.equals(managerEmployees.get(i), empsDoneAss.get(j))){
                                                notDoneBy += 1;
                                            }

                                        }
                                    }
                                }
                            }
                            doneAss.setNotDoneBy(notDoneBy);
                            quests.add(doneAss);
                        }
                        ApiResponse<List<AllAssDto>> response = new ApiResponse<>(200, "successful");
                        response.setItem(quests);
                        return response;
                    }else{
                        return new ApiResponse<>(200, "user has not done assessments");

                    }


                }
            }catch (Exception e){
                return new ApiResponse<>(500, e.getMessage());

            }
    }

    @GetMapping("/getUserAssessedAssessments")
    public ApiResponse<List<AllAssDto>> getUserAssessedAssessments(@RequestParam Integer empId, @RequestParam Integer manId){
        try{
            if (empId == null || empId == 0 || manId == null || manId == 0) {
                return new ApiResponse<>(301, "Enter a valid id");
            } else if (userService.getUserById(empId) == null) {
                return new ApiResponse<>(301, "No user exists with the given id");
            } else {
                User user = userService.getUserById(empId);
                Manager manager = userService.getManagerById(manId);


                //getting the users done assessed assessments
                List<Integer> userDoneAssess = qService.getUserAssessedAssessments(manId, empId);
                if (userDoneAssess == null || userDoneAssess.isEmpty()){
                    return new ApiResponse<>(200, "No assessed assessments");
                }else{
                    List<AllAssDto> quests = new ArrayList<>();

                    for (Integer a: userDoneAssess) {
                        Assessment ass = service.getById(a);
                        AllAssDto assessedAss = new AllAssDto();
                        assessedAss.setId(ass.getAssessmentId());
                        assessedAss.setAssessmentName(ass.getAssessmentName());
                        assessedAss.setAssessmentDescription(ass.getAssessmentDescription());
                        assessedAss.setTotalQuestions(ass.getAssessmentQuestions().size());

                        //getting employees who have done the assessment
                        List<Integer> empsDoneAss = qService.userDoneAssessIds(a, manId);

                        //calculating the employees average
                        List<Float> assessmentAverages = new ArrayList<>();
                        for (Integer i:empsDoneAss) {
                            User u = userService.getUserById(i);

                            // getting questions attempted by the user.
                            List<Integer> userQuestionIds = qService.userDoneAssQuestionsIds(a, u.getUserId());
                            List<AssQuestionDto> assQuestions = new ArrayList<>();

                            //storing the employees average
                            int empAverage = 0;
                            float employeeAverage = 0.0F;
                            List<Integer> empValues = new ArrayList<>();

                            //getting the choices of the user
                            for (Integer q : userQuestionIds) {
                                AssessmentQuestion question = assQuestService.getAssessmentQuestionById(q);

                                //getting the choice selected by the user
                                List<Integer> selectedChoiceIds = qService.getEmployeeSelectedChoice(u.getUserId(), q, a);
                                int selectedChoiceId = selectedChoiceIds.get(selectedChoiceIds.size() - 1);

                                for (Choice c : question.getChoices()) {
                                    if (c.getChoiceId() == selectedChoiceId) {
                                        empValues.add(Integer.parseInt(c.getChoiceValue()));
                                    }
                                }
                            }

                            //calculating the employees average
                            for (Integer e:empValues) {
                                empAverage += e;
                            }

                            employeeAverage = (float) (empAverage/empValues.size());
                            assessmentAverages.add(employeeAverage);

                        }


                        float assessmentAverage = calculateAverage(assessmentAverages);

                        assessedAss.setAverageScore(assessmentAverage);
                        assessedAss.setDoneBy(empsDoneAss.size());

                        //getting number of employees of a manager
                        List<Integer> managerEmployees = userService.getManEmployees(manId);
                        int notDoneBy = 0;

                        //getting emp count not done the assessment
                        if(empsDoneAss.size() == 0){
                            notDoneBy = managerEmployees.size();
                        }else {
                            if(!managerEmployees.isEmpty()){
                                for (int i = 0; i < managerEmployees.size(); i++) {
                                    for (int j = 0; j < empsDoneAss.size(); j++) {
                                        if(!Objects.equals(managerEmployees.get(i), empsDoneAss.get(j))){
                                            notDoneBy += 1;
                                        }

                                    }
                                }
                            }
                        }

                        assessedAss.setNotDoneBy(notDoneBy);
                        quests.add(assessedAss);

                    }
                    ApiResponse<List<AllAssDto>> response = new ApiResponse<>(200, "successful");
                    response.setItem(quests);
                    return response;
                }
            }
        }catch(Exception e){
            return  new ApiResponse<>(500, e.getMessage());
        }
    }

    @GetMapping("/getManagerUnAssessedAssessments")
    public ApiResponse<List<AllAssDto>> getManagerUnAssessedAssessments(@RequestParam Integer manId){
        try {
            if (manId == null || manId == 0) {
                return new ApiResponse<>(301, "Enter a valid id");
            } else if (userService.getUserById(manId) == null) {
                return new ApiResponse<>(301, "No user exists with the given id");
            } else {

                //getting all un-assessed assessments
                List<Integer> unAssessedAssess = qService.getManagerUnAssessedAssessments(manId);
                if(unAssessedAssess == null || unAssessedAssess.isEmpty()){
                    return new ApiResponse<>(200, "No assessed assessments");
                }else{
                    List<AllAssDto> quests = new ArrayList<>();
                    for (Integer a : unAssessedAssess) {
                        Assessment ass = service.getById(a);
                        AllAssDto doneAss = new AllAssDto();
                        doneAss.setId(ass.getAssessmentId());
                        doneAss.setAssessmentName(ass.getAssessmentName());
                        doneAss.setAssessmentDescription(ass.getAssessmentDescription());
                        doneAss.setTotalQuestions(ass.getAssessmentQuestions().size());

                        //getting employees who have done the assessment
                        List<Integer> empsDoneAss = qService.userDoneAssessIds(a, manId);

                        doneAss.setDoneBy(empsDoneAss.size());

                        //getting number of employees of a manager
                        List<Integer> managerEmployees = userService.getManEmployees(manId);

                        //getting emp count not done the assessment
                        int notDoneBy = managerEmployees.size() - empsDoneAss.size();


                        doneAss.setNotDoneBy(notDoneBy);
                        quests.add(doneAss);
                    }
                    ApiResponse<List<AllAssDto>> response = new ApiResponse<>(200, "successful");
                    response.setItem(quests);
                    return response;
                }

            }
        }catch (Exception e){
            return new ApiResponse<>(500, e.getMessage());
        }
    }

    @GetMapping("/getManagerAssessedAssessments")
    public ApiResponse<List<AllAssDto>> getManagerAssessedAssessments(@RequestParam Integer manId){
        try {
            if (manId == null || manId == 0) {
                return new ApiResponse<>(301, "Enter a valid id");
            } else if (userService.getUserById(manId) == null) {
                return new ApiResponse<>(301, "No user exists with the given id");
            } else {

                //getting all un-assessed assessments
                List<Integer> unAssessedAssess = qService.getManagerAssessedAssessments(manId);
                if(unAssessedAssess == null || unAssessedAssess.isEmpty()){
                    return new ApiResponse<>(200, "No assessed assessments");
                }else{
                    List<AllAssDto> quests = new ArrayList<>();
                    for (Integer a : unAssessedAssess) {
                        Assessment ass = service.getById(a);
                        AllAssDto doneAss = new AllAssDto();
                        doneAss.setId(ass.getAssessmentId());
                        doneAss.setAssessmentName(ass.getAssessmentName());
                        doneAss.setAssessmentDescription(ass.getAssessmentDescription());
                        doneAss.setTotalQuestions(ass.getAssessmentQuestions().size());

                        //getting employees who have done the assessment
                        List<Integer> empsDoneAss = qService.userDoneAssessIds(a, manId);

                        doneAss.setDoneBy(empsDoneAss.size());

                        //getting number of employees of a manager
                        List<Integer> managerEmployees = userService.getManEmployees(manId);

                        //getting emp count not done the assessment
                        int notDoneBy = managerEmployees.size() - empsDoneAss.size();


                        doneAss.setNotDoneBy(notDoneBy);
                        quests.add(doneAss);
                    }
                    ApiResponse<List<AllAssDto>> response = new ApiResponse<>(200, "successful");
                    response.setItem(quests);
                    return response;
                }

            }
        }catch (Exception e){
            return new ApiResponse<>(500, e.getMessage());
        }
    }

    private float calculateAverage(List<Float> assessmentAverages) {
        float sum = 0.0f;

        if (assessmentAverages.isEmpty()) {
            return sum;
        }

        for (float value : assessmentAverages) {
            sum += value;
        }

        return sum / assessmentAverages.size();
    }
    private float getEmployeeAverageScore(List<AssQuestionDto> quests) {
        int count = 0;
        int maximumScore = 6 * quests.size();

        for (AssQuestionDto q : quests) {
            count = count + q.getEmployeeChoice().getChoiceValue();
        }

        float average = ((float) count) / maximumScore;
        return average;
    }
}


