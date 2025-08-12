package com.example.mydoctor.dto;

import com.example.mydoctor.entity.MedicalVisit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MedicalVisitMapper {

    MedicalVisitDTO toDto(MedicalVisit visit);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", expression = "java(com.example.mydoctor.enums.Status.ACTIVE)")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    MedicalVisit toEntity(MedicalVisitDTO dto);

    List<MedicalVisitDTO> toDtoList(List<MedicalVisit> visits);
}
