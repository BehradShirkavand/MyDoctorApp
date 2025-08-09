package com.example.mydoctor.restcontroller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mydoctor.dto.OtpVerifyRequest;
import com.example.mydoctor.dto.PatientDTO;
import com.example.mydoctor.service.RegisterationService;

import jakarta.validation.Valid;


@Validated
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class RegisterationController {

    private final RegisterationService registerationService;

    @PostMapping("/signup")
    public ResponseEntity<?> RegisterAndLogin(@Valid @RequestBody PatientDTO thePatientDTO) {

        return registerationService.registerAndLogin(thePatientDTO);
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verify(@Valid @RequestBody OtpVerifyRequest request) {

        return registerationService.verify(request.getPhoneNumber(), request.getOtp());    
    }
}
