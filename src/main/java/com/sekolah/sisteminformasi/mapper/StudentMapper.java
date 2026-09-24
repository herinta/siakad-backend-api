package com.sekolah.sisteminformasi.mapper;

import com.sekolah.sisteminformasi.dto.student.StudentResponseDto;
import com.sekolah.sisteminformasi.entity.Student;

public class StudentMapper {

    public static StudentResponseDto mapToStudentResponseDto(Student student) {
        if (student == null) return null;

        return StudentResponseDto.builder()
                .id(student.getId())
                .nisn(student.getNisn())
                .nis(student.getNis())
                .fullName(student.getFullName())
                .gender(student.getGender())
                .birthPlace(student.getBirthPlace())
                .birthDate(student.getBirthDate())
                .religion(student.getReligion())
                .address(student.getAddress())
                .parentName(student.getParentName())
                .parentPhone(student.getParentPhone())
                .classroom(ClassroomMapper.mapToClassroomDto(student.getClassroom()))
                .admissionYear(student.getAdmissionYear())
                .status(student.getStatus())
                .build();
    }
}
