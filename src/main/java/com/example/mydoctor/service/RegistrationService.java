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
    
    public void registerAndLogin(UserDTO theUserDTO) {

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

        sendOtp(theUserDTO.getPhoneNumber());
    }
    
    public String verifyOtp(String phoneNumber, String otp) {
        
        if (!otpService.verifyOtp(phoneNumber, otp)) {
        
            throw new ApiException("Invalid or expired OTP", HttpStatus.UNAUTHORIZED);
        }

        UserDTO user = patientService.getPatientByPhoneNumber(phoneNumber);
        return jwtUtil.generateToken(user.getUsername());
    }
}
