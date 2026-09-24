package com.sekolah.sisteminformasi.controller;

import com.sekolah.sisteminformasi.dto.common.ApiResponse;
import com.sekolah.sisteminformasi.dto.grade.GradeRequestDto;
import com.sekolah.sisteminformasi.dto.grade.GradeResponseDto;
import com.sekolah.sisteminformasi.service.GradeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grades")
@RequiredArgsConstructor
public class GradeController {

    private final GradeService gradeService;

    @PostMapping
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<ApiResponse<GradeResponseDto>> inputGrade(@Valid @RequestBody GradeRequestDto gradeRequestDto) {
        GradeResponseDto grade = gradeService.inputOrUpdateGrade(gradeRequestDto);
        return new ResponseEntity<>(
                ApiResponse.created("Nilai ujian siswa berhasil disimpan!", grade),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<ApiResponse<GradeResponseDto>> getGradeById(@PathVariable Long id) {
        GradeResponseDto grade = gradeService.getGradeById(id);
        return ResponseEntity.ok(ApiResponse.success("Detail nilai berhasil diambil", grade));
    }

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<ApiResponse<List<GradeResponseDto>>> getGradesByStudent(
            @PathVariable Long studentId,
            @RequestParam String academicYear,
            @RequestParam Integer semester) {
        List<GradeResponseDto> grades = gradeService.getGradesByStudent(studentId, academicYear, semester);
        return ResponseEntity.ok(ApiResponse.success("Daftar nilai ujian siswa berhasil diambil", grades));
    }

    @GetMapping("/classroom/{classroomId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<ApiResponse<List<GradeResponseDto>>> getGradesByClassroom(
            @PathVariable Long classroomId,
            @RequestParam Long subjectId,
            @RequestParam String academicYear,
            @RequestParam Integer semester) {
        List<GradeResponseDto> grades = gradeService.getGradesByClassroomAndSubject(classroomId, subjectId, academicYear, semester);
        return ResponseEntity.ok(ApiResponse.success("Daftar nilai ujian kelas berhasil diambil", grades));
    }
}
