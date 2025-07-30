package com.example.mydoctor.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.mydoctor.entity.MedicalVisit;
import com.example.mydoctor.enums.Status;

public interface MedicalVisitRepository extends JpaRepository<MedicalVisit, Integer> {

    List<MedicalVisit> findAllByStatus(Status status);

}
