package com.sekolah.sisteminformasi.mapper;

import com.sekolah.sisteminformasi.dto.attendance.AttendanceResponseDto;
import com.sekolah.sisteminformasi.entity.Attendance;

public class AttendanceMapper {

    public static AttendanceResponseDto mapToAttendanceResponseDto(Attendance attendance) {
        if (attendance == null) return null;

        return AttendanceResponseDto.builder()
                .id(attendance.getId())
                .scheduleId(attendance.getSchedule().getId())
                .subjectName(attendance.getSchedule().getSubject().getName())
                .classroomName(attendance.getSchedule().getClassroom().getName())
                .student(StudentMapper.mapToStudentResponseDto(attendance.getStudent()))
                .attendanceDate(attendance.getAttendanceDate())
                .status(attendance.getStatus())
                .remarks(attendance.getRemarks())
                .build();
    }
}
