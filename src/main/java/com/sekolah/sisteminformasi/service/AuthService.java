package com.sekolah.sisteminformasi.service;

import com.sekolah.sisteminformasi.dto.auth.AuthResponseDto;
import com.sekolah.sisteminformasi.dto.auth.LoginRequestDto;
import com.sekolah.sisteminformasi.dto.auth.RegisterRequestDto;

public interface AuthService {
    AuthResponseDto register(RegisterRequestDto registerRequestDto);
    AuthResponseDto login(LoginRequestDto loginRequestDto);
}
