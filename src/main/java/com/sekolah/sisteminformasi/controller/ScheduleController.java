package com.sekolah.sisteminformasi.controller;

import com.sekolah.sisteminformasi.dto.common.ApiResponse;
import com.sekolah.sisteminformasi.dto.schedule.ScheduleRequestDto;
import com.sekolah.sisteminformasi.dto.schedule.ScheduleResponseDto;
import com.sekolah.sisteminformasi.entity.DayOfWeekEnum;
import com.sekolah.sisteminformasi.service.ScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ScheduleResponseDto>> createSchedule(@Valid @RequestBody ScheduleRequestDto scheduleRequestDto) {
        ScheduleResponseDto createdSchedule = scheduleService.createSchedule(scheduleRequestDto);
        return new ResponseEntity<>(
                ApiResponse.created("Jadwal pelajaran berhasil dibuat!", createdSchedule),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<ApiResponse<ScheduleResponseDto>> getScheduleById(@PathVariable Long id) {
        ScheduleResponseDto schedule = scheduleService.getScheduleById(id);
        return ResponseEntity.ok(ApiResponse.success("Detail jadwal berhasil diambil", schedule));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<ApiResponse<List<ScheduleResponseDto>>> getAllSchedules(
            @RequestParam(required = false) Long teacherId,
            @RequestParam(required = false) Long classroomId,
            @RequestParam(required = false) DayOfWeekEnum dayOfWeek) {

        List<ScheduleResponseDto> schedules;
        if (teacherId != null) {
            schedules = scheduleService.getSchedulesByTeacherId(teacherId);
        } else if (classroomId != null) {
            schedules = scheduleService.getSchedulesByClassroomId(classroomId);
        } else if (dayOfWeek != null) {
            schedules = scheduleService.getSchedulesByDay(dayOfWeek);
        } else {
            schedules = scheduleService.getAllSchedules();
        }

        return ResponseEntity.ok(ApiResponse.success("Daftar jadwal pelajaran berhasil diambil", schedules));
    }

    @GetMapping("/my-schedule")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<ApiResponse<List<ScheduleResponseDto>>> getMySchedule(Authentication authentication) {
        String username = authentication.getName();
        List<ScheduleResponseDto> schedules = scheduleService.getMyTeacherSchedule(username);
        return ResponseEntity.ok(ApiResponse.success("Jadwal mengajar saya berhasil diambil", schedules));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ScheduleResponseDto>> updateSchedule(
            @PathVariable Long id,
            @Valid @RequestBody ScheduleRequestDto scheduleRequestDto) {
        ScheduleResponseDto updatedSchedule = scheduleService.updateSchedule(id, scheduleRequestDto);
        return ResponseEntity.ok(ApiResponse.success("Jadwal pelajaran berhasil diperbarui!", updatedSchedule));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteSchedule(@PathVariable Long id) {
        scheduleService.deleteSchedule(id);
        return ResponseEntity.ok(ApiResponse.success("Jadwal pelajaran berhasil dihapus!", null));
    }
}
