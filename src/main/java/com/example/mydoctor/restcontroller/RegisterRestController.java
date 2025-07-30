package com.example.mydoctor.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.mydoctor.dto.PatientDTO;
import com.example.mydoctor.service.PatientService;

@RestController
public class RegisterRestController {

    private PatientService patientService;

    @Autowired
    public RegisterRestController(PatientService thePatientService) {

        this.patientService = thePatientService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> register(@RequestBody PatientDTO thePatientDTO) {

        if (patientService.getPatientByUsername(thePatientDTO.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username is already taken");
        }

        PatientDTO saved = patientService.createPatient(thePatientDTO);
        return ResponseEntity.ok(saved);
    }
}
