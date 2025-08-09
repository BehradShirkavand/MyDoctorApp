package com.example.mydoctor.exception;

import com.example.mydoctor.payload.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse<?>> handleApiException(ApiException ex) {
        return ResponseEntity.status(ex.getStatus()).body(ex.getResponse());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        ApiResponse<Map<String, String>> response = new ApiResponse<>(
                false,
                "Validation failed",
                errors
        );

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleUnexpectedException(Exception ex) {
        ApiResponse<?> response = new ApiResponse<>(
                false,
                "Unexpected error" + ex.getMessage(),
                null
        );
        return ResponseEntity.status(500).body(response);
    }
}

