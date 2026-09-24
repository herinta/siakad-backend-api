package com.sekolah.sisteminformasi.dto.assignment;

import com.sekolah.sisteminformasi.dto.classroom.ClassroomDto;
import com.sekolah.sisteminformasi.dto.subject.SubjectDto;
import com.sekolah.sisteminformasi.dto.teacher.TeacherResponseDto;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssignmentResponseDto {

    private Long id;
    private String title;
    private String description;
    private LocalDate dueDate;
    private SubjectDto subject;
    private ClassroomDto classroom;
    private TeacherResponseDto teacher;
    private String academicYear;
    private Integer semester;
    private LocalDateTime createdAt;
}
