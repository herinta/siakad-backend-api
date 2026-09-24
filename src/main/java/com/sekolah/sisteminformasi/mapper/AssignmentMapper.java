package com.sekolah.sisteminformasi.mapper;

import com.sekolah.sisteminformasi.dto.assignment.AssignmentResponseDto;
import com.sekolah.sisteminformasi.dto.assignment.AssignmentScoreResponseDto;
import com.sekolah.sisteminformasi.entity.Assignment;
import com.sekolah.sisteminformasi.entity.AssignmentScore;

public class AssignmentMapper {

    public static AssignmentResponseDto mapToAssignmentResponseDto(Assignment assignment) {
        if (assignment == null) return null;

        return AssignmentResponseDto.builder()
                .id(assignment.getId())
                .title(assignment.getTitle())
                .description(assignment.getDescription())
                .dueDate(assignment.getDueDate())
                .subject(SubjectMapper.mapToSubjectDto(assignment.getSubject()))
                .classroom(ClassroomMapper.mapToClassroomDto(assignment.getClassroom()))
                .teacher(TeacherMapper.mapToTeacherResponseDto(assignment.getTeacher()))
                .academicYear(assignment.getAcademicYear())
                .semester(assignment.getSemester())
                .createdAt(assignment.getCreatedAt())
                .build();
    }

    public static AssignmentScoreResponseDto mapToAssignmentScoreResponseDto(AssignmentScore score) {
        if (score == null) return null;

        return AssignmentScoreResponseDto.builder()
                .id(score.getId())
                .assignmentId(score.getAssignment().getId())
                .assignmentTitle(score.getAssignment().getTitle())
                .student(StudentMapper.mapToStudentResponseDto(score.getStudent()))
                .score(score.getScore())
                .feedback(score.getFeedback())
                .updatedAt(score.getUpdatedAt())
                .build();
    }
}
