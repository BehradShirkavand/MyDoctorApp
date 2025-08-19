package com.example.mydoctor.service;

import com.example.mydoctor.dto.UserDTO;
import com.example.mydoctor.dto.UserMapper;
import com.example.mydoctor.entity.RefreshToken;
import com.example.mydoctor.entity.User;
import com.example.mydoctor.exception.ApiException;
import com.example.mydoctor.repository.RefreshTokenRepository;
import com.example.mydoctor.utils.JwtUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;
    private final PatientService patientService;
    private final UserMapper userMapper;

    Instant expiryDate = Instant.now().plusSeconds(7 * 24 * 60 * 60); // 7 days

    public RefreshToken createRefreshToken(UserDTO userDTO) {

        UserDTO user = patientService.getPatientByUsername(userDTO.getUsername());
        String token = jwtUtil.generateToken(userDTO.getUsername(), expiryDate);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(userMapper.toEntity(user));
        refreshToken.setToken(token);
        refreshToken.setExpiryDate(expiryDate);

        return refreshTokenRepository.save(refreshToken);
    }

    public void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) {

        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/auth/refresh");
        cookie.setMaxAge(7 * 24 * 60 * 60);
        response.addCookie(cookie);
    }

    @Transactional
    public RefreshToken rotateRefreshToken(String oldToken) {

        RefreshToken refreshToken = refreshTokenRepository.findByToken(oldToken)
                .orElseThrow(() -> new ApiException("Invalid Refresh token", HttpStatus.BAD_REQUEST));

        if (refreshToken.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.deleteByToken(oldToken);
            throw new ApiException("Refresh token expired", HttpStatus.BAD_REQUEST);
        }

        refreshTokenRepository.deleteByToken(oldToken);

        UserDTO userDTO = userMapper.toDto(refreshToken.getUser());

        return createRefreshToken(userDTO);
    }
}

