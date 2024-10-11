package com.example.talent_man.controllers;

import com.example.talent_man.dto.PerformanceRequestDto;
import com.example.talent_man.dto.PerformanceYearlyDto;
import com.example.talent_man.dto.RolesStrategiesDto;
import com.example.talent_man.dto.UserPerformanceDTO;
import com.example.talent_man.models.CriticalRolesStrategies;
import com.example.talent_man.models.Performance;
import com.example.talent_man.repos.PerformanceRepository;
import com.example.talent_man.services.PerformanceService;
import com.example.talent_man.utils.ApiResponse;
import com.example.talent_man.utils.CombinedDataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/api/performances")
public class PerformanceController {

    @Autowired
    private PerformanceService performanceService;

    @Autowired
    private PerformanceRepository repository;

        @PostMapping("/add-for-user/{pf}")
        public ApiResponse<Void> addPerformanceForUser(
                @PathVariable String pf,
                @RequestBody List<PerformanceRequestDto> performanceDtos) {

            ApiResponse<Void> response = new ApiResponse<>();

            try {
                ResponseEntity<ApiResponse<String>> serviceResponse = performanceService.addPerformanceForUser(pf, performanceDtos);
                response.setMessage(serviceResponse.getBody().getMessage());
                response.setStatus(serviceResponse.getStatusCodeValue());
                response.setItem(null); // Optionally set item to null or leave it out
            } catch (Exception e) {
                response.setMessage("Error adding performances: " + e.getMessage());
                response.setStatus(500);
                response.setItem(null); // Optionally set item to null or leave it out
            }

            return response;
        }

        @PostMapping("/import")
        public ApiResponse<Void> importPerformancesFromExcel(@RequestParam("file") MultipartFile file) {

            ApiResponse<Void> response = new ApiResponse<>();

            try {
                // Convert MultipartFile to InputStream
                InputStream inputStream = file.getInputStream();
                ResponseEntity<ApiResponse<String>> serviceResponse = performanceService.importPerformancesFromExcel(inputStream);
                response.setMessage(serviceResponse.getBody().getMessage());
                response.setStatus(serviceResponse.getStatusCodeValue());
                response.setItem(null); // Optionally set item to null or leave it out
            } catch (Exception e) {
                response.setMessage("Error importing performances: " + e.getMessage());
                response.setStatus(500);
                response.setItem(null); // Optionally set item to null or leave it out
            }

            return response;
        }

        @GetMapping("/get-by-user/{pf}")
        public ApiResponse<List<PerformanceRequestDto>> getPerformancesByUser(@PathVariable String pf) {

            ApiResponse<List<PerformanceRequestDto>> response = new ApiResponse<>();

            try {
                List<PerformanceRequestDto> performances = performanceService.getPerformancesByUser(pf);

                if (performances.isEmpty()) {
                    response.setMessage("No performances found for user with PF number: " + pf);
                    response.setStatus(404);
                    response.setItem(null); // Optionally set item to null or leave it out
                } else {
                    response.setMessage("Performances retrieved successfully for user with PF number: " + pf);
                    response.setStatus(200);
                    response.setItem(performances);
                }
            } catch (Exception e) {
                response.setMessage("Error retrieving performances: " + e.getMessage());
                response.setStatus(500);
                response.setItem(null); // Optionally set item to null or leave it out
            }

            return response;
        }

        @PostMapping("/create/users/{userId}")
        public ApiResponse<Object> createPerformanceForUser(
                @PathVariable int userId,
                @RequestBody PerformanceRequestDto performanceRequestDto) {

            ApiResponse<Object> res = new ApiResponse<>();

            try {
                Performance performance = performanceService.createPerformanceForUser(userId, performanceRequestDto);
                res.setMessage("Performance added successfully");
                res.setStatus(HttpStatus.OK.value());
                res.setItem(performance);
            } catch (IllegalArgumentException e) {
                // Handle specific exception
                res.setMessage(e.getMessage());
                res.setStatus(HttpStatus.BAD_REQUEST.value());
                res.setItem(null);
            } catch (Exception e) {
                // Handle generic exceptions
                res.setMessage("An error occurred while creating the performance record");
                res.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                res.setItem(null);
            }

            return res;
        }



    @GetMapping("/talentMapping/{managerId}")
    public ApiResponse<Object> getUserPerformancesByManagerId(@PathVariable int managerId) {
       List<PerformanceRepository.TalentInterface> talent = performanceService.getUserPerformancesByManagerId(managerId);
        ApiResponse<Object> res = new ApiResponse<>();
        res.setMessage("Employee Talent Mapping retrieved successfully ");
        res.setStatus(200);
        res.setItem(talent);
        return res;
    }

//    @GetMapping("/employees/{managerId}")
//    public ApiResponse<Object> getPerformancesByManagerId(@PathVariable int managerId) {
//        List<PerformanceRepository.performanceInterface> talent = performanceService.getPerformancesByManagerId(managerId);
//        ApiResponse<Object> res = new ApiResponse<>();
//        res.setMessage("Manager Employee performances retrieved successfully");
//        res.setStatus(200);
//        res.setItem(talent);
//        return res;
//    }
    @GetMapping("/employees/{managerId}")
    public ApiResponse<List<PerformanceYearlyDto>> getPerformancesByManagerId(@PathVariable int managerId) {
        List<PerformanceYearlyDto> performances = performanceService.getPerformancesByManagerId(managerId);
        ApiResponse<List<PerformanceYearlyDto>> res = new ApiResponse<>();
        res.setMessage("Manager Employee performances retrieved successfully");
        res.setStatus(200);
        res.setItem(performances);
        return res;
    }

    @GetMapping("/employee/{employeeId}")
    public ApiResponse<PerformanceYearlyDto> getEmployeePerformance(@PathVariable int employeeId) {

        PerformanceYearlyDto performances = performanceService.getPerformanceByEmployeeId(employeeId);
        System.out.println("hello" + performances);
        ApiResponse<PerformanceYearlyDto> res = new ApiResponse<>();
        res.setMessage("Employee performances retrieved successfully");
        res.setStatus(200);
        res.setItem(performances);
        return res;
    }

    @GetMapping("/talentMapping/all/employees")
    public ApiResponse<Object> getAllUserPerformances() {

        List<PerformanceRepository.TalentInterface> talent = performanceService.getAllUserPerformances();
        ApiResponse<Object> res = new ApiResponse<>();
        res.setMessage("Employee Talent Mapping retrieved successfully ");
        res.setStatus(200);
        res.setItem(talent);
        return res;
    }

    @GetMapping("/HIPOs/all/employees/{managerId}")
    public CombinedDataResponse getAllUserHIPOs(@PathVariable int managerId) {
        List<UserPerformanceDTO> talent = performanceService.getAllUserHIPOs(managerId);
        System.out.println("items: " + talent);
        // Generate combined data for each user
        Map<Integer, CombinedDataResponse.Item> combinedData = generateCombinedData(talent);

        // Create a CombinedDataResponse object
        CombinedDataResponse response = new CombinedDataResponse();

        // Convert combined data map to list of items
        List<CombinedDataResponse.Item> items = new ArrayList<>(combinedData.values());

        // Set the list of items in the response
        response.setItems(items);
        response.setStatus(200);
        response.setMessage("Employee HIPOs retrieved successfully");

        return response;
    }

    //get Hipos by year
    @GetMapping("/HIPOs/all/employees/{managerId}/{year}")
    public CombinedDataResponse getAllUserHIPOsByYear(@PathVariable int managerId, @PathVariable int year) {
        List<UserPerformanceDTO> talent = performanceService.getAllUserHIPObyYear(managerId, year);
        Map<Integer, CombinedDataResponse.Item> combinedData = generateCombinedData(talent);

        // Create a CombinedDataResponse object
        CombinedDataResponse response = new CombinedDataResponse();

        // Convert combined data map to list of items
        List<CombinedDataResponse.Item> items = new ArrayList<>(combinedData.values());

        // Set the list of items in the response
        response.setItems(items);
        response.setStatus(200);
        response.setMessage("Employee HIPOs retrieved successfully");

        return response;
    }

    private Map<Integer, CombinedDataResponse.Item> generateCombinedData(List<UserPerformanceDTO> talent) {
        Map<Integer, CombinedDataResponse.Item> combinedData = new HashMap<>();

        for (UserPerformanceDTO dto : talent) {
            int userId = dto.getUserId(); // Use userId instead of id

            // Create or retrieve the CombinedDataResponse.Item object for the userId
            CombinedDataResponse.Item item = combinedData.getOrDefault(userId, new CombinedDataResponse.Item());

            // Set the userId if it's not already set
            if (item.getUserId() == null) {
                item.setUserId(String.valueOf(userId));
                // Set other properties as needed
                item.setPf_No(dto.getPf_No());
                item.setUsername(dto.getUsername());
                item.setManagerId(dto.getManagerId());
                item.setDepartmentName(dto.getDepartmentName());
                item.setPositionName(dto.getPositionName());
                // Set other properties based on the DTO
                item.setUserFullName(dto.getUserFullName());
                item.setAveragePerformance(dto.getAveragePerformance());
                item.setAveragePotential(dto.getAveragePotential());
                item.setUserAssessmentAvg(dto.getUserAssessmentAvg());
                item.setManAssessmentAvg(dto.getManAssessmentAvg());
                item.setAssessmentName(dto.getAssessmentName());
                item.setTalentRating(dto.getTalentRating());
                item.setPotentialRating(dto.getPotentialRating());
                item.setPerformanceRating(dto.getPerformanceRating());
                item.setPerformanceYear(dto.getPerformanceYear());
            }

            // Extract potential attribute and manAssessmentAvg from each DTO
            String potentialAttribute = dto.getPotentialAttributeName();
            Double manAssessmentAvg = dto.getManAssessmentAvg();

            System.out.println("Unrecognized potential attribute: " +dto.getPotentialAttributeName());
            // Set the manAssessmentAvg for the potentialAttribute
            // Assuming there's only one score per potential attribute for each user in this case we are using manager score.
            if (manAssessmentAvg == null || manAssessmentAvg == 0 || manAssessmentAvg.isNaN()){
                throw new RuntimeException("You have some  un assessed employees ");

            } else {
                switch (potentialAttribute.trim()) {
                    case "Drive":
                        item.setDriveScore(manAssessmentAvg);
                        break;
                    case "Change Agility":
                        item.setChangeAgilityScore(manAssessmentAvg);
                        break;
                    case "Judgement":
                        item.setJudgmentScore(manAssessmentAvg);
                        break;
                    case "Aspiration":
                        item.setAspirationScore(manAssessmentAvg);
                        break;
                    default:
                        // Log the unrecognized potential attribute names
                        System.out.println("Unrecognized potential attribute: " + potentialAttribute);
                        break;
                }
            }

            // Put the updated item back into the combinedData map
            combinedData.put(userId, item);
        }

        return combinedData;
    }

    // Method to convert UserPerformanceDTO to Item
    private List<CombinedDataResponse.Item> convertToItems(List<UserPerformanceDTO> talent) {
        List<CombinedDataResponse.Item> items = new ArrayList<>();
        for (UserPerformanceDTO dto : talent) {
            CombinedDataResponse.Item item = new CombinedDataResponse.Item();
            // Set properties from UserPerformanceDTO to Item
            item.setUserId(String.valueOf(dto.getUserId()));
            item.setUsername(dto.getUsername());
            item.setPf_No(dto.getPf_No());
            item.setDepartmentName(dto.getDepartmentName());
            item.setPositionName(dto.getPositionName());
            item.setManagerId(dto.getManagerId());
            item.setAveragePerformance(dto.getAveragePerformance());
            item.setAveragePotential(dto.getAveragePotential());
            item.setUserAssessmentAvg(dto.getUserAssessmentAvg());
            item.setManAssessmentAvg(dto.getManAssessmentAvg());
            item.setAssessmentName(dto.getAssessmentName());
            item.setPotentialAttributeName(dto.getPotentialAttributeName());
            item.setTalentRating(dto.getTalentRating());
            item.setPotentialRating(dto.getPotentialRating());
            item.setPerformanceRating(dto.getPerformanceRating());
            item.setPerformanceYear(dto.getPerformanceYear());
            // Add more properties as needed
            items.add(item);
        }
        return items;
    }

    //add hipo proposed development interventions


}
