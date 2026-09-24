package com.sekolah.sisteminformasi.dto.attendance;

import com.sekolah.sisteminformasi.dto.student.StudentResponseDto;
import com.sekolah.sisteminformasi.entity.AttendanceStatus;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceResponseDto {

    private Long id;
    private Long scheduleId;
    private String subjectName;
    private String classroomName;
    private StudentResponseDto student;
    private LocalDate attendanceDate;
    private AttendanceStatus status;
    private String remarks;
}
