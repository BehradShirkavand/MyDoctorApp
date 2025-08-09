package com.example.mydoctor.restcontroller;

import java.util.List;

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
import com.example.mydoctor.entity.MedicalVisit;
import com.example.mydoctor.service.MedicalVisitService;

@RestController
@RequestMapping("/medical_visits")
@RequiredArgsConstructor
public class MedicalVisitController {

    private final MedicalVisitService medicalVisitService;

    @GetMapping
    public List<MedicalVisitDTO> getAll() {

        return medicalVisitService.getAllActiveMedicalVisits();
    }

    @GetMapping("/{id}")
    public MedicalVisitDTO getById(@PathVariable int id) {

        return medicalVisitService.getMedicalVisitById(id);
    }

    @PostMapping("/add")
    public MedicalVisit addMedicalVisit(@RequestBody MedicalVisit theMedicalVisit) {

        return medicalVisitService.addMedicalVisit(theMedicalVisit);
    }

    @PutMapping("/update")
    public MedicalVisit updateMedicalVisit(@RequestBody MedicalVisit theMedicalVisit) {

        return medicalVisitService.updateMedicalVisit(theMedicalVisit);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> softDeleteVisit(@PathVariable int id) {
        
        medicalVisitService.softDeleteMedicalVisit(id);
        return ResponseEntity.noContent().build();
    }
}
