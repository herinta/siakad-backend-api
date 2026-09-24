package com.sekolah.sisteminformasi.controller;

import com.sekolah.sisteminformasi.dto.attendance.AttendanceResponseDto;
import com.sekolah.sisteminformasi.dto.attendance.AttendanceSummaryDto;
import com.sekolah.sisteminformasi.dto.attendance.BatchAttendanceRequestDto;
import com.sekolah.sisteminformasi.dto.common.ApiResponse;
import com.sekolah.sisteminformasi.service.AttendanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/attendances")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping("/batch")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<ApiResponse<List<AttendanceResponseDto>>> recordBatchAttendance(
            @Valid @RequestBody BatchAttendanceRequestDto batchRequest) {
        List<AttendanceResponseDto> results = attendanceService.recordBatchAttendance(batchRequest);
        return new ResponseEntity<>(
                ApiResponse.created("Presensi absensi siswa berhasil dicatat!", results),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/schedule/{scheduleId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<ApiResponse<List<AttendanceResponseDto>>> getAttendanceByScheduleAndDate(
            @PathVariable Long scheduleId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<AttendanceResponseDto> attendances = attendanceService.getAttendanceByScheduleAndDate(scheduleId, date);
        return ResponseEntity.ok(ApiResponse.success("Data absensi berhasil diambil", attendances));
    }

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<ApiResponse<List<AttendanceResponseDto>>> getAttendanceByStudent(@PathVariable Long studentId) {
        List<AttendanceResponseDto> attendances = attendanceService.getAttendanceByStudentId(studentId);
        return ResponseEntity.ok(ApiResponse.success("Riwayat absensi siswa berhasil diambil", attendances));
    }

    @GetMapping("/student/{studentId}/summary")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<ApiResponse<AttendanceSummaryDto>> getStudentAttendanceSummary(
            @PathVariable Long studentId,
            @RequestParam String academicYear,
            @RequestParam Integer semester) {
        AttendanceSummaryDto summary = attendanceService.getStudentAttendanceSummary(studentId, academicYear, semester);
        return ResponseEntity.ok(ApiResponse.success("Ringkasan kehadiran siswa berhasil diambil", summary));
    }
}
