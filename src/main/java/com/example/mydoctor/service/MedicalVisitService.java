package com.example.mydoctor.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.mydoctor.dto.MedicalVisitDTO;
import com.example.mydoctor.dto.MedicalVisitMapper;
import com.example.mydoctor.entity.MedicalVisit;
import com.example.mydoctor.enums.Status;
import com.example.mydoctor.repository.MedicalVisitRepository;

@Service
public class MedicalVisitService {

    private MedicalVisitRepository medicalVisitRepository;

    private MedicalVisitMapper medicalVisitMapper;

    

    @Autowired
    public MedicalVisitService (MedicalVisitRepository theMedicalVisitRepository, MedicalVisitMapper theMedicalVisitMapper) {
        this.medicalVisitRepository = theMedicalVisitRepository;
        this.medicalVisitMapper = theMedicalVisitMapper;

    }

    public List<MedicalVisitDTO> getAllActiveMedicalVisits() {
        
        return medicalVisitMapper.toDtoList(medicalVisitRepository.findAllByStatus(Status.ACTIVE));
    }

    public MedicalVisitDTO getMedicalVisitById(int theId) {

        MedicalVisit theMedicalVisit = medicalVisitRepository.findById(theId).
        orElseThrow(() -> new RuntimeException("MedicalVisit not found with id: " + theId));

        return medicalVisitMapper.toDto(theMedicalVisit);
    }

    public MedicalVisit addMedicalVisit(MedicalVisit theMedicalVisit) {

        return medicalVisitRepository.save(theMedicalVisit);
    }

    public MedicalVisit updateMedicalVisit(MedicalVisit theMedicalVisit) {

        int id = theMedicalVisit.getId();

        MedicalVisit result = medicalVisitRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("MedicalVisit not found with id: " + id));

        result.setDescription(theMedicalVisit.getDescription());
        result.setDiseaseType(theMedicalVisit.getDiseaseType());

    
        return medicalVisitRepository.save(result);
    }


    public void softDeleteMedicalVisit(int theId) {
        
        MedicalVisit medicalVisit = medicalVisitRepository.findById(theId)
            .orElseThrow(() -> new RuntimeException("Medical visit not found"));

        medicalVisit.setStatus(Status.DELETED);

        medicalVisitRepository.save(medicalVisit);
    }

}
