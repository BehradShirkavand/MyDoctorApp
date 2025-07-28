package com.example.mydoctor.dto;

import java.util.List;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientDTO {

    private int id;

    private String username;

    private String email;

    private List<MedicalVisitDTO> medicalVisits;
}
