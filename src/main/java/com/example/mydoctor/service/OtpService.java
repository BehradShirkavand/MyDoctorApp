package com.example.mydoctor.service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.example.mydoctor.dto.UserDTO;
import com.example.mydoctor.exception.ApiException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OtpService {
    
    private final Map<String, OtpToken> otpStorage = new ConcurrentHashMap<>();
    private final SmsService smsService;

    public String generateOtp(UserDTO userDTO) {

        // delete expired otp
        otpStorage.entrySet().removeIf(entry -> {
            OtpToken existingOtp = entry.getValue();
            return (existingOtp.getUserDTO().getId() == userDTO.getId()) && existingOtp.isExpired();
        });

        boolean hasValidOtp = otpStorage.values().stream()
                .anyMatch(existingOtp -> (existingOtp.getUserDTO().getId() == userDTO.getId()) && !existingOtp.isExpired());

        if (hasValidOtp) {
            throw new ApiException("OTP already sent. Please wait until it expires.", HttpStatus.BAD_REQUEST);
        }

        String tokenId = UUID.randomUUID().toString();
        String otp = String.valueOf(new SecureRandom().nextInt(900_000) + 100_000); 

        OtpToken otptoken = new OtpToken(otp, userDTO, LocalDateTime.now().plusMinutes(5));
        otpStorage.put(tokenId, otptoken);

        smsService.sendSms(userDTO.getPhoneNumber(), otp);
        return tokenId;

    }

    public UserDTO verifyOtp(String tokenId, String otp) {

        if (!otpStorage.containsKey(tokenId)) {
            throw new ApiException("Invalid or expired OTP", HttpStatus.UNAUTHORIZED);
        }

        OtpToken otpToken = otpStorage.get(tokenId);

        if (otpToken.isExpired() || !otpToken.getOtp().equals(otp)) {
            throw new ApiException("Invalid or expired OTP", HttpStatus.UNAUTHORIZED);
        }
        otpStorage.remove(tokenId);
        return otpToken.getUserDTO();
    }

    @Getter
    private static class OtpToken {

        private final String otp;
        private final UserDTO userDTO;
        private final LocalDateTime expiresAt;

        public OtpToken(String otp, UserDTO userDTO, LocalDateTime expiresAt) {
            this.otp = otp;
            this.userDTO = userDTO;
            this.expiresAt = expiresAt;
        }

        public boolean isExpired() {
            return LocalDateTime.now().isAfter(expiresAt);
        }
    }
}