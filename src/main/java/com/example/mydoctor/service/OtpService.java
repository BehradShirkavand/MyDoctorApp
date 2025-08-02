package com.example.mydoctor.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.mydoctor.entity.OtpToken;
import com.example.mydoctor.repository.OtpTokenRepository;

@Service
public class OtpService {

    private OtpTokenRepository otpTokenRepository;

    private SmsService smsService;

    @Autowired
    public OtpService(OtpTokenRepository theOtpTokenRepository, SmsService theSmsService) {
        this.otpTokenRepository = theOtpTokenRepository;
        this.smsService = theSmsService;
    }

    public void generateOtp(String phoneNumber) {

        Optional<OtpToken> existingOtp = otpTokenRepository
            .findTopByPhoneNumberAndUsedFalseOrderByCreatedAtDesc(phoneNumber);

        if (existingOtp.isPresent()) {

            OtpToken otp = existingOtp.get();

        if (otp.getExpiresAt().isAfter(LocalDateTime.now())) {

            throw new IllegalStateException("Previous code is valid");
        } else {

            otpTokenRepository.delete(otp);
        }
    }
        
        String otp = String.valueOf(new Random().nextInt(900_000) + 100_000); 

        OtpToken token = new OtpToken();
        token.setPhoneNumber(phoneNumber);
        token.setOtp(otp);
        token.setCreatedAt(LocalDateTime.now());
        token.setExpiresAt(LocalDateTime.now().plusMinutes(5));
        token.setUsed(false);

        otpTokenRepository.save(token);

        smsService.sendSms(phoneNumber, otp);

    }

    public boolean verifyOtp(String phoneNumber, String otp) {

        Optional<OtpToken> tokenOpt = otpTokenRepository.findByPhoneNumberAndOtpAndUsedFalse(phoneNumber, otp);

        if (tokenOpt.isPresent()) {

            OtpToken token = tokenOpt.get();
            
            if (token.getExpiresAt().isAfter(LocalDateTime.now())) {
                token.setUsed(true);
                otpTokenRepository.save(token);
                return true;
            }
        }
        return false;
    }
}

