package com.example.mydoctor.service;

import java.util.List;

import com.example.mydoctor.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.mydoctor.dto.MedicalVisitDTO;
import com.example.mydoctor.dto.MedicalVisitMapper;
import com.example.mydoctor.entity.MedicalVisit;
import com.example.mydoctor.enums.Status;
import com.example.mydoctor.repository.MedicalVisitRepository;

@Service
@RequiredArgsConstructor
public class MedicalVisitService {

    private MedicalVisitRepository medicalVisitRepository;
    private MedicalVisitMapper medicalVisitMapper;

    public List<MedicalVisitDTO> getAllActiveMedicalVisits() {
        
        return medicalVisitMapper.toDtoList(medicalVisitRepository.findAllByStatus(Status.ACTIVE));
    }

    public MedicalVisitDTO getMedicalVisitById(int theId) {

        MedicalVisit theMedicalVisit = medicalVisitRepository.findById(theId).
        orElseThrow(() -> new ApiException("MedicalVisit not found with id: " + theId, HttpStatus.NOT_FOUND));

        return medicalVisitMapper.toDto(theMedicalVisit);
    }

    public MedicalVisit addMedicalVisit(MedicalVisit theMedicalVisit) {

        return medicalVisitRepository.save(theMedicalVisit);
    }

    public MedicalVisit updateMedicalVisit(MedicalVisit theMedicalVisit) {

        int theId = theMedicalVisit.getId();

        MedicalVisit result = medicalVisitRepository.findById(theId)
        .orElseThrow(() -> new ApiException("MedicalVisit not found with id: " + theId, HttpStatus.NOT_FOUND));

        result.setDescription(theMedicalVisit.getDescription());
        result.setDiseaseType(theMedicalVisit.getDiseaseType());

    
        return medicalVisitRepository.save(result);
    }


    public void softDeleteMedicalVisit(int theId) {
        
        MedicalVisit medicalVisit = medicalVisitRepository.findById(theId)
            .orElseThrow(() -> new ApiException("MedicalVisit not found with id: " + theId, HttpStatus.NOT_FOUND));

        medicalVisit.setStatus(Status.DELETED);

        medicalVisitRepository.save(medicalVisit);
    }

}
