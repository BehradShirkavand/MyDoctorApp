package com.example.mydoctor.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.mydoctor.entity.OtpToken;

public interface OtpTokenRepository extends JpaRepository<OtpToken, Long> {

    Optional<OtpToken> findByPhoneNumberAndOtpAndUsedFalse(String username, String otp);

    Optional<OtpToken> findTopByPhoneNumberAndUsedFalseOrderByCreatedAtDesc(String phoneNumber);
}

