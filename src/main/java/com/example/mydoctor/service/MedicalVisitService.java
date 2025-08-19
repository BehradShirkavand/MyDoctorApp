package com.example.mydoctor.service;

import java.util.List;
import java.util.Optional;

import com.example.mydoctor.dto.UserDTO;
import com.example.mydoctor.entity.User;
import com.example.mydoctor.enums.ERole;
import com.example.mydoctor.exception.ApiException;
import com.example.mydoctor.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final UserRepository userRepository;

    public List<MedicalVisitDTO> getAllActiveMedicalVisits() {
        
        return medicalVisitMapper.toDtoList(medicalVisitRepository.findAllByStatus(Status.ACTIVE));
    }

    public MedicalVisitDTO getMedicalVisitById(int theId) {

        MedicalVisit theMedicalVisit = medicalVisitRepository.findById(theId).
        orElseThrow(() -> new ApiException("MedicalVisit not found with id: " + theId, HttpStatus.NOT_FOUND));

        return medicalVisitMapper.toDto(theMedicalVisit);
    }

    public MedicalVisitDTO addMedicalVisit(MedicalVisitDTO theMedicalVisitDTO) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        User patient = userRepository.findByRoles_NameAndUsernameAndStatus(ERole.ROLE_PATIENT, username, Status.ACTIVE).orElseThrow(() -> new ApiException("Patient not found", HttpStatus.NOT_FOUND));

        MedicalVisit visit = medicalVisitMapper.toEntity(theMedicalVisitDTO);

        patient.addMedicalVisit(visit);

        userRepository.save(patient);

        return medicalVisitMapper.toDto(visit);
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
