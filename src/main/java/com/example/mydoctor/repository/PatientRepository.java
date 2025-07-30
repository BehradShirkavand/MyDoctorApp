package com.example.mydoctor.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.mydoctor.entity.Patient;
import com.example.mydoctor.enums.Status;


public interface PatientRepository extends JpaRepository<Patient, Integer> {
        
    List<Patient> findAllByStatus(Status status);

    Optional<Patient> findByIdAndStatus(int id, Status status);

    Optional<Patient> findByUsername(String username);

}
