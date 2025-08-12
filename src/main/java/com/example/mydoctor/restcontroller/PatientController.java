package com.example.mydoctor.restcontroller;

import java.util.List;
import java.util.Map;

import com.example.mydoctor.dto.UserDTO;
import com.example.mydoctor.payload.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.mydoctor.service.PatientService;

@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserDTO>>> getAll() {

        List<UserDTO> users = patientService.getAllPatients();
        ApiResponse<List<UserDTO>> response = new ApiResponse<List<UserDTO>>(true, "List of patients", users);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> getById(@PathVariable int id) {

        UserDTO user = patientService.getPatientById(id);
        ApiResponse<UserDTO> response = new ApiResponse<UserDTO>(true, "Patient", user);
        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<ApiResponse<UserDTO>> updatePatient(@Valid @RequestBody UserDTO theUserDTO) {

        UserDTO updated = patientService.updatePatient(theUserDTO);
        ApiResponse<UserDTO> response = new ApiResponse<>(true, "Patient updated successfully", updated);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> patchPatient(
            @PathVariable int id,
            @RequestBody Map<String, Object> patchPayload
    ) {
        UserDTO updatedDto = patientService.patchPatient(id, patchPayload);
        ApiResponse<UserDTO> response = new ApiResponse<>(true, "Patient patched successfully", updatedDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDeletePatient(@PathVariable int id) {
        
        patientService.softDeletePatient(id);

        return ResponseEntity.noContent().build();
    }
}
