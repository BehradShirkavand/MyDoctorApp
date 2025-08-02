package com.example.mydoctor.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.mydoctor.dto.PatientDTO;
import com.example.mydoctor.service.OtpService;
import com.example.mydoctor.service.PatientService;

import jakarta.validation.constraints.Pattern;


@Validated
@RestController
public class RegisterController {

    private PatientService patientService;

    private OtpService otpService;

    @Autowired
    public RegisterController(PatientService thePatientService, OtpService theOtpService) {

        this.patientService = thePatientService;
        this.otpService = theOtpService;
    }

    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(
        @RequestParam
        @Pattern(regexp = "^(\\+98|0)?9\\d{9}$", message = "Phone number must be a valid mobile number")
        String phoneNumber) {

        if (patientService.existsByPhoneNumber(phoneNumber)) {
            return ResponseEntity.badRequest().body("This number is already taken");
        }

        try {
            otpService.generateOtp(phoneNumber);
            return ResponseEntity.ok("The Otp code was sent");

        } catch (IllegalStateException ex) {
            return ResponseEntity
                .status(HttpStatus.TOO_MANY_REQUESTS)
                .body(ex.getMessage());
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> verifyAndRegister(@RequestBody PatientDTO thePatientDTO) {
        
        if (patientService.getPatientByUsername(thePatientDTO.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username is already taken");
        }

        if (patientService.existsByPhoneNumber(thePatientDTO.getPhoneNumber())) {
            return ResponseEntity.badRequest().body("Number is already taken");
        }

        if (!otpService.verifyOtp(thePatientDTO.getPhoneNumber(), thePatientDTO.getOtp())) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired OTP or phone number");
        }


        PatientDTO saved = patientService.createPatient(thePatientDTO);
        return ResponseEntity.ok(saved);
    }
}
