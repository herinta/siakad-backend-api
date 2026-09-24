package com.sekolah.sisteminformasi.dto.grade;

import com.sekolah.sisteminformasi.dto.classroom.ClassroomDto;
import com.sekolah.sisteminformasi.dto.student.StudentResponseDto;
import com.sekolah.sisteminformasi.dto.subject.SubjectDto;
import com.sekolah.sisteminformasi.dto.teacher.TeacherResponseDto;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GradeResponseDto {

    private Long id;
    private StudentResponseDto student;
    private SubjectDto subject;
    private TeacherResponseDto teacher;
    private ClassroomDto classroom;
    private String academicYear;
    private Integer semester;
    private Double midtermScore;
    private Double finalScore;
    private LocalDateTime updatedAt;
}
