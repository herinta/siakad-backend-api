package com.sekolah.sisteminformasi.controller;

import com.sekolah.sisteminformasi.dto.common.ApiResponse;
import com.sekolah.sisteminformasi.dto.teacher.TeacherRequestDto;
import com.sekolah.sisteminformasi.dto.teacher.TeacherResponseDto;
import com.sekolah.sisteminformasi.service.TeacherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teachers")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<TeacherResponseDto>> createTeacher(@Valid @RequestBody TeacherRequestDto teacherRequestDto) {
        TeacherResponseDto createdTeacher = teacherService.createTeacher(teacherRequestDto);
        return new ResponseEntity<>(
                ApiResponse.created("Data guru berhasil ditambahkan!", createdTeacher),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<ApiResponse<TeacherResponseDto>> getTeacherById(@PathVariable Long id) {
        TeacherResponseDto teacher = teacherService.getTeacherById(id);
        return ResponseEntity.ok(ApiResponse.success("Detail guru berhasil diambil", teacher));
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<ApiResponse<TeacherResponseDto>> getTeacherByUserId(@PathVariable Long userId) {
        TeacherResponseDto teacher = teacherService.getTeacherByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success("Profil guru berhasil diambil", teacher));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<ApiResponse<List<TeacherResponseDto>>> getAllTeachers() {
        List<TeacherResponseDto> teachers = teacherService.getAllTeachers();
        return ResponseEntity.ok(ApiResponse.success("Daftar guru berhasil diambil", teachers));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<TeacherResponseDto>> updateTeacher(
            @PathVariable Long id,
            @Valid @RequestBody TeacherRequestDto teacherRequestDto) {
        TeacherResponseDto updatedTeacher = teacherService.updateTeacher(id, teacherRequestDto);
        return ResponseEntity.ok(ApiResponse.success("Data guru berhasil diperbarui!", updatedTeacher));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteTeacher(@PathVariable Long id) {
        teacherService.deleteTeacher(id);
        return ResponseEntity.ok(ApiResponse.success("Data guru berhasil dinonaktifkan (soft-delete)!", null));
    }
}
