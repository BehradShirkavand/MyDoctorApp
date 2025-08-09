package com.example.mydoctor.service;

import java.util.Optional;

import com.example.mydoctor.exception.ApiException;
import com.example.mydoctor.payload.ApiResponse;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class RegisterationService {

    private final PatientService patientService;
    private final OtpService otpService;
    private final AuthenticationManager authenticationManager;
    
    public ResponseEntity<ApiResponse<Object>> sendOtp(String phoneNumber) {
        
        try {
            otpService.generateOtp(phoneNumber);
            return ResponseEntity.ok(new ApiResponse<>(true, "The Otp code was sent", null));

        } catch (IllegalStateException ex) {
            return ResponseEntity
            .status(HttpStatus.TOO_MANY_REQUESTS)
            .body(new ApiResponse<>(false, ex.getMessage(), null));
        }
    }
    
    public ResponseEntity<ApiResponse<Object>> registerAndLogin(PatientDTO thePatientDTO) {

        if (patientService.existsByPhoneNumber(thePatientDTO.getPhoneNumber())) {
            throw new ApiException("Number is already taken", HttpStatus.BAD_REQUEST);
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
                throw new ApiException("Invalid Username or Password", HttpStatus.UNAUTHORIZED);
            } 
        } else {

            patientService.createPatient(thePatientDTO);
        }

        sendOtp(thePatientDTO.getPhoneNumber());

        return ResponseEntity.ok(new ApiResponse<>(true, "success", null));

    }
    
    public ResponseEntity<ApiResponse<Object>> verify(String phoneNumber, String otp) {
        
        if (!otpService.verifyOtp(phoneNumber, otp)) {
        
            throw new ApiException("Invalid or expired OTP", HttpStatus.UNAUTHORIZED);
        
        } else {

            return ResponseEntity.ok(new ApiResponse<>(true, "Authenticated successfully", null));
        }
    }
}
