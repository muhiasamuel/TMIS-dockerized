package com.example.talent_man.utils;

import com.example.talent_man.utils.ApiResponse;
import com.example.talent_man.utils.custom_exceptions.InvalidOtpException;
import com.example.talent_man.utils.custom_exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handle generic runtime exceptions
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<String>> handleRuntimeException(RuntimeException ex) {
        ApiResponse<String> response = new ApiResponse<>();
        response.setMessage(ex.getMessage());
        response.setStatus(HttpStatus.FORBIDDEN.value()); // 403 Forbidden
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    // Handle UsernameNotFoundException (typically for security contexts)
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleUsernameNotFound(UsernameNotFoundException ex) {
        ApiResponse<String> response = new ApiResponse<>();
        response.setMessage(ex.getMessage());
        response.setStatus(HttpStatus.NOT_FOUND.value()); // 404 Not Found
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // Handle custom InvalidOtpException
    @ExceptionHandler(InvalidOtpException.class)
    public ResponseEntity<ApiResponse<String>> handleInvalidOtp(InvalidOtpException ex) {
        ApiResponse<String> response = new ApiResponse<>();
        response.setMessage(ex.getMessage());
        response.setStatus(HttpStatus.BAD_REQUEST.value()); // 400 Bad Request
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // Handle IllegalArgumentException (used for bad input arguments)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<String>> handleIllegalArgument(IllegalArgumentException ex) {
        ApiResponse<String> response = new ApiResponse<>();
        response.setMessage(ex.getMessage());
        response.setStatus(HttpStatus.BAD_REQUEST.value()); // 400 Bad Request
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // Handle custom ResourceNotFoundException
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleResourceNotFound(ResourceNotFoundException ex) {
        ApiResponse<String> response = new ApiResponse<>();
        response.setMessage(ex.getMessage());
        response.setStatus(HttpStatus.NOT_FOUND.value()); // 404 Not Found
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // Catch-all for other uncaught exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleGenericException(Exception ex) {
        ApiResponse<String> response = new ApiResponse<>();
        response.setMessage("An unexpected error occurred: " + ex.getMessage());
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value()); // 500 Internal Server Error
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
