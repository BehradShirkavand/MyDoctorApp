package com.example.mydoctor.dto;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.mydoctor.entity.Patient;

@Mapper(componentModel = "spring", uses = MedicalVisitMapper.class)
public interface PatientMapper {

    @Mapping(target = "password", ignore = true)
    PatientDTO toDto(Patient patient);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", expression = "java(com.example.mydoctor.enums.Status.ACTIVE)")
    @Mapping(target = "medicalVisits", ignore = true)
    @Mapping(target = "roles", ignore = true)

    Patient toEntityForCreate(PatientDTO dto);

    @Mapping(target = "status", expression = "java(com.example.mydoctor.enums.Status.ACTIVE)")
    @Mapping(target = "medicalVisits", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "roles", ignore = true)
    Patient toEntity(PatientDTO dto);

    List<PatientDTO> toDtoList(List<Patient> patients);
}
