package com.sekolah.sisteminformasi.controller;

import com.sekolah.sisteminformasi.dto.auth.AuthResponseDto;
import com.sekolah.sisteminformasi.dto.auth.LoginRequestDto;
import com.sekolah.sisteminformasi.dto.auth.RegisterRequestDto;
import com.sekolah.sisteminformasi.dto.common.ApiResponse;
import com.sekolah.sisteminformasi.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponseDto>> register(@Valid @RequestBody RegisterRequestDto registerRequestDto) {
        AuthResponseDto responseDto = authService.register(registerRequestDto);
        return new ResponseEntity<>(
                ApiResponse.created("Pengguna berhasil didaftarkan!", responseDto),
                HttpStatus.CREATED
        );
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponseDto>> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        AuthResponseDto responseDto = authService.login(loginRequestDto);
        return ResponseEntity.ok(
                ApiResponse.success("Login berhasil!", responseDto)
        );
    }
}
