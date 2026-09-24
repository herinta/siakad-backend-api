package com.sekolah.sisteminformasi.service;

import com.sekolah.sisteminformasi.dto.subject.SubjectDto;

import java.util.List;

public interface SubjectService {
    SubjectDto createSubject(SubjectDto subjectDto);
    SubjectDto getSubjectById(Long id);
    List<SubjectDto> getAllSubjects();
    SubjectDto updateSubject(Long id, SubjectDto subjectDto);
    void deleteSubject(Long id);
}
