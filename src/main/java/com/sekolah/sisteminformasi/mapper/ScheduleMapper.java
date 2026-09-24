package com.sekolah.sisteminformasi.mapper;

import com.sekolah.sisteminformasi.dto.schedule.ScheduleResponseDto;
import com.sekolah.sisteminformasi.entity.Schedule;

public class ScheduleMapper {

    public static ScheduleResponseDto mapToScheduleResponseDto(Schedule schedule) {
        if (schedule == null) return null;

        return ScheduleResponseDto.builder()
                .id(schedule.getId())
                .teacher(TeacherMapper.mapToTeacherResponseDto(schedule.getTeacher()))
                .subject(SubjectMapper.mapToSubjectDto(schedule.getSubject()))
                .classroom(ClassroomMapper.mapToClassroomDto(schedule.getClassroom()))
                .dayOfWeek(schedule.getDayOfWeek())
                .startTime(schedule.getStartTime())
                .endTime(schedule.getEndTime())
                .academicYear(schedule.getAcademicYear())
                .semester(schedule.getSemester())
                .build();
    }
}
