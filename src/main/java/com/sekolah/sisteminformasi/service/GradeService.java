package com.sekolah.sisteminformasi.service;

import com.sekolah.sisteminformasi.dto.grade.GradeRequestDto;
import com.sekolah.sisteminformasi.dto.grade.GradeResponseDto;

import java.util.List;

public interface GradeService {
    GradeResponseDto inputOrUpdateGrade(GradeRequestDto gradeRequestDto);
    GradeResponseDto getGradeById(Long id);
    List<GradeResponseDto> getGradesByStudent(Long studentId, String academicYear, Integer semester);
    List<GradeResponseDto> getGradesByClassroomAndSubject(Long classroomId, Long subjectId, String academicYear, Integer semester);
}
