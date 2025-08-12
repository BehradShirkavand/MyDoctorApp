package com.example.mydoctor.restcontroller;

import java.util.List;

import com.example.mydoctor.payload.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mydoctor.dto.MedicalVisitDTO;
import com.example.mydoctor.service.MedicalVisitService;

@RestController
@RequestMapping("/visits")
@RequiredArgsConstructor
public class MedicalVisitController {

    private final MedicalVisitService medicalVisitService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<MedicalVisitDTO>>> getAll() {

        List<MedicalVisitDTO> visits = medicalVisitService.getAllActiveMedicalVisits();
        ApiResponse<List<MedicalVisitDTO>> response = new ApiResponse<>(true, "List of visits", visits);
        return ResponseEntity.ok(response);

    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MedicalVisitDTO>> getById(@PathVariable int id) {

        MedicalVisitDTO visit = medicalVisitService.getMedicalVisitById(id);
        ApiResponse<MedicalVisitDTO> response = new ApiResponse<>(true, "Medical visit", visit);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<MedicalVisitDTO>> addMedicalVisit(@Valid @RequestBody MedicalVisitDTO theMedicalVisitDTO) {

        MedicalVisitDTO visit = medicalVisitService.addMedicalVisit(theMedicalVisitDTO);
        ApiResponse<MedicalVisitDTO> response = new ApiResponse<>(true, "Visit added successfully", visit);
        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<ApiResponse<MedicalVisitDTO>> updateMedicalVisit(@Valid @RequestBody MedicalVisitDTO theMedicalVisitDTO) {

        MedicalVisitDTO visit = medicalVisitService.updateMedicalVisit(theMedicalVisitDTO);
        ApiResponse<MedicalVisitDTO> response = new ApiResponse<>(true, "Updated successfully", visit);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDeleteVisit(@PathVariable int id) {

        medicalVisitService.softDeleteMedicalVisit(id);
        return ResponseEntity.noContent().build();
    }
}
