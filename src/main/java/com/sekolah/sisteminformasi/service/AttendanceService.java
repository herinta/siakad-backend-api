package com.sekolah.sisteminformasi.service;

import com.sekolah.sisteminformasi.dto.attendance.AttendanceResponseDto;
import com.sekolah.sisteminformasi.dto.attendance.AttendanceSummaryDto;
import com.sekolah.sisteminformasi.dto.attendance.BatchAttendanceRequestDto;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceService {
    List<AttendanceResponseDto> recordBatchAttendance(BatchAttendanceRequestDto batchRequest);
    List<AttendanceResponseDto> getAttendanceByScheduleAndDate(Long scheduleId, LocalDate date);
    List<AttendanceResponseDto> getAttendanceByStudentId(Long studentId);
    AttendanceSummaryDto getStudentAttendanceSummary(Long studentId, String academicYear, Integer semester);
}
