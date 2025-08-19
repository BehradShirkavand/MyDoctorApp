package com.example.mydoctor.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class OtpVerifyRequest {

    private String tokenId;

    @NotBlank(message = "Otp can not be blank")
    @Size(min = 6, max = 6, message = "Otp must 6 digits")
    private String otp;
}
