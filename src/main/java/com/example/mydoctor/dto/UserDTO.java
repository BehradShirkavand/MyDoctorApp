package com.example.mydoctor.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private int id;

    @NotBlank(message = "Username can not be blank")
    private String username;

    @Size(min = 4, message = "Password must be at least 4 characters")
    @JsonProperty(access = Access.WRITE_ONLY)
    private String password;

    @Pattern(
            regexp = "^09\\d{9}$",
            message = "Invalid phone number"
    )
    private String phoneNumber;

    @Email(message = "Invalid email address")
    private String email;

    private List<MedicalVisitDTO> medicalVisits;
}
