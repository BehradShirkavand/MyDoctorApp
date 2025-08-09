package com.example.mydoctor.restcontroller;

import java.util.List;
import java.util.Map;

import com.example.mydoctor.payload.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.mydoctor.dto.PatientDTO;

import com.example.mydoctor.service.PatientService;

@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<PatientDTO>>> getAll() {

        List<PatientDTO> patients = patientService.getAllActivePatients();
        ApiResponse<List<PatientDTO>> response = new ApiResponse<List<PatientDTO>>(true, "List of patients", patients);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PatientDTO>> getById(@PathVariable int id) {

        PatientDTO patient = patientService.getPatientById(id);
        ApiResponse<PatientDTO> response = new ApiResponse<PatientDTO>(true, "Patient", patient);
        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<ApiResponse<PatientDTO>> updatePatient(@Valid @RequestBody PatientDTO thePatientDTO) {

        PatientDTO updated = patientService.updatePatient(thePatientDTO);
        ApiResponse<PatientDTO> response = new ApiResponse<>(true, "Patient updated successfully", updated);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<PatientDTO>> patchPatient(
            @PathVariable int id,
            @RequestBody Map<String, Object> patchPayload
    ) {
        PatientDTO updatedDto = patientService.patchPatient(id, patchPayload);
        ApiResponse<PatientDTO> response = new ApiResponse<>(true, "Patient patched successfully", updatedDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDeletePatient(@PathVariable int id) {
        
        patientService.softDeletePatient(id);

        return ResponseEntity.noContent().build();
    }
}
