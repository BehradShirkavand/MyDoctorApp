package com.example.mydoctor.service;

import java.util.List;
import java.util.Map;

import com.example.mydoctor.entity.Role;
import com.example.mydoctor.entity.User;
import com.example.mydoctor.enums.ERole;
import com.example.mydoctor.exception.ApiException;
import com.example.mydoctor.repository.RoleRepository;
import com.example.mydoctor.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.mydoctor.dto.*;
import com.example.mydoctor.entity.MedicalVisit;
import com.example.mydoctor.enums.Status;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ObjectMapper objectMapper;
    private final UserMapper userMapper;
    private final MedicalVisitMapper medicalVisitMapper;
    private final PasswordEncoder passwordEncoder;


    public List<UserDTO> getAllPatients() {

        return userMapper.toDtoList(userRepository.findAllByRoles_NameAndStatus(ERole.ROLE_PATIENT, Status.ACTIVE));
    }

    public UserDTO getPatientById(int theId) {

        User theUser = userRepository.findByIdAndRoles_NameAndAndStatus(theId, ERole.ROLE_PATIENT, Status.ACTIVE)
                .orElseThrow(() -> new ApiException("Patient not found with id: " + theId, HttpStatus.NOT_FOUND));

        return userMapper.toDto(theUser);
    }

    public UserDTO getPatientByUsername(String username) {

        User theUser = userRepository.findByRoles_NameAndUsernameAndStatus(ERole.ROLE_PATIENT, username, Status.ACTIVE)
                .orElseThrow(() -> new ApiException("Patient not found with username: " + username, HttpStatus.NOT_FOUND));

        return userMapper.toDto(theUser);
    }

    public boolean existsPatientByUsername(String username) {
        return userRepository.existsByRoles_NameAndUsernameAndStatus(ERole.ROLE_PATIENT, username, Status.ACTIVE);
    }

    public UserDTO getPatientByPhoneNumber(String phoneNumber) {

        User theUser = userRepository.findByRoles_NameAndPhoneNumberAndStatus(ERole.ROLE_PATIENT, phoneNumber, Status.ACTIVE)
                .orElseThrow(() -> new ApiException("Patient not found with phone number: " + phoneNumber, HttpStatus.NOT_FOUND));

        return userMapper.toDto(theUser);
    }

    public boolean existsPatientByPhoneNumber(String phoneNumber) {
        return userRepository.existsByRoles_NameAndPhoneNumberAndStatus(ERole.ROLE_PATIENT, phoneNumber, Status.ACTIVE);
    }

    public void addPatient(UserDTO theUserDTO) {

        if (userRepository.existsByRoles_NameAndPhoneNumberAndStatus(ERole.ROLE_PATIENT, theUserDTO.getPhoneNumber(), Status.ACTIVE)) {
            throw new ApiException("Number is already taken", HttpStatus.BAD_REQUEST);
        }

        if (userRepository.existsByRoles_NameAndUsernameAndStatus(ERole.ROLE_PATIENT, theUserDTO.getUsername(), Status.ACTIVE)) {
            throw new ApiException("Username is already taken", HttpStatus.BAD_REQUEST);
        }

        User theUser = userMapper.toEntityForCreate(theUserDTO);

        if (theUserDTO.getMedicalVisits() != null) {

            for (MedicalVisitDTO visitDTO : theUserDTO.getMedicalVisits()) {

                MedicalVisit visit = medicalVisitMapper.toEntity(visitDTO);
                theUser.addMedicalVisit(visit);
            }
        }

        theUser.setPassword(passwordEncoder.encode(theUser.getPassword()));

        Role role = roleRepository.findByName(ERole.ROLE_PATIENT)
                .orElseThrow(() -> new ApiException("Role not found", HttpStatus.NOT_FOUND));

        theUser.getRoles().add(role);

        User savedUser = userRepository.save(theUser);

        userMapper.toDto(savedUser);
    }

    public UserDTO updatePatient(UserDTO theUserDTO) {

        int theId = theUserDTO.getId();

        User theUser = userRepository.findByIdAndRoles_NameAndAndStatus(theId, ERole.ROLE_PATIENT, Status.ACTIVE)
                .orElseThrow(() -> new ApiException("Patient not found with id: " + theId, HttpStatus.NOT_FOUND));

        theUser.setUsername(theUserDTO.getUsername());
        theUser.setEmail(theUserDTO.getEmail());

        theUser.getMedicalVisits().clear();

        if (theUserDTO.getMedicalVisits() != null) {

            for (MedicalVisitDTO visitDTO : theUserDTO.getMedicalVisits()) {

                MedicalVisit visit = medicalVisitMapper.toEntity(visitDTO);
                theUser.addMedicalVisit(visit);
            }
        }

        User updated = userRepository.save(theUser);
        return userMapper.toDto(updated);
    }

    public UserDTO patchPatient(int theId, Map<String, Object> patchPayload) {

        User tempPatient = userRepository.findByIdAndRoles_NameAndAndStatus(theId, ERole.ROLE_PATIENT, Status.ACTIVE)
                .orElseThrow(() -> new ApiException("Patient not found with id: " + theId, HttpStatus.NOT_FOUND));

        ObjectNode patientNode = objectMapper.convertValue(tempPatient, ObjectNode.class);

        ObjectNode patchNode = objectMapper.convertValue(patchPayload, ObjectNode.class);

        patientNode.setAll(patchNode);

        User mergedUser = objectMapper.convertValue(patientNode, User.class);

        User dbUser = userRepository.save(mergedUser);

        return userMapper.toDto(dbUser);
    }

    public void softDeletePatient(int theId) {

        User theUser = userRepository.findByIdAndRoles_NameAndAndStatus(theId, ERole.ROLE_PATIENT, Status.ACTIVE)
                .orElseThrow(() -> new ApiException("Patient not found with id: " + theId, HttpStatus.NOT_FOUND));

        theUser.setStatus(Status.DELETED);

        if (theUser.getMedicalVisits() != null) {
            theUser.getMedicalVisits().forEach(app -> app.setStatus(Status.DELETED));
        }

        userRepository.save(theUser);
    }
}
