package com.example.talent_man.service_imp;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.example.talent_man.dto.*;
import com.example.talent_man.models.Performance;
import com.example.talent_man.models.user.User;
import com.example.talent_man.repos.PerformanceRepository;
import com.example.talent_man.repos.user.UserRepo;
import com.example.talent_man.services.PerformanceService;
import com.example.talent_man.utils.ApiResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PerformanceServiceImpl implements PerformanceService {

    @Autowired
    private UserRepo userRepository;

    @Autowired
    private PerformanceRepository performanceRepository;

    // Method to convert YearDto to java.time.Year

    private Year toYear(YearDto yearDto) {
        return Year.of(yearDto.getValue());
    }
    @Override
    public Performance createPerformanceForUser(int userId, PerformanceRequestDto performanceRequestDto) {
        // Find the user by userId
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // Convert YearDto to java.time.Year
        Year year;
        try {
            year = toYear(performanceRequestDto.getYear());
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid year format in request", e);
        }

        // Check if a performance record for the same year and quarter already exists for the user
        boolean exists = performanceRepository.existsByUserAndYearAndQuarter(user, year, performanceRequestDto.getQuarter());
        if (exists) {
            throw new IllegalArgumentException("Performance record already exists for user with ID " + userId +
                    " for year " + year + " and quarter " + performanceRequestDto.getQuarter());
        }


        // Create a new Performance entity and set its fields
        Performance performance = new Performance();
        performance.setUser(user);
        performance.setYear(year);
        performance.setQuarter(performanceRequestDto.getQuarter());
        performance.setPerformanceMetric(performanceRequestDto.getPerformanceMetric());

        // Save the performance and return it
        try {
            return performanceRepository.save(performance);
        } catch (Exception e) {
            throw new RuntimeException("Error saving performance record", e);
        }
    }
    @Override
    public ResponseEntity<ApiResponse<String>> addPerformanceForUser(String pf, List<PerformanceRequestDto> performanceRequestDtos) {
        Optional<User> userOptional = userRepository.findByPf(pf);

        // Check if user exists
        if (!userOptional.isPresent()) {
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "User with PF " + pf + " not found"), HttpStatus.NOT_FOUND);
        }

        User user = userOptional.get();
        List<Performance> performances = new ArrayList<>();
        List<String> errorMessages = new ArrayList<>();

        // Loop through performance DTOs
        for (PerformanceRequestDto dto : performanceRequestDtos) {
            Year year;
            try {
                // Try converting YearDto to Year
                year = toYear(dto.getYear());
            } catch (Exception e) {
                // Add error message if the year conversion fails
                errorMessages.add("Invalid year format for PF " + pf + " for year " + dto.getYear().getValue());
                continue;  // Skip to the next DTO
            }

            int quarter = dto.getQuarter();

            // Check if a performance for the same year and quarter already exists for the user
            if (performanceRepository.existsByUserAndYearAndQuarter(user, year, quarter)) {
                errorMessages.add("Performance record already exists for user with PF " + pf + " for year " + year + " and quarter " + quarter);
            } else {
                // Create new performance record if it doesn't exist
                Performance performance = new Performance();
                performance.setUser(user);
                performance.setYear(year);
                performance.setQuarter(quarter);
                performance.setPerformanceMetric(dto.getPerformanceMetric());
                performances.add(performance);
            }
        }

        // If there were any errors, return them in the response
        if (!errorMessages.isEmpty()) {
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), String.join(", ", errorMessages)), HttpStatus.BAD_REQUEST);
        }

        // Save all valid performance records
        performanceRepository.saveAll(performances);

        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK.value(), "Performances added successfully"), HttpStatus.OK);
    }


    @Override
    public ResponseEntity<ApiResponse<String>> importPerformancesFromExcel(InputStream inputStream) {
        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0); // Assuming data is in the first sheet
            Iterator<Row> rowIterator = sheet.iterator();

            // Skip header row
            if (rowIterator.hasNext()) {
                rowIterator.next();
            }

            List<Performance> performances = new ArrayList<>();
            List<String> errors = new ArrayList<>();

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                try {
                    // Read values from each cell
                    String pf = getCellValueAsString(row.getCell(0)); // PF number should be treated as a string
                    int yearValue = (int) row.getCell(1).getNumericCellValue(); // Read year as integer
                    YearDto yearDto = new YearDto(yearValue, false); // Create YearDto
                    Year year = toYear(yearDto); // Convert YearDto to Year
                    int quarter = (int) row.getCell(2).getNumericCellValue();
                    double performanceMetric = row.getCell(3).getNumericCellValue();

                    // Fetch the User based on the PF number
                    Optional<User> userOptional = userRepository.findByPf(pf);
                    if (userOptional.isPresent()) {
                        User user = userOptional.get();

                        // Check if a performance for the same year and quarter already exists for the user
                        if (performanceRepository.existsByUserAndYearAndQuarter(user, year, quarter)) {
                            errors.add("Performance record already exists for user with PF number " + pf + " for year " + yearValue + " and quarter " + quarter);
                            continue; // Skip adding this record
                        }

                        Performance performance = new Performance();
                        performance.setUser(user);
                        performance.setYear(year);
                        performance.setQuarter(quarter);
                        performance.setPerformanceMetric(performanceMetric);

                        performances.add(performance);
                    } else {
                        errors.add("User with PF number " + pf + " not found.");
                    }
                } catch (Exception e) {
                    errors.add("Error processing row: " + e.getMessage());
                }
            }

            // Save all performances
            if (!performances.isEmpty()) {
                performanceRepository.saveAll(performances);
            }

            if (!errors.isEmpty()) {
                return new ResponseEntity<>(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), String.join("; ", errors), 400), HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK.value(), "Performances imported successfully", 200), HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace(); // Log exception
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error processing Excel file: " + e.getMessage(), 500), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Helper method to get cell value as string
    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    // If it's a date, convert to a string representation
                    return cell.getDateCellValue().toString();
                } else {
                    // Convert numeric value to string
                    return String.valueOf((long) cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }


    @Override
    public List<PerformanceRequestDto> getPerformancesByUser(String pf) {
        return performanceRepository.findAll().stream()
                .filter(performance -> performance.getUser().getPf().equals(pf))
                .map(performance -> new PerformanceRequestDto(
                        new YearDto(performance.getYear().getValue(), performance.getYear().isLeap()),
                        performance.getQuarter(),
                        performance.getPerformanceMetric()
                ))
                .collect(Collectors.toList());
    }
    // Method to convert YearDto to java.time.Year


    @Override
    public List<PerformanceRepository.TalentInterface> getUserPerformancesByManagerId(int managerId) {
        try {
            return performanceRepository.getUserPerformancesByManagerId(managerId);
        }catch (Exception e){
            throw new RuntimeException("Error occurred while getting employee talent mapping: " + e.getMessage(), e);
        }
    }

    @Override
    public List<PerformanceRepository.TalentInterface> getAllUserPerformances() {
        try {
            return performanceRepository.getAllUserPerformances();
        }catch (Exception e){
            throw new RuntimeException("Error occurred while getting employee talent mapping: " + e.getMessage(), e);
        }
    }


    @Override
    public List<PerformanceYearlyDto> getPerformancesByManagerId(int managerId) {
        List<PerformanceRepository.performanceInterface> performanceInterfaces = performanceRepository.getAllUsersPerformances(managerId);
        Map<Integer, PerformanceYearlyDto> performanceMap = new HashMap<>();

        for (PerformanceRepository.performanceInterface performanceInterface : performanceInterfaces) {
            int userId = performanceInterface.getUserId();
            String userFullName = performanceInterface.getUserFullName();
            double performanceRating = performanceInterface.getPerformanceRating();
            int performanceYear = performanceInterface.getPerformanceYear();
            int yearsCount = performanceInterface.getYearsCount();
            double totalPerformanceRating = performanceInterface.getAveragePerformanceLastThreeYears();
            if (performanceMap.containsKey(userId)) {
                performanceMap.get(userId).getPerformances().add(new PerformanceDto(performanceRating, performanceYear));
            } else {
                PerformanceYearlyDto performanceYearlyDto = new PerformanceYearlyDto(userId, userFullName,yearsCount,totalPerformanceRating, new ArrayList<>());
                performanceYearlyDto.getPerformances().add(new PerformanceDto(performanceRating, performanceYear));
                performanceMap.put(userId, performanceYearlyDto);
            }
        }

        return new ArrayList<>(performanceMap.values());
    }




    @Override
    public PerformanceYearlyDto getPerformanceByEmployeeId(int employeeId) {
        List<PerformanceRepository.performanceInterface> performanceInterfaces = performanceRepository.getPerformanceByEmployeeId(employeeId);
        PerformanceYearlyDto performanceYearlyDto = null;

        for (PerformanceRepository.performanceInterface performanceInterface : performanceInterfaces) {
            int userId = performanceInterface.getUserId();
            String userFullName = performanceInterface.getUserFullName();
            double performanceRating = performanceInterface.getPerformanceRating();
            int performanceYear = performanceInterface.getPerformanceYear();
            int yearsCount = performanceInterface.getYearsCount();
            double totalPerformanceRating = performanceInterface.getAveragePerformanceLastThreeYears();

            if (performanceYearlyDto == null) {
                performanceYearlyDto = new PerformanceYearlyDto(userId, userFullName, yearsCount, totalPerformanceRating, new ArrayList<>());
            }

            performanceYearlyDto.getPerformances().add(new PerformanceDto(performanceRating, performanceYear));
        }

        return performanceYearlyDto;
    }

    @Override
    public List<UserPerformanceDTO> getAllUserHIPOs(int managerId) {
        try {
            List<PerformanceRepository.UserPerformanceData> talentInterfaces = performanceRepository.getAllUserHIPOs(managerId);
            List<UserPerformanceDTO> dtos = new ArrayList<>();
            UserPerformanceDTO data = new UserPerformanceDTO();

            double totalManAssessmentAvg = 0.0; // Add this line to store the total
            for (PerformanceRepository.UserPerformanceData talentInterface : talentInterfaces) {
                UserPerformanceDTO dto = mapToDTO(talentInterface);
                dtos.add(dto);
            }
            return dtos;
        } catch (Exception e){
            throw new RuntimeException("Error occurred while getting employee talent mapping: " + e.getMessage(), e);
        }
    }
    public List<UserPerformanceDTO> getAllUserHIPObyYear(int managerId, int year) {
        try {
            List<PerformanceRepository.UserPerformanceData> talentInterfaces = performanceRepository.getAllUserHIPOsByYear(managerId, year);
            List<UserPerformanceDTO> dtos = new ArrayList<>();
            UserPerformanceDTO data = new UserPerformanceDTO();

            double totalManAssessmentAvg = 0.0; // Add this line to store the total
            for (PerformanceRepository.UserPerformanceData talentInterface : talentInterfaces) {
                UserPerformanceDTO dto = mapToDTO(talentInterface);
                dtos.add(dto);
            }
            return dtos;
        } catch (Exception e){
            throw new RuntimeException("Error occurred while getting employee talent mapping: " + e.getMessage(), e);
        }
    }

    private UserPerformanceDTO mapToDTO(PerformanceRepository.UserPerformanceData talentInterface) {
        UserPerformanceDTO dto = new UserPerformanceDTO();
        dto.setUserId(talentInterface.getUserId());
        dto.setPf_No(talentInterface.getPf());
        dto.setDepartmentName(talentInterface.getDepartmentName());
        dto.setPositionName(talentInterface.getPositionName());
        dto.setUsername(talentInterface.getUsername());
        dto.setAveragePerformance(talentInterface.getAveragePerformance());
        dto.setUserAssessmentAvg(talentInterface.getUserAssessmentAvg());
        dto.setPerformanceYear(talentInterface.getPerformanceYear());
        dto.setPotentialAttributeName(talentInterface.getPotentialAttribute());
        dto.setAveragePotential(talentInterface.getPotentialRating());
        dto.setUserFullName(talentInterface.getUserFullName());
        // Calculate and set talentRating and potentialRating if needed
        //dto.setTalentRating(talentInterface.getPotentialRating());
        dto.setPotentialRating(calculatePotentialRating(talentInterface));
        dto.setManAssessmentAvg(talentInterface.getManAssessmentAvgWithDefault());
        dto.setTalentRating( calculateTalentRating(talentInterface));
        dto.setPerformanceRating(calculatePerformanceRating(talentInterface));
        System.out.println("HELOO" + talentInterface.getPerformanceYear() + talentInterface.getAveragePerformance());

        return dto;
    }

    private Integer calculatePerformanceRating(PerformanceRepository.UserPerformanceData talentInterface) {
        // Your logic to calculate talent rating based on performance metrics
        // Example:
        double averagePerformance = talentInterface.getAveragePerformance();
        if (averagePerformance >= 1 && averagePerformance <= 2.9) {
            return 3;
        } else if (averagePerformance >=3 && averagePerformance <= 4) {
            return 2;
        } else {
            return 1;
        }
    }

    private String calculatePotentialRating(PerformanceRepository.UserPerformanceData talentInterface) {

        double potential = talentInterface.getPotentialRating() != null ? talentInterface.getPotentialRating() : 0.0;
        // Determine potential rating based on average manAssessmentAvg
        if (potential >= 1 && potential <= 1.9) {
            return "C";
        } else if (potential >= 2 && potential < 4) {
            return "B";
        } else if((potential >= 4 && potential <= 6)){
            return "A";
        }
        else {
            return "low";
        }
    }

    private String calculateTalentRating(PerformanceRepository.UserPerformanceData talentInterface) {
        // Your logic to calculate potential rating based on assessment metrics
        // Example:
        double potential = talentInterface.getPotentialRating() != null ? talentInterface.getPotentialRating() : 0.0;
        double perfomance = talentInterface.getAveragePerformance();

        if ((perfomance >= 4.1 && perfomance <= 5) && (potential >= 4 && potential <= 6)) {

            return "A1";
        } else if ((perfomance >= 4 && perfomance <= 5) && (potential >= 2 && potential < 4)) {
            return "B1";
        }  else if ((perfomance >= 4 && perfomance <= 5) && (potential >= 1 && potential <= 1.9)) {
            return "C1";
        }else if ((perfomance >= 2.9 && perfomance <= 3.9) && (potential >= 4 && potential <= 6)) {
            return "A2";
        }else if ((perfomance > 2.9 && perfomance <= 3.9) && (potential >= 2 && potential <= 3.9)) {
            return "B2";
        }else if ((perfomance >= 2.9 && perfomance <= 3.9) && (potential >= 1 && potential <= 1.9)) {
            return "C2";
        }else if ((perfomance >= 1 && perfomance < 2.9) && (potential >= 4 && potential <= 6)) {
            return "A3";
        }
        else if ((perfomance >= 1 && perfomance < 2.9) && (potential >= 2 && potential <= 3.9)) {
            return "B3";
        }
        else if ((perfomance >= 1 && perfomance < 2.9) && (potential >= 1 && potential <= 1.9)) {
            return "C3";
        }else {
            return "Low";
        }
    }

}
