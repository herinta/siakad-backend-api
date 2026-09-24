package com.sekolah.sisteminformasi.mapper;

import com.sekolah.sisteminformasi.dto.teacher.TeacherResponseDto;
import com.sekolah.sisteminformasi.entity.Teacher;

public class TeacherMapper {

    public static TeacherResponseDto mapToTeacherResponseDto(Teacher teacher) {
        if (teacher == null) return null;

        return TeacherResponseDto.builder()
                .id(teacher.getId())
                .nip(teacher.getNip())
                .fullName(teacher.getFullName())
                .email(teacher.getEmail())
                .phone(teacher.getPhone())
                .gender(teacher.getGender())
                .address(teacher.getAddress())
                .subject(SubjectMapper.mapToSubjectDto(teacher.getSubject()))
                .userId(teacher.getUser() != null ? teacher.getUser().getId() : null)
                .username(teacher.getUser() != null ? teacher.getUser().getUsername() : null)
                .isActive(teacher.getIsActive())
                .build();
    }
}
