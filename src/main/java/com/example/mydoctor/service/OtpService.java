package com.example.mydoctor.service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OtpService {
    
    private final Map<String, OtpToken> otpStorage = new ConcurrentHashMap<>();

    private SmsService smsService;

    @Autowired
    public OtpService(SmsService theSmsService) {
        this.smsService = theSmsService;
    }

    public void generateOtp(String phoneNumber) {

        if (otpStorage.containsKey(phoneNumber)) {
            OtpToken existingOtp = otpStorage.get(phoneNumber);

            if (!existingOtp.isExpired()) {
                throw new IllegalStateException("OTP already sent. Please wait until it expires.");
            } else {
                otpStorage.remove(phoneNumber);
            }
        }
        
        String otp = String.valueOf(new SecureRandom().nextInt(900_000) + 100_000); 

        OtpToken otptoken = new OtpToken(otp, LocalDateTime.now().plusMinutes(5));
        otpStorage.put(phoneNumber, otptoken);

        smsService.sendSms(phoneNumber, otp);

    }

    public boolean verifyOtp(String phoneNumber, String otp) {

        if (!otpStorage.containsKey(phoneNumber)) return false;

        OtpToken otpToken = otpStorage.get(phoneNumber);

        if (otpToken.isExpired() || !otpToken.getOtp().equals(otp)) {

            otpStorage.remove(phoneNumber);
            return false;
        }

        otpStorage.remove(phoneNumber);
        
        return true;
    }

    private static class OtpToken {

        private final String otp;
        private final LocalDateTime expiresAt;

        public OtpToken(String otp, LocalDateTime expiresAt) {
            this.otp = otp;
            this.expiresAt = expiresAt;
        }

        public String getOtp() {
            return otp;
        }

        public boolean isExpired() {
            return LocalDateTime.now().isAfter(expiresAt);
        }
    }
}

