package com.sekolah.sisteminformasi.dto.schedule;

import com.sekolah.sisteminformasi.dto.classroom.ClassroomDto;
import com.sekolah.sisteminformasi.dto.subject.SubjectDto;
import com.sekolah.sisteminformasi.dto.teacher.TeacherResponseDto;
import com.sekolah.sisteminformasi.entity.DayOfWeekEnum;
import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleResponseDto {

    private Long id;
    private TeacherResponseDto teacher;
    private SubjectDto subject;
    private ClassroomDto classroom;
    private DayOfWeekEnum dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private String academicYear;
    private Integer semester;
}
