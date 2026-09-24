package com.sekolah.sisteminformasi.service.impl;

import com.sekolah.sisteminformasi.dto.auth.AuthResponseDto;
import com.sekolah.sisteminformasi.dto.auth.LoginRequestDto;
import com.sekolah.sisteminformasi.dto.auth.RegisterRequestDto;
import com.sekolah.sisteminformasi.entity.User;
import com.sekolah.sisteminformasi.exception.BadRequestException;
import com.sekolah.sisteminformasi.repository.UserRepository;
import com.sekolah.sisteminformasi.security.JwtUtils;
import com.sekolah.sisteminformasi.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Override
    @Transactional
    public AuthResponseDto register(RegisterRequestDto registerRequestDto) {
        if (userRepository.existsByUsername(registerRequestDto.getUsername())) {
            throw new BadRequestException("Username '" + registerRequestDto.getUsername() + "' sudah digunakan!");
        }

        if (userRepository.existsByEmail(registerRequestDto.getEmail())) {
            throw new BadRequestException("Email '" + registerRequestDto.getEmail() + "' sudah terdaftar!");
        }

        User user = User.builder()
                .username(registerRequestDto.getUsername())
                .email(registerRequestDto.getEmail())
                .password(passwordEncoder.encode(registerRequestDto.getPassword()))
                .fullName(registerRequestDto.getFullName())
                .role(registerRequestDto.getRole())
                .isActive(true)
                .build();

        User savedUser = userRepository.save(user);

        // Langsung generate token saat berhasil register
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        registerRequestDto.getUsername(),
                        registerRequestDto.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtUtils.generateToken(authentication);

        return AuthResponseDto.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .userId(savedUser.getId())
                .username(savedUser.getUsername())
                .email(savedUser.getEmail())
                .fullName(savedUser.getFullName())
                .role(savedUser.getRole().name())
                .build();
    }

    @Override
    public AuthResponseDto login(LoginRequestDto loginRequestDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDto.getUsername(),
                        loginRequestDto.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtUtils.generateToken(authentication);

        User user = userRepository.findByUsername(loginRequestDto.getUsername())
                .or(() -> userRepository.findByEmail(loginRequestDto.getUsername()))
                .orElseThrow(() -> new BadRequestException("User tidak ditemukan"));

        return AuthResponseDto.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole().name())
                .build();
    }
}
