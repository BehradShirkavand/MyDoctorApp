package com.example.mydoctor.exception;

import com.example.mydoctor.payload.ApiResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class ApiException extends RuntimeException{

    private final ApiResponse<?> response;
    private final HttpStatus status;

    public ApiException(String message, HttpStatus status) {
        this.response = new ApiResponse<>(false, message, null);
        this.status = status;
    }
}
