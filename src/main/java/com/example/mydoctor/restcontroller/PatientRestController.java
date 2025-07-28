package com.example.mydoctor.restcontroller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mydoctor.dto.PatientDTO;

import com.example.mydoctor.service.PatientService;

@RestController
@RequestMapping("/patients")
public class PatientRestController {

    private PatientService patientService;

    @Autowired
    public PatientRestController(PatientService thePatientService) {

        this.patientService = thePatientService;
    }

    @GetMapping
    public List<PatientDTO> getAll() {
        return patientService.getAllActivePatients();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientDTO> getById(@PathVariable int id) {

        return ResponseEntity.ok(patientService.getPatientById(id));
    }

    @PostMapping
    public ResponseEntity<PatientDTO> createPatient(@RequestBody PatientDTO thePatientDTO) {

        PatientDTO saved = patientService.createPatient(thePatientDTO);
        return ResponseEntity.ok(saved);
    }

    @PutMapping
    public PatientDTO updatePatient(@RequestBody PatientDTO thePatientDTO) {

        return patientService.updatePatient(thePatientDTO);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PatientDTO> patchPatient(
        @PathVariable int id,
        @RequestBody Map<String, Object> patchPayload
        ) {

        PatientDTO updatedDto = patientService.patchPatient(id, patchPayload);

        return ResponseEntity.ok(updatedDto);
}


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDeletePatient(@PathVariable int id) {
        
        patientService.softDeletePatient(id);

        return ResponseEntity.noContent().build();
    }
}
