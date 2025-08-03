package com.example.mydoctor.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import com.example.mydoctor.dto.PatientDTO;
import com.example.mydoctor.entity.Patient;


@Service
public class RegisterationService {

    private PatientService patientService;

    private OtpService otpService;

    private AuthenticationManager authenticationManager;

    @Autowired
    public RegisterationService(PatientService thePatientService, OtpService theOtpService, AuthenticationManager theAuthenticationManager) {
        this.patientService = thePatientService;
        this.otpService = theOtpService;
        this.authenticationManager = theAuthenticationManager;
    }
    
    public ResponseEntity<?> sendOtp(String phoneNumber) {
        
        try {
            otpService.generateOtp(phoneNumber);
            return ResponseEntity.ok("The Otp code was sent");
            
        } catch (IllegalStateException ex) {
            return ResponseEntity
            .status(HttpStatus.TOO_MANY_REQUESTS)
            .body(ex.getMessage());
        }
    }
    
    public ResponseEntity<?> RegisterAndLogin(PatientDTO thePatientDTO) {

        if (patientService.existsByPhoneNumber(thePatientDTO.getPhoneNumber())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Number is already taken");
        }


        Optional<Patient> patient = patientService.getPatientByUsername(thePatientDTO.getUsername());
        
        if (patient.isPresent()) {

            try {
                authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                        thePatientDTO.getUsername(), 
                        thePatientDTO.getPassword()
                    )
                );

            } catch (AuthenticationException e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
            } 
        } else {

            patientService.createPatient(thePatientDTO);
        }

        sendOtp(thePatientDTO.getPhoneNumber());

        return ResponseEntity.ok("success");
        
        
    }
    
    public ResponseEntity<?> verify(String phoneNumber, String otp) {
        
        if (!otpService.verifyOtp(phoneNumber, otp)) {
        
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired OTP");
        
        } else {

            return ResponseEntity.ok("Authenticated succesfully");
        }
    }
}
