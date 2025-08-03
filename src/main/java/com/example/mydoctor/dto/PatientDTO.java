package com.example.mydoctor.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientDTO {

    private int id;
    @NotBlank(message = "username is not valid")
    private String username;

    @JsonProperty(access = Access.WRITE_ONLY)
    private String password;

    private String phoneNumber;

    private String email;

    private List<MedicalVisitDTO> medicalVisits;
}
