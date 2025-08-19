package com.example.mydoctor.restcontroller;

import com.example.mydoctor.dto.UserDTO;
import com.example.mydoctor.entity.RefreshToken;
import com.example.mydoctor.entity.User;
import com.example.mydoctor.payload.ApiResponse;
import com.example.mydoctor.service.OtpService;
import com.example.mydoctor.service.PatientService;
import com.example.mydoctor.service.RefreshTokenService;
import com.example.mydoctor.utils.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.mydoctor.dto.OtpVerifyRequest;
import com.example.mydoctor.service.RegistrationService;

import jakarta.validation.Valid;

import java.time.Instant;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;
    private final OtpService otpService;
    private final RefreshTokenService refreshTokenService;
    private final JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<ApiResponse<String>> RegisterAndLogin(@Valid @RequestBody UserDTO theUserDTO) {

        String tokenId = registrationService.registerAndLogin(theUserDTO);
        return ResponseEntity.ok(new ApiResponse<>(true, "Otp sent successfully", tokenId));
    }

    @PostMapping("/verifyOtp")
    public ResponseEntity<ApiResponse<String>> verifyOtp(@Valid @RequestBody OtpVerifyRequest request, HttpServletResponse response) {

        UserDTO userDTO = otpService.verifyOtp(request.getTokenId(), request.getOtp());

        // generate refresh token (7 days)
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDTO);
        refreshTokenService.setRefreshTokenCookie(response, refreshToken.getToken());

        // generate access token (15 minutes)
        String accessToken = jwtUtil.generateToken(userDTO.getUsername(), Instant.now().plusSeconds(15 * 60));

        return ResponseEntity.ok(new ApiResponse<>(true, "Authenticated successfully", accessToken));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<String>> refresh(@CookieValue("refreshToken") String oldToken, HttpServletResponse response) {

        // Rotate + Delete
        RefreshToken newRefreshToken = refreshTokenService.rotateRefreshToken(oldToken);

        User user = newRefreshToken.getUser();
        String accessToken = jwtUtil.generateToken(user.getUsername(), Instant.now().plusSeconds(15 * 60));

        // New refresh token for cookie
        refreshTokenService.setRefreshTokenCookie(response, newRefreshToken.getToken());

        return ResponseEntity.ok(new ApiResponse<>(true, "accessToken", accessToken));
    }
}