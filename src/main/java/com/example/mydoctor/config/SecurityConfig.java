package com.example.mydoctor.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.mydoctor.repository.PatientRepository;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class SecurityConfig {

    private PatientRepository patientRepository;

    @Autowired
    public SecurityConfig(PatientRepository thePatientRepository) {
        this.patientRepository = thePatientRepository;
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/signup", "/login", "/send-otp").permitAll()
                .requestMatchers("/patients/**").hasRole("PATIENT")
                .anyRequest().authenticated()
            )
            .httpBasic(Customizer.withDefaults())
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .exceptionHandling(e -> e
                .authenticationEntryPoint((_ ,response, _) -> {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json");
                    response.getWriter().write("{\"error\": \"Unauthorized\", \"message\": \"You must login first\"}");

                })
            )
            .userDetailsService(userDetailsService())
            
            .logout(logout -> logout.permitAll());

        return http.build();

    }

    @Bean
    public UserDetailsService userDetailsService() {

        return username -> patientRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Patient not found with username: " + username));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        
        return new BCryptPasswordEncoder();
    }
}
