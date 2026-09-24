package com.sekolah.sisteminformasi.mapper;

import com.sekolah.sisteminformasi.dto.subject.SubjectDto;
import com.sekolah.sisteminformasi.entity.Subject;

public class SubjectMapper {

    public static SubjectDto mapToSubjectDto(Subject subject) {
        if (subject == null) return null;
        return SubjectDto.builder()
                .id(subject.getId())
                .code(subject.getCode())
                .name(subject.getName())
                .description(subject.getDescription())
                .creditHours(subject.getCreditHours())
                .build();
    }

    public static Subject mapToSubject(SubjectDto subjectDto) {
        if (subjectDto == null) return null;
        return Subject.builder()
                .id(subjectDto.getId())
                .code(subjectDto.getCode())
                .name(subjectDto.getName())
                .description(subjectDto.getDescription())
                .creditHours(subjectDto.getCreditHours())
                .build();
    }
}
