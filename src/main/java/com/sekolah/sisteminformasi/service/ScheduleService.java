package com.sekolah.sisteminformasi.service;

import com.sekolah.sisteminformasi.dto.schedule.ScheduleRequestDto;
import com.sekolah.sisteminformasi.dto.schedule.ScheduleResponseDto;
import com.sekolah.sisteminformasi.entity.DayOfWeekEnum;

import java.util.List;

public interface ScheduleService {
    ScheduleResponseDto createSchedule(ScheduleRequestDto scheduleRequestDto);
    ScheduleResponseDto getScheduleById(Long id);
    List<ScheduleResponseDto> getAllSchedules();
    List<ScheduleResponseDto> getSchedulesByTeacherId(Long teacherId);
    List<ScheduleResponseDto> getSchedulesByClassroomId(Long classroomId);
    List<ScheduleResponseDto> getSchedulesByDay(DayOfWeekEnum dayOfWeek);
    List<ScheduleResponseDto> getMyTeacherSchedule(String username);
    ScheduleResponseDto updateSchedule(Long id, ScheduleRequestDto scheduleRequestDto);
    void deleteSchedule(Long id);
}
