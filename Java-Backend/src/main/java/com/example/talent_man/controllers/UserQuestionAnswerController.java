package com.example.talent_man.controllers;

import com.example.talent_man.dto.DoneAssessmentDto;
import com.example.talent_man.dto.assessment.*;
import com.example.talent_man.dto.user.ManagerDto;
import com.example.talent_man.models.*;
import com.example.talent_man.models.composite_keys.UserAnswerKey;
import com.example.talent_man.models.user.Employee;
import com.example.talent_man.models.user.Manager;
import com.example.talent_man.models.user.User;
import com.example.talent_man.service_imp.AssessmentQuestionServiceImp;
import com.example.talent_man.service_imp.AssessmentServiceImp;
import com.example.talent_man.service_imp.ChoiceServiceImp;
import com.example.talent_man.service_imp.UserQuestionAnswerImp;
import com.example.talent_man.services.AssessmentQuestionService;
import com.example.talent_man.services.UserQuestionAnswerService;
import com.example.talent_man.services.UserService;
import com.example.talent_man.utils.ApiResponse;
import com.example.talent_man.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
@RequestMapping("/v1/api")
public class UserQuestionAnswerController {
    @Autowired
    private UserService uService;

    @Autowired
    private UserQuestionAnswerImp qService;

    @Autowired
    private ChoiceServiceImp cService;

    @Autowired
    private AssessmentServiceImp assService;

    @Autowired
    private AssessmentQuestionServiceImp assQuestService;


    @PostMapping("/addAnswers")
    public ApiResponse<List<UserQuestionAnswer>> addAnswers(@RequestParam Integer assId, @RequestParam Integer manId, @RequestBody List<Answer> answers) {
        try {
            if (assId == null || assId == 0) {
                return new ApiResponse<>(301, "Enter valid assessment id");
            } else if (answers == null || answers.isEmpty()) {
                return new ApiResponse<>(301, "Please provide all the answers");
            } else {
                List<UserQuestionAnswer> uAnswers = new ArrayList<>();
                Assessment ass = assService.getById(assId);
                int employeeId = answers.get(0).getUserId();
                User user = uService.getUserById(employeeId);


                for (Answer answer : answers) {
                    int questionId = answer.getQuestionId();
                    int choiceId = answer.getChoiceId();
                    int userId = answer.getUserId();

                    UserQuestionAnswer userQuestionAnswer = new UserQuestionAnswer();
                    userQuestionAnswer.setQuestionId(questionId);
                    userQuestionAnswer.setUserChoiceId(choiceId);
                    userQuestionAnswer.setEmployeeId(userId);
                    userQuestionAnswer.setAssessmentId(assId);
                    userQuestionAnswer.setManagerId(manId);


                    UserQuestionAnswer newAns = qService.addUserAnswer(userQuestionAnswer);
                    uAnswers.add(newAns);

                }

                if (user.getCompleteAssessments() == null || user.getCompleteAssessments().isEmpty()) {
                    Set<Assessment> assess = new HashSet<>();
                    assess.add(ass);
                    user.setCompleteAssessments(assess);
                } else {
                    user.getCompleteAssessments().add(ass);
                }

                ApiResponse<List<UserQuestionAnswer>> response = new ApiResponse<>(200, "successful");
                response.setItem(uAnswers);
                return response;
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ApiResponse<>(500, e.getMessage());
        }
    }

    @GetMapping("/userAssessment")
    public ApiResponse<List<UserQuestionAnswer>> getUserAnswers(@RequestParam Integer assId, @RequestParam Integer userId) {
        try {
            if (assId == null || assId == 0 || userId == null || userId == 0) {
                return new ApiResponse<>(301, "Please enter a valid id");
            } else {
                if (qService.getUserAnswers(assId, userId) == null || qService.getUserAnswers(assId, userId).isEmpty()) {
                    return new ApiResponse<>(301, "No assessments attempted");

                } else {
                    List<UserQuestionAnswer> answers = qService.getUserAnswers(assId, userId);
                    ApiResponse<List<UserQuestionAnswer>> response = new ApiResponse<>(200, "successful");
                    response.setItem(answers);
                    return response;
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ApiResponse<>(500, e.getMessage());
        }
    }

    @GetMapping("/getDoneAssessments")
    public ApiResponse<List<UserQuestionAnswer>> getAllAssessments() {
        try {
            List<UserQuestionAnswer> allAssessments = qService.getAll();
            ApiResponse<List<UserQuestionAnswer>> response = new ApiResponse<>(200, "successful");
            response.setItem(allAssessments);
            return response;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ApiResponse<>(500, e.getMessage());
        }
    }


    @GetMapping("/employeeDoneAssessments")
    public ApiResponse<List<Assessment>> getEmployeeDoneAssessments(@RequestParam Integer manId) {
        try {
            if (manId == null || manId == 0) {
                return new ApiResponse<>(301, "Enter a valid id");
            } else if (qService.managerDoneAssessIds(manId) == null) {
                return new ApiResponse<>(403, "No assessments done");
            } else {
                List<Integer> assess = qService.managerDoneAssessIds(manId);
                List<Assessment> assessments = new ArrayList<>();
                for (Integer ass : assess) {
                    int a = ass;
                    Assessment assessment = assService.getById(a);
                    assessments.add(assessment);
                }

                ApiResponse<List<Assessment>> response = new ApiResponse<>(200, "successful");
                response.setItem(assessments);
                return response;
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ApiResponse<>(500, e.getMessage());
        }
    }


    @GetMapping("/doneAssessmentDetails")
    public ApiResponse<DoneAssessmentDto> getUsersDetails(@RequestParam Integer assId, @RequestParam Integer managerId) {
        try {
            if (assId == null || assId == 0 || managerId == null || managerId == 0) {
                return new ApiResponse<>(301, "Please enter a valid id");
            } else if (qService.userDoneAssessIds(assId, managerId) == null) {
                return new ApiResponse<>(403, "No user has done the current assessment");
            } else {
                Manager man = uService.getManagerById(managerId);
                DoneAssessmentDto doneAss = new DoneAssessmentDto();
                Assessment ass = assService.getById(assId);

                //getting user ids whom have done the specified id
                List<Integer> userIds = qService.userDoneAssessIds(assId, managerId);

                //creating a list to store the users
                List<UserAssessmentDto> users = new ArrayList<>();

                List<User> managerEmployees = uService.getManagerEmployees(man);

                //getting number of employees of a manager
                List<Integer> manEmployees = uService.getManEmployees(managerId);

                List<UserAssessmentDto> employeesNotDone = new ArrayList<>();

                //getting users who have done the assessment
                for (Integer a : userIds) {
                    User user = uService.getUserById(a);
                    UserAssessmentDto userAss = new UserAssessmentDto();
                    userAss.setUserId(user.getUserId());
                    userAss.setUserFullName(user.getUserFullName());
                    userAss.setUserType(user.getUserType());

                    // getting questions attempted by the user.
                    List<Integer> userQuestionIds = qService.userDoneAssQuestionsIds(assId, user.getUserId());
                    List<AssQuestionDto> assQuestions = new ArrayList<>();
                    for (Integer q : userQuestionIds) {
                        AssessmentQuestion question = assQuestService.getAssessmentQuestionById(q);
                        AssQuestionDto assQuestionDto = new AssQuestionDto();
                        assQuestionDto.setQuestionId(question.getAssessmentQuestionId());
                        assQuestionDto.setQuestionDescription(question.getAssessmentQuestionDescription());

                        //getting the choice selected by the user
                        List<Integer> selectedChoiceIds = qService.getEmployeeSelectedChoice(user.getUserId(), q, assId);
                        int selectedChoiceId = selectedChoiceIds.get(selectedChoiceIds.size() - 1);

                        List<ChoiceDto> qChoices = new ArrayList<>();
                        for (Choice c : question.getChoices()) {
                            ChoiceDto cDto = new ChoiceDto();
                            cDto.setChoiceName(c.getChoiceName());
                            cDto.setChoiceValue(Integer.parseInt(c.getChoiceValue()));
                            cDto.setChoiceId(c.getChoiceId());
                            if (c.getChoiceId() == selectedChoiceId) {
                                assQuestionDto.setEmployeeChoice(cDto);
                            }
                            qChoices.add(cDto);

                        }
                        assQuestionDto.setChoices(qChoices);
                        assQuestions.add(assQuestionDto);

                    }

                    userAss.setAssQuestionDtoList(assQuestions);
                    float averageScore = getEmployeeAverageScore(assQuestions);
                    userAss.setAverageScore(averageScore);
                    users.add(userAss);
                }


                Collections.sort(userIds);
                Collections.sort(manEmployees);

                Map<Integer, Integer> occurrences = new HashMap<>();

                // Combine the two lists
                List<Integer> combinedList = new ArrayList<>();
                combinedList.addAll(manEmployees);
                combinedList.addAll(userIds);

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


                for (Integer nonRepeatedInteger : nonRepeatedIntegers) {
                    User u = uService.getUserById(nonRepeatedInteger);
                    UserAssessmentDto newUser = new UserAssessmentDto();
                    newUser.setUserId(u.getUserId());
                    newUser.setUserFullName(u.getUserFullName());
                    newUser.setUserType(u.getUserType());
                    employeesNotDone.add(newUser);
                }


                List<AssQuestionDto> quests = new ArrayList<>();
                for (AssessmentQuestion q : ass.getAssessmentQuestions()) {
                    AssQuestionDto assQuestionDto = new AssQuestionDto();
                    assQuestionDto.setQuestionId(q.getAssessmentQuestionId());
                    assQuestionDto.setQuestionDescription(q.getAssessmentQuestionDescription());

                    List<ChoiceDto> choiceDtos = new ArrayList<>();
                    for (Choice c : q.getChoices()) {
                        ChoiceDto choiceDto = new ChoiceDto();
                        choiceDto.setChoiceId(c.getChoiceId());
                        choiceDto.setChoiceValue(Integer.parseInt(c.getChoiceValue()));
                        choiceDto.setChoiceName(c.getChoiceName());
                        choiceDtos.add(choiceDto);
                    }

                    assQuestionDto.setChoices(choiceDtos);
                    quests.add(assQuestionDto);
                }

                doneAss.setQuestions(quests);
                doneAss.setAssessmentName(ass.getAssessmentName());
                doneAss.setAssessmentDescription(ass.getAssessmentDescription());
                doneAss.setDoneBy(users);
                doneAss.setNotDoneBy(employeesNotDone);

                ApiResponse<DoneAssessmentDto> response = new ApiResponse<>(200, "successful");
                response.setItem(doneAss);
                return response;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ApiResponse<>(500, e.getMessage());
        }
    }

    @GetMapping("/employeeAss")
    public ApiResponse<UserDoneAssessmentDto> getEmployeeAss(@RequestParam Integer assId, @RequestParam Integer empId) {
        try {
            //calculating the users average score
            int maximumQuestionScore = 6;


            if (assId == null || empId == null || assId == 0 || empId == 0) {
                return new ApiResponse<>(301, "Enter a valid id");
            } else if (uService.getUserById(empId) == null || assService.getById(assId) == null) {
                if (uService.getUserById(empId) == null) {
                    return new ApiResponse<>(301, "User not found");

                } else {
                    return new ApiResponse<>(301, "Assessment not found");

                }
            } else {
                //getting the user from the db
                User user = uService.getUserById(empId);

                //setting user details
                UserAssessmentDto userAss = new UserAssessmentDto();
                userAss.setUserFullName(user.getUserFullName());
                userAss.setUserType(user.getUserType());
                userAss.setUserId(user.getUserId());

                //getting the assessment from the db
                Assessment ass = assService.getById(assId);

                // getting questions attempted by the user.
                List<Integer> userQuestionIds = qService.userDoneAssQuestionsIds(assId, user.getUserId());
                List<AssQuestionDto> assQuestions = new ArrayList<>();
                for (Integer q : userQuestionIds) {
                    int userSelectedChoiceCount = 0;
                    AssessmentQuestion question = assQuestService.getAssessmentQuestionById(q);
                    AssQuestionDto assQuestionDto = new AssQuestionDto();
                    assQuestionDto.setQuestionId(question.getAssessmentQuestionId());
                    assQuestionDto.setQuestionDescription(question.getAssessmentQuestionDescription());

                    //getting the choice selected by the user
                    List<Integer> selectedChoiceIds = qService.getEmployeeSelectedChoice(user.getUserId(), q, assId);
                    int selectedChoiceId = selectedChoiceIds.get(selectedChoiceIds.size() - 1);

                    List<ChoiceDto> qChoices = new ArrayList<>();
                    for (Choice c : question.getChoices()) {
                        ChoiceDto cDto = new ChoiceDto();
                        cDto.setChoiceName(c.getChoiceName());
                        cDto.setChoiceValue(Integer.parseInt(c.getChoiceValue()));
                        cDto.setChoiceId(c.getChoiceId());
                        if (c.getChoiceId() == selectedChoiceId) {
                            assQuestionDto.setEmployeeChoice(cDto);
                            userSelectedChoiceCount += cDto.getChoiceValue();
                        }
                        qChoices.add(cDto);

                    }
                    assQuestionDto.setChoices(qChoices);
                    assQuestions.add(assQuestionDto);

                    //calculating and assigning the average score
                    //int averageScore = userSelectedChoiceCount/(maximumQuestionScore * assQuestions.size());
                    //userAss.setAverageScore(averageScore);

                }
                userAss.setAssQuestionDtoList(assQuestions);
                UserDoneAssessmentDto userDone = new UserDoneAssessmentDto();
                for (AssessmentQuestion q : ass.getAssessmentQuestions()) {
                    AssQuestionDto newQ = new AssQuestionDto();
                    newQ.setQuestionId(q.getAssessmentQuestionId());
                    newQ.setQuestionDescription(q.getAssessmentQuestionDescription());

                    for (Choice c : q.getChoices()) {
                        ChoiceDto cDto = new ChoiceDto();
                        cDto.setChoiceId(c.getChoiceId());
                        cDto.setChoiceName(c.getChoiceName());
                        cDto.setChoiceValue(Integer.parseInt(c.getChoiceValue()));
                    }
                    userDone.getAssQuestions().add(newQ);
                }


                userDone.setUser(userAss);
                userDone.setAssName(ass.getAssessmentName());
                userDone.setAssDescription(ass.getAssessmentDescription());
                ApiResponse<UserDoneAssessmentDto> response = new ApiResponse<>(200, "successful");
                response.setItem(userDone);
                return response;
            }

        } catch (Exception e) {
            return new ApiResponse<>(200, e.getMessage());
        }

    }

    @PostMapping("/managerUpdateChoice")
    public ApiResponse<List<UserQuestionAnswer>> managerUpdateChoices(@RequestParam Integer assId, @RequestParam Integer manId, @RequestParam Integer empId, @RequestBody List<Answer> answers) {
        try {
            if (assId == null || manId == null || empId == null || assId == 0 || manId == 0 || empId == 0) {
                return new ApiResponse<>(301, "Enter a valid id");
            } else if (answers == null || answers.isEmpty()) {
                return new ApiResponse<>(301, "valid answers required");
            } else {
                //getting the row to update from the database
                List<UserQuestionAnswer> responses = new ArrayList<>();
                for (Answer a : answers) {
                    List<UserQuestionAnswer> userQuestionAnswers = qService.getUserQuestionAnswer(assId, empId, manId, a.getQuestionId());
                    UserQuestionAnswer currentUserAnswer = userQuestionAnswers.get(userQuestionAnswers.size() - 1);
                    currentUserAnswer.setMangerChoiceId(a.getChoiceId());
                    currentUserAnswer.setStatus(Constants.ASSESSED_ASSESSMENT);
                    UserQuestionAnswer updatedAnswer = qService.addUserAnswer(currentUserAnswer);
                    responses.add(updatedAnswer);
                }
                ApiResponse<List<UserQuestionAnswer>> response = new ApiResponse<>(200, "successful");
                response.setItem(responses);
                return response;
            }

        } catch (Exception e) {
            return new ApiResponse<>(500, e.getMessage());
        }

    }


    //getting assessed assessment details(Manager choices and user choices )
    @GetMapping("/assessedAssessmentDetails")
    public ApiResponse<AssessedAssessmentDto> assessedAssessmentDetails(@RequestParam Integer assId, @RequestParam Integer managerId, @RequestParam Integer empId) {
        try {
            if (assId == null || assId == 0 || managerId == null || managerId == 0) {
                return new ApiResponse<>(301, "Please enter a valid id");
            } else if (qService.userDoneAssessIds(assId, managerId) == null) {
                return new ApiResponse<>(403, "No user has done the current assessment");
            } else {

                //creating the object instance to return to the user
                AssessedAssessmentDto doneAss = new AssessedAssessmentDto();

                //getting the users manager
                Manager man = uService.getManagerById(managerId);

                //defining a manager instance to be associated with the user
                ManagerDto managerDto = new ManagerDto();
                managerDto.setId(Long.valueOf(man.getUserId()));
                managerDto.setUserType(man.getUserType());
                managerDto.setUserFullName(man.getUserFullName());

                managerDto.setRoleId(Math.toIntExact(man.getRole().getId()));

                //getting the user who did the assessment
                User user = uService.getUserById(empId);

                //getting the original assessment done by the user
                Assessment ass = assService.getById(assId);

                //assigning the response values
                doneAss.setId(ass.getAssessmentId());
                doneAss.setAssName(ass.getAssessmentName());
                doneAss.setAssDescription(ass.getAssessmentDescription());

                // getting questions attempted by the user.
                List<Integer> userQuestionIds = qService.userDoneAssQuestionsIds(assId, user.getUserId());
                List<AssQuestionDto> assQuestions = new ArrayList<>();

                //creating user to return with the response
                UserAssessmentDto doneAssUser = new UserAssessmentDto();

                //assigning the user data
                doneAssUser.setUserId(user.getUserId());
                doneAssUser.setMan(managerDto);
                doneAssUser.setUserType(user.getUserType());
                doneAssUser.setUserFullName(user.getUserFullName());

                for (Integer q : userQuestionIds) {
                    AssessmentQuestion question = assQuestService.getAssessmentQuestionById(q);
                    AssQuestionDto assQuestionDto = new AssQuestionDto();
                    assQuestionDto.setQuestionId(question.getAssessmentQuestionId());
                    assQuestionDto.setQuestionDescription(question.getAssessmentQuestionDescription());

                    //getting the choice selected by the user
                    List<Integer> selectedChoiceIds = qService.getEmployeeSelectedChoice(user.getUserId(), q, assId);
                    int selectedChoiceId = selectedChoiceIds.get(selectedChoiceIds.size() - 1);

                    //getting the manager selected choice
                    List<Integer> managerSelectedChoice= qService.getManagerSelectedChoice(empId, q, assId, managerId);
                    int managerSelectedChoiceId = managerSelectedChoice.get(managerSelectedChoice.size() - 1);

                    List<ChoiceDto> qChoices = new ArrayList<>();
                    for (Choice c : question.getChoices()) {
                        ChoiceDto cDto = new ChoiceDto();
                        cDto.setChoiceName(c.getChoiceName());
                        cDto.setChoiceValue(Integer.parseInt(c.getChoiceValue()));
                        cDto.setChoiceId(c.getChoiceId());
                        if (c.getChoiceId() == selectedChoiceId) {
                            assQuestionDto.setEmployeeChoice(cDto);
                        }
                        if (c.getChoiceId() == managerSelectedChoiceId) {
                            assQuestionDto.setManagerChoice(cDto);
                        }
                        qChoices.add(cDto);
                    }
                    assQuestionDto.setChoices(qChoices);
                    assQuestions.add(assQuestionDto);
                }
                doneAssUser.setAssQuestionDtoList(assQuestions);
                float empAverage = getEmployeeAverageScore(assQuestions);
                float manAverage = getManagerAverageScore(assQuestions);
                doneAssUser.setAverageScore(empAverage);
                doneAssUser.setManagerAverageScore(manAverage);
                doneAss.setDoneBy(doneAssUser);

                ApiResponse<AssessedAssessmentDto> response = new ApiResponse<>(200, "successful");
                response.setItem(doneAss);
                return response;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ApiResponse<>(500, e.getMessage());
        }
    }
    /// Newer endpoints
    @GetMapping("/getAssessedAndUnAssessed")
    public ApiResponse<AssessedVsUnassessedDto> assessedVsUnAssessed(@RequestParam Integer assId, @RequestParam Integer managerId){
        try {
            if (assId == null || assId == 0 || managerId == null || managerId == 0) {
                return new ApiResponse<>(301, "Please enter a valid id");
            }else if(uService.getUserById(managerId) == null) {
                return new ApiResponse<>(301, "No user existing with the given id");

            }else if(assService.getById(assId) == null){
                return new ApiResponse<>(301, "No assessment existing with the given id");
            }else{
                //initialising the response to return
                AssessedVsUnassessedDto doneAss = new AssessedVsUnassessedDto();

                //getting the manager from the database
                Manager man = uService.getManagerById(managerId);

                //getting the assessment from the database
                Assessment ass = assService.getById(assId);

                //binding the response with the required details
                doneAss.setId(ass.getAssessmentId());
                doneAss.setAssName(ass.getAssessmentName());
                doneAss.setAssDescription(ass.getAssessmentDescription());

                //getting all the users who have been assessed from the given assessment
                List<Integer> assessedUsers = qService.getAssessedEmployees(managerId, assId);

                //creating a bag to store all assessed users
                List<UserAssessmentDto> assessedResponseUsers= new ArrayList<>();

                //getting the users from the database
                if(assessedUsers != null && assessedUsers.size() > 0){
                    for(Integer i: assessedUsers){
                        User user = uService.getUserById(i);
                        UserAssessmentDto u = new UserAssessmentDto();
                        u.setUserId(i);
                        u.setUserType(user.getUserType());
                        u.setUserFullName(user.getUserFullName());

                        // getting questions attempted by the user.
                        List<Integer> userQuestionIds = qService.userDoneAssQuestionsIds(assId, user.getUserId());
                        List<AssQuestionDto> assQuestions = new ArrayList<>();
                        for (Integer q : userQuestionIds) {
                            AssessmentQuestion question = assQuestService.getAssessmentQuestionById(q);
                            AssQuestionDto assQuestionDto = new AssQuestionDto();
                            assQuestionDto.setQuestionId(question.getAssessmentQuestionId());
                            assQuestionDto.setQuestionDescription(question.getAssessmentQuestionDescription());

                            //getting the choice selected by the user
                            List<Integer> selectedChoiceIds = qService.getEmployeeSelectedChoice(user.getUserId(), q, assId);
                            int selectedChoiceId = selectedChoiceIds.get(selectedChoiceIds.size() - 1);

                            //getting the manager selected choice
                            List<Integer> managerSelectedChoice= qService.getManagerSelectedChoice(user.getUserId(), q, assId, managerId);
                            int managerSelectedChoiceId = managerSelectedChoice.get(managerSelectedChoice.size() - 1);

                            List<ChoiceDto> qChoices = new ArrayList<>();
                            for (Choice c : question.getChoices()) {
                                ChoiceDto cDto = new ChoiceDto();
                                cDto.setChoiceName(c.getChoiceName());
                                cDto.setChoiceValue(Integer.parseInt(c.getChoiceValue()));
                                cDto.setChoiceId(c.getChoiceId());

                                if (c.getChoiceId() == managerSelectedChoiceId) {
                                    assQuestionDto.setManagerChoice(cDto);
                                }

                                if (c.getChoiceId() == selectedChoiceId) {
                                    assQuestionDto.setEmployeeChoice(cDto);
                                }
                                qChoices.add(cDto);

                            }
                            assQuestionDto.setChoices(qChoices);
                            assQuestions.add(assQuestionDto);
                        }

                        //calculating the users average score
                        u.setAverageScore(getEmployeeAverageScore(assQuestions));

                        //calculating the managers average score
                        u.setManagerAverageScore(getManagerAverageScore(assQuestions));


                        u.setAssQuestionDtoList(assQuestions);
                        assessedResponseUsers.add(u);
                    }
                }


                //getting all un-assessed users from the given assessment
                List<Integer> unAssessedUsers = qService.getUnAssessedEmployees(managerId, assId);

                //defining a bag to store all un assessed users
                List<UserAssessmentDto> unAssessedResponseUsers = new ArrayList<>();

                if(unAssessedUsers != null && unAssessedUsers.size() > 0){
                    for(Integer u : unAssessedUsers){
                        User user = uService.getUserById(u);
                        UserAssessmentDto notAssessed = new UserAssessmentDto();
                        notAssessed.setUserId(u);
                        notAssessed.setUserType(user.getUserType());
                        notAssessed.setUserFullName(user.getUserFullName());


                        // getting questions attempted by the user.
                        List<Integer> userQuestionIds = qService.userDoneAssQuestionsIds(assId, user.getUserId());
                        List<AssQuestionDto> assQuestions = new ArrayList<>();
                        for (Integer q : userQuestionIds) {
                            AssessmentQuestion question = assQuestService.getAssessmentQuestionById(q);
                            AssQuestionDto assQuestionDto = new AssQuestionDto();
                            assQuestionDto.setQuestionId(question.getAssessmentQuestionId());
                            assQuestionDto.setQuestionDescription(question.getAssessmentQuestionDescription());

                            //getting the choice selected by the user
                            List<Integer> selectedChoiceIds = qService.getEmployeeSelectedChoice(user.getUserId(), q, assId);
                            int selectedChoiceId = selectedChoiceIds.get(selectedChoiceIds.size() - 1);

                            //getting the manager selected choice
                            List<Integer> managerSelectedChoice= qService.getManagerSelectedChoice(user.getUserId(), q, assId, managerId);
                            int managerSelectedChoiceId = managerSelectedChoice.get(managerSelectedChoice.size() - 1);

                            List<ChoiceDto> qChoices = new ArrayList<>();
                            for (Choice c : question.getChoices()) {
                                ChoiceDto cDto = new ChoiceDto();
                                cDto.setChoiceName(c.getChoiceName());
                                cDto.setChoiceValue(Integer.parseInt(c.getChoiceValue()));
                                cDto.setChoiceId(c.getChoiceId());

                                if (c.getChoiceId() == managerSelectedChoiceId) {
                                    assQuestionDto.setManagerChoice(cDto);
                                }

                                if (c.getChoiceId() == selectedChoiceId) {
                                    assQuestionDto.setEmployeeChoice(cDto);
                                }
                                qChoices.add(cDto);

                            }
                            assQuestionDto.setChoices(qChoices);
                            assQuestions.add(assQuestionDto);
                        }

                        //calculating the users average score
                        notAssessed.setAverageScore(getEmployeeAverageScore(assQuestions));
                        notAssessed.setAssQuestionDtoList(assQuestions);
                        unAssessedResponseUsers.add(notAssessed);
                    }
                }
                doneAss.setAssessed(assessedResponseUsers);
                doneAss.setUnAssessed(unAssessedResponseUsers);

                ApiResponse<AssessedVsUnassessedDto> response = new ApiResponse<>(200, "successful");
                response.setItem(doneAss);
                return response;

            }
        }catch (Exception e){
            return new ApiResponse<>(500, e.getMessage());
        }
    }


    private float getEmployeeAverageScore(List<AssQuestionDto> quests) {
        int count = 0;
        int maximumScore = 6 * quests.size();

        for (AssQuestionDto q : quests) {
            count += q.getEmployeeChoice().getChoiceValue();
        }

        float average = ((float) count) / maximumScore;
        return average;
    }

    private float getManagerAverageScore(List<AssQuestionDto> quests){
        int count = 0;
        int maximumScore = 6 * quests.size();

        for (AssQuestionDto q : quests) {
            count += q.getManagerChoice().getChoiceValue();
        }

        float average = ((float) count) / maximumScore;
        return average;
    }

}
