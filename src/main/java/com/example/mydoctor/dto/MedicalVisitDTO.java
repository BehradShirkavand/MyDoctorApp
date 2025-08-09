package com.example.mydoctor.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicalVisitDTO {


    @NotBlank(message = "Disease type can not be blank")
    private String diseaseType;

    private String description;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
