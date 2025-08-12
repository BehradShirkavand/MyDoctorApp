package com.example.mydoctor.config;

import com.example.mydoctor.entity.Patient;
import com.example.mydoctor.exception.ApiException;
import com.example.mydoctor.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final PatientRepository patientRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Patient patient = patientRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException(("User not found"), HttpStatus.NOT_FOUND));

        return new CustomUserDetails(patient);
    }
}
