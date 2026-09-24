package com.sekolah.sisteminformasi.controller;

import com.sekolah.sisteminformasi.dto.assignment.AssignmentRequestDto;
import com.sekolah.sisteminformasi.dto.assignment.AssignmentResponseDto;
import com.sekolah.sisteminformasi.dto.assignment.AssignmentScoreRequestDto;
import com.sekolah.sisteminformasi.dto.assignment.AssignmentScoreResponseDto;
import com.sekolah.sisteminformasi.dto.common.ApiResponse;
import com.sekolah.sisteminformasi.service.AssignmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assignments")
@RequiredArgsConstructor
public class AssignmentController {

    private final AssignmentService assignmentService;

    @PostMapping
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<ApiResponse<AssignmentResponseDto>> createAssignment(@Valid @RequestBody AssignmentRequestDto requestDto) {
        AssignmentResponseDto assignment = assignmentService.createAssignment(requestDto);
        return new ResponseEntity<>(
                ApiResponse.created("Tugas baru berhasil dibuat!", assignment),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<ApiResponse<AssignmentResponseDto>> getAssignmentById(@PathVariable Long id) {
        AssignmentResponseDto assignment = assignmentService.getAssignmentById(id);
        return ResponseEntity.ok(ApiResponse.success("Detail tugas berhasil diambil", assignment));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<ApiResponse<List<AssignmentResponseDto>>> getAssignments(
            @RequestParam(required = false) Long classroomId,
            @RequestParam(required = false) Long subjectId,
            @RequestParam(required = false) Long teacherId) {

        List<AssignmentResponseDto> assignments;
        if (classroomId != null && subjectId != null) {
            assignments = assignmentService.getAssignmentsByClassroomAndSubject(classroomId, subjectId);
        } else if (teacherId != null) {
            assignments = assignmentService.getAssignmentsByTeacherId(teacherId);
        } else {
            assignments = assignmentService.getAssignmentsByTeacherId(teacherId != null ? teacherId : 1L);
        }

        return ResponseEntity.ok(ApiResponse.success("Daftar tugas berhasil diambil", assignments));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<ApiResponse<Void>> deleteAssignment(@PathVariable Long id) {
        assignmentService.deleteAssignment(id);
        return ResponseEntity.ok(ApiResponse.success("Tugas berhasil dihapus!", null));
    }

    // =========================================================================
    // 📝 ENDPOINTS NILAI TUGAS SISWA
    // =========================================================================

    @PostMapping("/{assignmentId}/scores")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<ApiResponse<AssignmentScoreResponseDto>> inputScore(
            @PathVariable Long assignmentId,
            @Valid @RequestBody AssignmentScoreRequestDto scoreRequestDto) {

        AssignmentScoreResponseDto score = assignmentService.inputAssignmentScore(assignmentId, scoreRequestDto);
        return ResponseEntity.ok(ApiResponse.success("Nilai tugas siswa berhasil disimpan!", score));
    }

    @GetMapping("/{assignmentId}/scores")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<ApiResponse<List<AssignmentScoreResponseDto>>> getScoresByAssignment(@PathVariable Long assignmentId) {
        List<AssignmentScoreResponseDto> scores = assignmentService.getScoresByAssignmentId(assignmentId);
        return ResponseEntity.ok(ApiResponse.success("Daftar nilai tugas berhasil diambil", scores));
    }

    @GetMapping("/scores/student/{studentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<ApiResponse<List<AssignmentScoreResponseDto>>> getScoresByStudent(@PathVariable Long studentId) {
        List<AssignmentScoreResponseDto> scores = assignmentService.getScoresByStudentId(studentId);
        return ResponseEntity.ok(ApiResponse.success("Riwayat nilai tugas siswa berhasil diambil", scores));
    }
}
