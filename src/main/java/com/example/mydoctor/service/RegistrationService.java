package com.example.mydoctor.service;

import java.util.Optional;

import com.example.mydoctor.exception.ApiException;
import com.example.mydoctor.payload.ApiResponse;
import com.example.mydoctor.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import com.example.mydoctor.dto.PatientDTO;
import com.example.mydoctor.entity.Patient;


@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final PatientService patientService;
    private final OtpService otpService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public void sendOtp(String phoneNumber) {
        
        try {
            otpService.generateOtp(phoneNumber);

        } catch (IllegalStateException ex) {
            throw new ApiException(ex.getMessage(), HttpStatus.TOO_MANY_REQUESTS);
        }
    }
    
    public void registerAndLogin(PatientDTO thePatientDTO) {

        PatientDTO existingByPhone = patientService.getPatientByPhoneNumber(thePatientDTO.getPhoneNumber());
        PatientDTO existingByUsername = patientService.getPatientByUsername(thePatientDTO.getUsername());

        if (existingByUsername != null || existingByPhone != null) {

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
    }
    
    public String verify(String phoneNumber, String otp) {
        
        if (!otpService.verifyOtp(phoneNumber, otp)) {
        
            throw new ApiException("Invalid or expired OTP", HttpStatus.UNAUTHORIZED);
        }

        PatientDTO patient = patientService.getPatientByPhoneNumber(phoneNumber);
        return jwtUtil.generateToken(patient.getUsername());
    }
}
