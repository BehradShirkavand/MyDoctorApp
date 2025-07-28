package com.example.mydoctor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicalVisitDTO {


    private String diseaseType;

    private String description;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
