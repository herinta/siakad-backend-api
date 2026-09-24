package com.sekolah.sisteminformasi.service;

import com.sekolah.sisteminformasi.dto.report.CumulativeTranscriptResponseDto;
import com.sekolah.sisteminformasi.dto.report.FinalReportResponseDto;
import com.sekolah.sisteminformasi.dto.report.MidtermReportResponseDto;

public interface ReportService {
    MidtermReportResponseDto generateMidtermReport(Long studentId, String academicYear, Integer semester);
    FinalReportResponseDto generateFinalReport(Long studentId, String academicYear, Integer semester);
    CumulativeTranscriptResponseDto generateCumulativeTranscript(Long studentId);
}
