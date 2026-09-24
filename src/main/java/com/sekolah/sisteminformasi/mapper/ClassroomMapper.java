package com.sekolah.sisteminformasi.mapper;

import com.sekolah.sisteminformasi.dto.classroom.ClassroomDto;
import com.sekolah.sisteminformasi.entity.Classroom;

public class ClassroomMapper {

    public static ClassroomDto mapToClassroomDto(Classroom classroom) {
        if (classroom == null) return null;
        return ClassroomDto.builder()
                .id(classroom.getId())
                .code(classroom.getCode())
                .name(classroom.getName())
                .gradeLevel(classroom.getGradeLevel())
                .academicYear(classroom.getAcademicYear())
                .build();
    }

    public static Classroom mapToClassroom(ClassroomDto classroomDto) {
        if (classroomDto == null) return null;
        return Classroom.builder()
                .id(classroomDto.getId())
                .code(classroomDto.getCode())
                .name(classroomDto.getName())
                .gradeLevel(classroomDto.getGradeLevel())
                .academicYear(classroomDto.getAcademicYear())
                .build();
    }
}
