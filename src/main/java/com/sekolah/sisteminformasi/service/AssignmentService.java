package com.sekolah.sisteminformasi.service;

import com.sekolah.sisteminformasi.dto.assignment.AssignmentRequestDto;
import com.sekolah.sisteminformasi.dto.assignment.AssignmentResponseDto;
import com.sekolah.sisteminformasi.dto.assignment.AssignmentScoreRequestDto;
import com.sekolah.sisteminformasi.dto.assignment.AssignmentScoreResponseDto;

import java.util.List;

public interface AssignmentService {
    AssignmentResponseDto createAssignment(AssignmentRequestDto assignmentRequestDto);
    AssignmentResponseDto getAssignmentById(Long id);
    List<AssignmentResponseDto> getAssignmentsByClassroomAndSubject(Long classroomId, Long subjectId);
    List<AssignmentResponseDto> getAssignmentsByTeacherId(Long teacherId);
    void deleteAssignment(Long id);

    AssignmentScoreResponseDto inputAssignmentScore(Long assignmentId, AssignmentScoreRequestDto scoreRequestDto);
    List<AssignmentScoreResponseDto> getScoresByAssignmentId(Long assignmentId);
    List<AssignmentScoreResponseDto> getScoresByStudentId(Long studentId);
    Double getStudentAssignmentAverage(Long studentId, Long subjectId, String academicYear, Integer semester);
}
