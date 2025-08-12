package com.example.mydoctor.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.example.mydoctor.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.mydoctor.dto.MedicalVisitDTO;
import com.example.mydoctor.dto.MedicalVisitMapper;
import com.example.mydoctor.dto.PatientDTO;
import com.example.mydoctor.dto.PatientMapper;
import com.example.mydoctor.entity.MedicalVisit;
import com.example.mydoctor.entity.Patient;
import com.example.mydoctor.enums.Status;
import com.example.mydoctor.repository.PatientRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final ObjectMapper objectMapper;
    private final PatientMapper patientMapper;
    private final MedicalVisitMapper medicalVisitMapper;
    private final PasswordEncoder passwordEncoder;


    public List<PatientDTO> getAllActivePatients() {

        return patientMapper.toDtoList(patientRepository.findAllByStatus(Status.ACTIVE));
    }

    public PatientDTO getPatientById(int theId) {

        Patient thePatient = patientRepository.findByIdAndStatus(theId, Status.ACTIVE)
                .orElseThrow(() -> new ApiException("Patient not found with id: " + theId, HttpStatus.NOT_FOUND));

        return patientMapper.toDto(thePatient);
    }

    public PatientDTO getPatientByUsername(String username) {

        Patient thePatient = patientRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException("Patient not found with username: " + username, HttpStatus.NOT_FOUND));

        return patientMapper.toDto(thePatient);
    }

    public PatientDTO getPatientByPhoneNumber(String phoneNumber) {

        Patient thePatient = patientRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new ApiException("Patient not found with phone number: " + phoneNumber, HttpStatus.NOT_FOUND));

        return patientMapper.toDto(thePatient);
    }

    public PatientDTO createPatient(PatientDTO thePatientDTO) {

        if (patientRepository.findByPhoneNumber(thePatientDTO.getPhoneNumber()).isPresent()) {
            throw new ApiException("Number is already taken", HttpStatus.BAD_REQUEST);
        }

        if (patientRepository.findByUsername(thePatientDTO.getUsername()).isPresent()) {
            throw new ApiException("Username is already taken", HttpStatus.BAD_REQUEST);
        }

        Patient thePatient = patientMapper.toEntityForCreate(thePatientDTO);

        if (thePatientDTO.getMedicalVisits() != null) {

            for (MedicalVisitDTO visitDTO : thePatientDTO.getMedicalVisits()) {

                MedicalVisit visit = medicalVisitMapper.toEntity(visitDTO);
                thePatient.addMedicalVisit(visit);
            }
        }

        thePatient.setPassword(passwordEncoder.encode(thePatient.getPassword()));

        thePatient.setRoles(Set.of("ROLE_PATIENT"));

        Patient savedPatient = patientRepository.save(thePatient);

        return patientMapper.toDto(savedPatient);
    }

    public PatientDTO updatePatient(PatientDTO thePatientDTO) {

        int theId = thePatientDTO.getId();

        Patient thePatient = patientRepository.findByIdAndStatus(theId, Status.ACTIVE)
                .orElseThrow(() -> new ApiException("Patient not found with id: " + theId, HttpStatus.NOT_FOUND));

        thePatient.setUsername(thePatientDTO.getUsername());
        thePatient.setEmail(thePatientDTO.getEmail());

        thePatient.getMedicalVisits().clear();

        if (thePatientDTO.getMedicalVisits() != null) {

            for (MedicalVisitDTO visitDTO : thePatientDTO.getMedicalVisits()) {

                MedicalVisit visit = medicalVisitMapper.toEntity(visitDTO);
                thePatient.addMedicalVisit(visit);
            }
        }

        Patient updated = patientRepository.save(thePatient);
        return patientMapper.toDto(updated);
    }

    public PatientDTO patchPatient(int theId, Map<String, Object> patchPayload) {

        Patient tempPatient = patientRepository.findByIdAndStatus(theId, Status.ACTIVE)
                .orElseThrow(() -> new ApiException("Patient not found with id: " + theId, HttpStatus.NOT_FOUND));

        ObjectNode patientNode = objectMapper.convertValue(tempPatient, ObjectNode.class);

        ObjectNode patchNode = objectMapper.convertValue(patchPayload, ObjectNode.class);

        patientNode.setAll(patchNode);

        Patient mergedPatient = objectMapper.convertValue(patientNode, Patient.class);

        Patient dbPatient = patientRepository.save(mergedPatient);

        return patientMapper.toDto(dbPatient);
    }

    public void softDeletePatient(int theId) {

        Patient thePatient = patientRepository.findByIdAndStatus(theId, Status.ACTIVE)
                .orElseThrow(() -> new ApiException("Patient not found with id: " + theId, HttpStatus.NOT_FOUND));

        thePatient.setStatus(Status.DELETED);

        if (thePatient.getMedicalVisits() != null) {
            thePatient.getMedicalVisits().forEach(app -> app.setStatus(Status.DELETED));
        }

        patientRepository.save(thePatient);
    }
}
