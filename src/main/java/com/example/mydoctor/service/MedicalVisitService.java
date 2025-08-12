package com.example.mydoctor.service;

import java.util.List;

import com.example.mydoctor.exception.ApiException;
import lombok.RequiredArgsConstructor;
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

    private final MedicalVisitRepository medicalVisitRepository;
    private final MedicalVisitMapper medicalVisitMapper;

    public List<MedicalVisitDTO> getAllActiveMedicalVisits() {
        
        return medicalVisitMapper.toDtoList(medicalVisitRepository.findAllByStatus(Status.ACTIVE));
    }

    public MedicalVisitDTO getMedicalVisitById(int theId) {

        MedicalVisit theMedicalVisit = medicalVisitRepository.findById(theId).
        orElseThrow(() -> new ApiException("MedicalVisit not found with id: " + theId, HttpStatus.NOT_FOUND));

        return medicalVisitMapper.toDto(theMedicalVisit);
    }

    public MedicalVisitDTO addMedicalVisit(MedicalVisitDTO theMedicalVisitDTO) {

        return medicalVisitMapper.toDto(medicalVisitRepository.save(medicalVisitMapper.toEntity(theMedicalVisitDTO)));
    }

    public MedicalVisitDTO updateMedicalVisit(MedicalVisitDTO theMedicalVisitDTO) {

        int theId = theMedicalVisitDTO.getId();

        MedicalVisit result = medicalVisitRepository.findById(theId)
        .orElseThrow(() -> new ApiException("MedicalVisit not found with id: " + theId, HttpStatus.NOT_FOUND));

        result.setDescription(theMedicalVisitDTO.getDescription());
        result.setDiseaseType(theMedicalVisitDTO.getDiseaseType());

        MedicalVisit updatedVisit = medicalVisitRepository.save(result);
        return medicalVisitMapper.toDto(updatedVisit);
    }

    public void softDeleteMedicalVisit(int theId) {
        
        MedicalVisit medicalVisit = medicalVisitRepository.findById(theId)
            .orElseThrow(() -> new ApiException("MedicalVisit not found with id: " + theId, HttpStatus.NOT_FOUND));

        medicalVisit.setStatus(Status.DELETED);

        medicalVisitRepository.save(medicalVisit);
    }
}
