package com.example.mydoctor.restcontroller;

import com.example.mydoctor.dto.UserDTO;
import com.example.mydoctor.payload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mydoctor.dto.OtpVerifyRequest;
import com.example.mydoctor.service.RegistrationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> RegisterAndLogin(@Valid @RequestBody UserDTO theUserDTO) {

        registrationService.registerAndLogin(theUserDTO);
        return ResponseEntity.ok(new ApiResponse<>(true, "Otp sent successfully", null));
    }

    @PostMapping("/verifyOtp")
    public ResponseEntity<ApiResponse<Object>> verifyOtp(@Valid @RequestBody OtpVerifyRequest request) {

        String token = registrationService.verifyOtp(request.getPhoneNumber(), request.getOtp());
        return ResponseEntity.ok(new ApiResponse<>(true, "Authenticated successfully", "token = " + token));
    }
}