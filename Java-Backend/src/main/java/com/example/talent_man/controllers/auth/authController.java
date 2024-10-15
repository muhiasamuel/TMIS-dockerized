package com.example.talent_man.controllers.auth;

import com.example.talent_man.dto.user.AuthRequest;
import com.example.talent_man.dto.user.AuthResponse;
import com.example.talent_man.dto.user.OtpRequest;
import com.example.talent_man.repos.user.UserRepo;
import com.example.talent_man.services.UserService;
import com.example.talent_man.utils.custom_exceptions.InvalidOtpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.example.talent_man.models.LoginRequest;
import com.example.talent_man.models.OTP.OTP;
import com.example.talent_man.models.OTP.OTPCode;
import com.example.talent_man.models.OTP.OTPRepository;
import com.example.talent_man.models.Responses.MessageResponse;
import com.example.talent_man.models.user.Employee;
import com.example.talent_man.models.user.Manager;
import com.example.talent_man.models.user.User;
import com.example.talent_man.service_imp.UserServiceImp;
import com.example.talent_man.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController()
@RequestMapping("v1/api/auth")
public class authController {

    @Autowired
    private UserServiceImp service;


    @Autowired
    UserRepo userRepo;
    @Value("${spring.security.user.name}")
    private String securityUserName;

    @GetMapping("/security")
    public ResponseEntity<String> getSecurityConfig() {
        return ResponseEntity.ok("Security User Name: " + securityUserName);
    }

//    @PostMapping("/authenticate")
//    public AuthResponse createAuthenticationToken(@RequestBody AuthRequest authRequest) throws Exception {
//        return service.authenticate(authRequest);
//    }

    @PostMapping("/authenticate")
    public AuthResponse createAuthenticationToken(@RequestBody AuthRequest authRequest) throws Exception {
        User user = userRepo.findByUsername(authRequest.getUsername());

        // Check if user exists
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        // Check if the user is enabled and not locked
        if (!user.isEnabled()) {
            throw new RuntimeException("User account is disabled");
        }

        if (!user.isAccountNonLocked()) {
            throw new RuntimeException("User account is locked");
        }

        return service.authenticate(authRequest);
    }

//    @PostMapping("/validateOtp")
//    public AuthResponse validateOtp(@RequestBody OtpRequest otpRequest) {
//        return service.validateOtp(otpRequest);
//    }

    @PostMapping("/validateOtp")
    public ResponseEntity<AuthResponse> validateOtp(@RequestBody OtpRequest otpRequest) {
        AuthResponse response = service.validateOtp(otpRequest);

        // Check if the response indicates an error
        if (response.getStatus() != 200) {
            // If status is not OK (200), throw a custom exception
            throw new InvalidOtpException(response.getMessage()); // Throw custom exception
        }

        return ResponseEntity.ok(response); // Return valid response with HTTP 200 OK
    }


    @PostMapping("/login")
    public ApiResponse<User> login(@RequestBody LoginRequest req){
        try{
            if(req == null || req.getUsername() == null || req.getUsername().equals("")){
                return new ApiResponse<>(101, "username required");
            }else if(req.getPassword() == null || req.getPassword().equals("")){
                return new ApiResponse<>(101, "password required");
            }else{

                if(service.getManagerByUserName(req.getUsername()) != null ){
                    Manager man = service.getManagerByUserName(req.getUsername());
                    String password = man.getPassword();
                    if (!password.equals(req.getPassword())){
                        return new ApiResponse<>(301, "Invalid credentials");
                    }else{
                        ApiResponse<User> res = new ApiResponse<>(200, "successful");
                        res.setItem(man);
                        return res;
                    }
                }else if(service.getEmployeeByUserName(req.getUsername()) != null){
                    System.out.println("samtedfdfgfgfxvvghgchx");
                    Employee emp = service.getEmployeeByUserName(req.getUsername());
                    String password = emp.getPassword();
                    System.out.println("samtedfdfgfgfxvvghgchx" + password);
                    if (!password.equals(req.getPassword())){
                        return new ApiResponse<>(301, "Invalid credentials");
                    }else{
                        ApiResponse<User> res = new ApiResponse<>(200, "successful");
                        res.setItem(emp);
                        System.out.println("samtedfdfgfgfxvvghgchx"+ emp);
                        System.out.println(emp);
                        return res;
                    }
                }else {
                    return new ApiResponse<>(300, "User doesn't exist");

                }
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
            return new ApiResponse<>(500, e.getMessage());
        }
    }
    OTPRepository otpRepository;
    @PostMapping(path = "/verifyOTP")
    public ResponseEntity<?> validateOTP(@RequestBody OTPCode otpCode) {
        OTP otp = otpRepository.validOTP(otpCode.username);
        if (Objects.isNull(otp) || !Objects.equals(otp.getOtp(), otpCode.otp)) {
            return ResponseEntity.badRequest().body(new MessageResponse("OTP is not valid!"));
        } else {
            return ResponseEntity.ok(new MessageResponse("Welcome, OTP valid!"));
        }
    }
}