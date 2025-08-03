package com.example.mydoctor.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.mydoctor.dto.OtpVerifyRequest;
import com.example.mydoctor.dto.PatientDTO;
import com.example.mydoctor.service.RegisterationService;

import jakarta.validation.Valid;


@Validated
@RestController
public class RegisterationController {

    private RegisterationService registerationService;

    @Autowired
    public RegisterationController(RegisterationService theRegisterationService) {
        this.registerationService = theRegisterationService;
    }

    // @PostMapping("/send-otp")
    // public ResponseEntity<?> sendOtp( 
    //     @RequestParam
    //     @Pattern(regexp = "^(\\+98|0)?9\\d{9}$", message = "Phone number must be a valid mobile number")
    //     String phoneNumber) {

    //     return registerationService.sendOtp(phoneNumber);
    // }

    @PostMapping("/signup")
    public ResponseEntity<?> RegisterAndLogin(@Valid @RequestBody PatientDTO thePatientDTO) {

        return registerationService.RegisterAndLogin(thePatientDTO);
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verify(@RequestBody OtpVerifyRequest request) {

        return registerationService.verify(request.getPhoneNumber(), request.getOtp());    
    }
}
