package com.sekolah.sisteminformasi.controller;

import com.sekolah.sisteminformasi.dto.common.ApiResponse;
import com.sekolah.sisteminformasi.dto.report.CumulativeTranscriptResponseDto;
import com.sekolah.sisteminformasi.dto.report.FinalReportResponseDto;
import com.sekolah.sisteminformasi.dto.report.MidtermReportResponseDto;
import com.sekolah.sisteminformasi.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/midterm")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<ApiResponse<MidtermReportResponseDto>> getMidtermReport(
            @RequestParam Long studentId,
            @RequestParam String academicYear,
            @RequestParam Integer semester) {
        MidtermReportResponseDto report = reportService.generateMidtermReport(studentId, academicYear, semester);
        return ResponseEntity.ok(ApiResponse.success("Rapor UTS berhasil dibuat dan diambil", report));
    }

    @GetMapping("/final")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<ApiResponse<FinalReportResponseDto>> getFinalReport(
            @RequestParam Long studentId,
            @RequestParam String academicYear,
            @RequestParam Integer semester) {
        FinalReportResponseDto report = reportService.generateFinalReport(studentId, academicYear, semester);
        return ResponseEntity.ok(ApiResponse.success("Rapor Akhir Semester berhasil dibuat dan diambil", report));
    }

    @GetMapping("/transcript/{studentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<ApiResponse<CumulativeTranscriptResponseDto>> getCumulativeTranscript(
            @PathVariable Long studentId) {
        CumulativeTranscriptResponseDto transcript = reportService.generateCumulativeTranscript(studentId);
        return ResponseEntity.ok(ApiResponse.success("Transkrip Nilai Kumulatif SMP berhasil dimuat", transcript));
    }
}
