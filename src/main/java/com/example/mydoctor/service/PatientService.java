package com.example.mydoctor.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.mydoctor.dto.MedicalVisitDTO;
import com.example.mydoctor.dto.MedicalVisitMapper;
import com.example.mydoctor.dto.PatientDTO;
import com.example.mydoctor.dto.PatientMapper;
import com.example.mydoctor.entity.MedicalVisit;
import com.example.mydoctor.entity.Patient;
import com.example.mydoctor.enums.Role;
import com.example.mydoctor.enums.Status;
import com.example.mydoctor.repository.PatientRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Service
public class PatientService {

    private PatientRepository patientRepository;

    private ObjectMapper objectMapper;

    private PatientMapper patientMapper;

    private MedicalVisitMapper medicalVisitMapper;

    private  PasswordEncoder passwordEncoder;


    @Autowired
    public PatientService (
        PatientRepository thePatientRepository, 
        PatientMapper thePatientMapper, 
        MedicalVisitMapper theMedicalVisitMapper,
        ObjectMapper theObjectMapper,
        PasswordEncoder thePasswordEncoder
        ) {
        
        this.patientRepository = thePatientRepository;
        this.patientMapper = thePatientMapper;
        this.medicalVisitMapper = theMedicalVisitMapper;
        this.objectMapper = theObjectMapper;
        this.passwordEncoder = thePasswordEncoder;
    }

    
    public List<PatientDTO> getAllActivePatients() {

        return patientMapper.toDtoList(patientRepository.findAllByStatus(Status.ACTIVE));
    }

    
    public PatientDTO getPatientById(int theId) {

        Patient thePatient = patientRepository.findByIdAndStatus(theId, Status.ACTIVE)
            .orElseThrow(() -> new RuntimeException("Patient not found with id: " + theId));

        return patientMapper.toDto(thePatient);

    }

    public Optional<Patient> getPatientByUsername(String username) {

        return patientRepository.findByUsername(username);

    }

    public PatientDTO createPatient(PatientDTO thePatientDTO) {

        Patient thePatient = patientMapper.toEntityForCreate(thePatientDTO);

        if (thePatientDTO.getMedicalVisits() != null) {

            for (MedicalVisitDTO visitDTO: thePatientDTO.getMedicalVisits()) {

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
            .orElseThrow(() -> new RuntimeException("Patient not found with id: " + theId));

        thePatient.setUsername(thePatientDTO.getUsername());
        thePatient.setEmail(thePatientDTO.getEmail());

        thePatient.getMedicalVisits().clear();

        if (thePatientDTO.getMedicalVisits() != null) {

            for (MedicalVisitDTO visitDTO: thePatientDTO.getMedicalVisits()) {

                MedicalVisit visit = medicalVisitMapper.toEntity(visitDTO);
                thePatient.addMedicalVisit(visit);
            }
        }
    
        Patient updated = patientRepository.save(thePatient);
        return patientMapper.toDto(updated);
    }

    public PatientDTO patchPatient(int theId, Map<String, Object> patchPayload) {
        
        Patient tempPatient = patientRepository.findByIdAndStatus(theId, Status.ACTIVE)
            .orElseThrow(() -> new RuntimeException("Patient not found with id: " + theId));

        ObjectNode patientNode = objectMapper.convertValue(tempPatient, ObjectNode.class);

        ObjectNode patchNode = objectMapper.convertValue(patchPayload, ObjectNode.class);

        patientNode.setAll(patchNode);

        Patient mergedPatient = objectMapper.convertValue(patientNode, Patient.class);

        Patient dbPatient = patientRepository.save(mergedPatient);

        return patientMapper.toDto(dbPatient);
    }

    public void softDeletePatient(int theId) {

        Patient thePatient = patientRepository.findByIdAndStatus(theId, Status.ACTIVE)
            .orElseThrow(() -> new RuntimeException("Patient not found with id: " + theId));

        thePatient.setStatus(Status.DELETED);

        if (thePatient.getMedicalVisits() != null) {
            thePatient.getMedicalVisits().forEach(app -> app.setStatus(Status.DELETED));
        }

        patientRepository.save(thePatient);
    }

}
