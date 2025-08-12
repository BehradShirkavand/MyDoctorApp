package com.example.mydoctor.dto;

import java.util.List;

import com.example.mydoctor.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring", uses = MedicalVisitMapper.class)
public interface UserMapper {

    @Mapping(target = "password", ignore = true)
    UserDTO toDto(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", expression = "java(com.example.mydoctor.enums.Status.ACTIVE)")
    @Mapping(target = "medicalVisits", ignore = true)
    User toEntityForCreate(UserDTO dto);

    @Mapping(target = "status", expression = "java(com.example.mydoctor.enums.Status.ACTIVE)")
    @Mapping(target = "medicalVisits", ignore = true)
    @Mapping(target = "password", ignore = true)
    User toEntity(UserDTO dto);

    List<UserDTO> toDtoList(List<User> patients);
}
