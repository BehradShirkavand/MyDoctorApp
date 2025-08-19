package com.example.mydoctor.service;

import com.example.mydoctor.dto.UserDTO;
import com.example.mydoctor.exception.ApiException;
import com.example.mydoctor.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.time.Instant;


@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final PatientService patientService;
    private final OtpService otpService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public String registerAndLogin(UserDTO theUserDTO) {

        boolean existingByPhone = patientService.existsPatientByPhoneNumber(theUserDTO.getPhoneNumber());
        boolean existingByUsername = patientService.existsPatientByUsername(theUserDTO.getUsername());

        if (existingByUsername || existingByPhone) {

            try {
                authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                        theUserDTO.getUsername(),
                        theUserDTO.getPassword()
                    )
                );

            } catch (AuthenticationException e) {
                throw new ApiException("Invalid Username or Password", HttpStatus.UNAUTHORIZED);
            } 
        } else {
            patientService.addPatient(theUserDTO);
        }

        try {
            return otpService.generateOtp(theUserDTO);

        } catch (IllegalStateException ex) {
            throw new ApiException(ex.getMessage(), HttpStatus.TOO_MANY_REQUESTS);
        }
    }
}
