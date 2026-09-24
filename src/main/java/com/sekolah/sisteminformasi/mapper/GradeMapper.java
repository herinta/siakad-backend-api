package com.sekolah.sisteminformasi.mapper;

import com.sekolah.sisteminformasi.dto.grade.GradeResponseDto;
import com.sekolah.sisteminformasi.entity.Grade;

public class GradeMapper {

    public static GradeResponseDto mapToGradeResponseDto(Grade grade) {
        if (grade == null) return null;

        return GradeResponseDto.builder()
                .id(grade.getId())
                .student(StudentMapper.mapToStudentResponseDto(grade.getStudent()))
                .subject(SubjectMapper.mapToSubjectDto(grade.getSubject()))
                .teacher(TeacherMapper.mapToTeacherResponseDto(grade.getTeacher()))
                .classroom(ClassroomMapper.mapToClassroomDto(grade.getClassroom()))
                .academicYear(grade.getAcademicYear())
                .semester(grade.getSemester())
                .midtermScore(grade.getMidtermScore())
                .finalScore(grade.getFinalScore())
                .updatedAt(grade.getUpdatedAt())
                .build();
    }
}
