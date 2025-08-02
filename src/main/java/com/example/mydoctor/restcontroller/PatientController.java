package com.example.mydoctor.restcontroller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.mydoctor.dto.PatientDTO;

import com.example.mydoctor.service.PatientService;

@RestController
@RequestMapping("/patients")
public class PatientController {

    private PatientService patientService;

    @Autowired
    public PatientController(PatientService thePatientService) {

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
