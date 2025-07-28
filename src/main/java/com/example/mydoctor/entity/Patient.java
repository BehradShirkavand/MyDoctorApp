package com.example.mydoctor.entity;

import java.util.ArrayList;
import java.util.List;

import com.example.mydoctor.enums.Status;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="patient")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="username", unique = true, nullable = false)    
    private String username;

    @Column(name="email")
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.ACTIVE;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MedicalVisit> medicalVisits;


    public void addMedicalVisit(MedicalVisit tempMedicalVisit) {

        if (medicalVisits == null) {
            medicalVisits = new ArrayList<>();
        }

        medicalVisits.add(tempMedicalVisit);
        tempMedicalVisit.setPatient(this);
    }
}
