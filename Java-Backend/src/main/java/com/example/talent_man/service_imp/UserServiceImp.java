package com.example.talent_man.service_imp;
import org.apache.poi.ss.usermodel.*;
import com.example.talent_man.dto.user.AuthRequest;
import com.example.talent_man.dto.user.UserRequestDto;
import com.example.talent_man.models.Position;
import com.example.talent_man.models.Role;
import com.example.talent_man.models.user.Employee;
import com.example.talent_man.models.user.Manager;
import com.example.talent_man.models.user.User;
import com.example.talent_man.repos.PositionRepo;
import com.example.talent_man.repos.RoleRepository;
import com.example.talent_man.repos.user.EmployeeRepo;
import com.example.talent_man.repos.user.ManagerRepo;
import com.example.talent_man.repos.user.UserRepo;
import com.example.talent_man.services.*;
import com.example.talent_man.utils.ApiResponse;
import com.example.talent_man.dto.user.AuthResponse;
import com.example.talent_man.dto.user.OtpRequest;
import com.example.talent_man.services.UserService;
import com.example.talent_man.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
//import com.example.talent_man.utils.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


@Service
public class UserServiceImp implements UserService {
    @Autowired
    private UserRepo repo;
    @Autowired
    private ManagerRepo managerRepo;
    @Autowired
    private EmployeeRepo employeeRepo;

    @Autowired
    RoleRepository roleRepo;

    @Autowired
    PasswordEncoder passwordEncoder;
     @Autowired
     EmailService emailService;

     @Autowired
    PositionRepo positionRepo;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailService customUserDetailsService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private OtpService otpService;

    @Override
    public User addUser(User user) {
        return repo.save(user);
    }

    public Manager addManger(Manager manager) {
        return repo.save(manager);
    }

    @Override
    public User getByUsername(String username) {
        return repo.findByUsername(username);
    }

    @Override
    public Manager getManagerById(int id) {
        return managerRepo.getReferenceById(id);
    }

    @Override
    public Manager getManagerByUserName(String username) {
        return managerRepo.findByUsername(username);
    }

    @Override
    public Employee getEmployeeByUserName(String username) {
        return employeeRepo.findByUsername(username);
    }

    @Override
    public List<Manager> findAllManagers() {
        return managerRepo.findByAllUserType();
    }

    @Override
    public List<Employee> findAllEmployees() {

        return employeeRepo.findByAllEmployees();
    }




    @Override
    public User getUserById(int id) {
        return repo.getReferenceById(id);
    }

    @Override
    public List<User> getManagerEmployees(Manager manId) {
        return employeeRepo.findByManager(manId);
    }

    @Override
    public List<Integer> getManEmployees(int manId) {
        return managerRepo.getManagerEmployees(manId);
    }

    @Override
    public  UserRepo.UserWithManagerDetails getUserPerformancesByManagerId(int employeeId){
        return  repo.getUserWithManagerDetails(employeeId);
    }


    //create a user and assign them roles


    @Override
    public AuthResponse authenticate(AuthRequest authRequest) throws Exception {
        String username = authRequest.getUsername();
        String password = authRequest.getPassword();

        // Retrieve the user from the database based on the username
        User userToMail = repo.findByUsername(username);
        if (userToMail != null) {
            // Check if the provided password matches the one stored in the database
            if (!passwordEncoder.matches(password, userToMail.getPassword())) {
                // Passwords do not match, return an error response
                return new AuthResponse("Invalid username or password", 401);
            }

            // Passwords match, proceed with generating and sending OTP
            otpService.generateAndSendOtp(userToMail.getEmail());
            return new AuthResponse("OTP sent to registered email", 200);
        } else {
            // User not found, return an error response
            return new AuthResponse( "User not found", 404);
        }
    }


    @Override
    public AuthResponse validateOtp(OtpRequest otpRequest) {
        String user = otpRequest.getUsername();
        User userToMail = repo.findByUsername(user);
        if (userToMail != null) {
            if (otpService.validateOtp(userToMail.getEmail(), otpRequest.getOtp())) {
                // OTP is valid, proceed with authentication
                String username = otpRequest.getUsername();
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
                String jwtToken = jwtUtil.generateToken(userToMail);
                AuthResponse authResponse = new AuthResponse();
                authResponse.setUser(userToMail);
                authResponse.setAuthToken(jwtToken);
                authResponse.setPermissions(userToMail.getRole().getPermissions());
                return authResponse;
            } else {
                return new AuthResponse( "Invalid OTP", 401);
            }
        }else {
            return new AuthResponse( "User not found", 404);
        }


    }

    //create Employee


    @Override
    public User createTopManager(UserRequestDto userDto) {
        // Generate a random password
        String rawPassword = generateRandomPassword();
        String encodedPassword = passwordEncoder.encode(rawPassword);

        // Set manager properties
        Manager manager = new Manager();
        manager.setUserType("Manager");
        manager.setUserFullName(userDto.getUserFullName());
        manager.setEmail(userDto.getEmail());
        manager.setPf(userDto.getPf());
        manager.setUsername(userDto.getUsername());
        manager.setPassword(encodedPassword);
        Role role = roleRepo.findById(userDto.getRoleId())
                .orElseThrow(() -> new RuntimeException("Role Not Found: " + userDto.getRoleId()));
        manager.setRole(role);
        Position position = positionRepo.findById(userDto.getPositionId())
                .orElseThrow(() -> new RuntimeException("Position Not Found: " + userDto.getRoleId()));
        manager.setPosition(position);
        manager.setRole(role);
        manager.setLocked(userDto.getLocked() != null ? userDto.getLocked() : false);
        manager.setEnabled(userDto.getEnabled() != null ? userDto.getEnabled() : false);



        // Save the employee
        User savedEmployee = repo.save(manager);

        // Send the plain text password to the employee's email
        emailService.sendEmail(manager.getEmail(), "Your new account password", "Your password is: " + rawPassword);

        return savedEmployee;
    }

    @Override
    public User createManager(UserRequestDto userDto, int managerId) {
        // Generate a random password
        String rawPassword = generateRandomPassword();
        String encodedPassword = passwordEncoder.encode(rawPassword);

        // Set manager properties
        Manager manager = new Manager();
        manager.setUserType("Manager");
        manager.setUserFullName(userDto.getUserFullName());
        manager.setEmail(userDto.getEmail());
        manager.setPf(userDto.getPf());
        manager.setUsername(userDto.getUsername());
        manager.setPassword(encodedPassword);
        Role role = roleRepo.findById(userDto.getRoleId())
                .orElseThrow(() -> new RuntimeException("Role Not Found: " + userDto.getRoleId()));
        manager.setRole(role);
        Position position = positionRepo.findById(userDto.getPositionId())
                .orElseThrow(() -> new RuntimeException("Position Not Found: " + userDto.getRoleId()));
        manager.setRole(role);

        manager.setPosition(position);
        manager.setLocked(userDto.getLocked() != null ? userDto.getLocked() : false);
        manager.setEnabled(userDto.getEnabled() != null ? userDto.getEnabled() : false);
        Manager man = managerRepo.findById(managerId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + managerId));
        manager.setManager(man);


        // Save the employee
        User savedEmployee = repo.save(manager);

        // Send the plain text password to the employee's email
        emailService.sendEmail(manager.getEmail(), "Your new account password", "Your password is: " + rawPassword);

        return savedEmployee;
    }

    @Override
    public User createEmployee(UserRequestDto userDto, int managerId) {
        // Generate a random password
        String rawPassword = generateRandomPassword();
        String encodedPassword = passwordEncoder.encode(rawPassword);

        // Set user properties
        Employee employee = new Employee();
        employee.setUserType("Employee");
        employee.setUserFullName(userDto.getUserFullName());
        employee.setEmail(userDto.getEmail());
        User user = repo.findFirstByEmail(userDto.getEmail());
        if(user != null){
            
        }
        employee.setUsername(userDto.getUsername());
        employee.setPassword(encodedPassword);
        employee.setPf(userDto.getPf());
        Role role = roleRepo.findById(userDto.getRoleId())
                .orElseThrow(() -> new RuntimeException("Role Not Found: " + userDto.getRoleId()));
        employee.setRole(role);
        Position position = positionRepo.findById(userDto.getPositionId())
                .orElseThrow(() -> new RuntimeException("Position Not Found: " + userDto.getPositionId()));
        employee.setPosition(position);
        employee.setRole(role);
        employee.setLocked(userDto.getLocked() != null ? userDto.getLocked() : false);
        employee.setEnabled(userDto.getEnabled() != null ? userDto.getEnabled() : false);
        Manager manager = managerRepo.findById(managerId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + managerId));
        employee.setManager(manager);


        // Save the employee
        User savedEmployee = repo.save(employee);

        // Send the plain text password to the employee's email
        emailService.sendEmail(employee.getEmail(), "Your new account password", "Your password is: " + rawPassword);

        return savedEmployee;
    }
    @Override
    public List<User> createEmployees(List<UserRequestDto> userDtos, int managerId) {
        List<User> createdEmployees = new ArrayList<>();
        List<String> emails = new ArrayList<>();
        List<String> passwords = new ArrayList<>();

        Manager manager = managerRepo.findById(managerId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + managerId));

        for (UserRequestDto userDto : userDtos) {
            User createdEmployee = createEmployee(userDto, managerId);
            createdEmployees.add(createdEmployee);
            emails.add(createdEmployee.getEmail());
            passwords.add(createdEmployee.getPassword());
        }

        sendEmails(emails, passwords);

        return createdEmployees;
    }

    @Override
    public List<User> uploadEmployees(MultipartFile file, int managerId) {
        List<UserRequestDto> userDtos = parseExcelFile(file);
        return createEmployees(userDtos, managerId);
    }

    private List<UserRequestDto> parseExcelFile(MultipartFile file) {
        List<UserRequestDto> userDtos = new ArrayList<>();
        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue; // Skip header row
                }
                UserRequestDto userDto = new UserRequestDto();

                // Example parsing for numeric and string cells
                userDto.setUserFullName(row.getCell(0).getStringCellValue());
                userDto.setEmail(row.getCell(1).getStringCellValue());
                userDto.setUsername(row.getCell(2).getStringCellValue());
                userDto.setPf(String.valueOf(row.getCell(3).getNumericCellValue()));

                // Handle numeric cell (Role ID)
                Cell roleCell = row.getCell(4);
                if (roleCell.getCellType() == CellType.NUMERIC) {
                    userDto.setRoleId((long) roleCell.getNumericCellValue());
                } else {
                    throw new IllegalArgumentException("Role ID must be numeric");
                }

                // Handle numeric cell (Position ID)
                Cell positionCell = row.getCell(5);
                if (positionCell.getCellType() == CellType.NUMERIC) {
                    userDto.setPositionId((int) positionCell.getNumericCellValue());
                } else {
                    throw new IllegalArgumentException("Position ID must be numeric");
                }

                // Handle boolean cell (Locked)
                userDto.setLocked(row.getCell(6).getBooleanCellValue());

                // Handle boolean cell (Enabled)
                userDto.setEnabled(row.getCell(7).getBooleanCellValue());

                userDtos.add(userDto);
            }
        } catch (IOException  e) {
            throw new RuntimeException("Failed to parse Excel file", e);
        }
        return userDtos;
    }

    private void sendEmails(List<String> emails, List<String> passwords) {
        for (int i = 0; i < emails.size(); i++) {
            emailService.sendEmail(emails.get(i), "Your new account password", "Your password is: " + passwords.get(i));
        }
    }


    private String generateRandomPassword() {
        int length = 6; // Adjust the length of the password as needed
        String upperCaseChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCaseChars = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        String specialChars = "!@#$%^&*()-_=+[{]};:<>|./?";

        String allChars = upperCaseChars + lowerCaseChars + numbers + specialChars;

        Random random = new SecureRandom();
        StringBuilder password = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            password.append(allChars.charAt(random.nextInt(allChars.length())));
        }

        return password.toString();
    }

}


