package com.sekolah.sisteminformasi.service.impl;

import com.sekolah.sisteminformasi.dto.assignment.AssignmentRequestDto;
import com.sekolah.sisteminformasi.dto.assignment.AssignmentResponseDto;
import com.sekolah.sisteminformasi.dto.assignment.AssignmentScoreRequestDto;
import com.sekolah.sisteminformasi.dto.assignment.AssignmentScoreResponseDto;
import com.sekolah.sisteminformasi.entity.*;
import com.sekolah.sisteminformasi.exception.BadRequestException;
import com.sekolah.sisteminformasi.exception.ResourceNotFoundException;
import com.sekolah.sisteminformasi.mapper.AssignmentMapper;
import com.sekolah.sisteminformasi.repository.*;
import com.sekolah.sisteminformasi.service.AssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssignmentServiceImpl implements AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final AssignmentScoreRepository assignmentScoreRepository;
    private final SubjectRepository subjectRepository;
    private final ClassroomRepository classroomRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;

    @Override
    @Transactional
    public AssignmentResponseDto createAssignment(AssignmentRequestDto dto) {
        Subject subject = subjectRepository.findById(dto.getSubjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Mata Pelajaran", "id", dto.getSubjectId()));

        Classroom classroom = classroomRepository.findById(dto.getClassroomId())
                .orElseThrow(() -> new ResourceNotFoundException("Kelas", "id", dto.getClassroomId()));

        Teacher teacher = teacherRepository.findById(dto.getTeacherId())
                .orElseThrow(() -> new ResourceNotFoundException("Guru", "id", dto.getTeacherId()));

        Assignment assignment = Assignment.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .dueDate(dto.getDueDate())
                .subject(subject)
                .classroom(classroom)
                .teacher(teacher)
                .academicYear(dto.getAcademicYear())
                .semester(dto.getSemester())
                .build();

        Assignment savedAssignment = assignmentRepository.save(assignment);
        return AssignmentMapper.mapToAssignmentResponseDto(savedAssignment);
    }

    @Override
    public AssignmentResponseDto getAssignmentById(Long id) {
        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tugas", "id", id));
        return AssignmentMapper.mapToAssignmentResponseDto(assignment);
    }

    @Override
    public List<AssignmentResponseDto> getAssignmentsByClassroomAndSubject(Long classroomId, Long subjectId) {
        return assignmentRepository.findByClassroomIdAndSubjectId(classroomId, subjectId).stream()
                .map(AssignmentMapper::mapToAssignmentResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AssignmentResponseDto> getAssignmentsByTeacherId(Long teacherId) {
        return assignmentRepository.findByTeacherId(teacherId).stream()
                .map(AssignmentMapper::mapToAssignmentResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteAssignment(Long id) {
        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tugas", "id", id));
        assignmentRepository.delete(assignment);
    }

    @Override
    @Transactional
    public AssignmentScoreResponseDto inputAssignmentScore(Long assignmentId, AssignmentScoreRequestDto scoreRequestDto) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Tugas", "id", assignmentId));

        Student student = studentRepository.findById(scoreRequestDto.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Siswa", "id", scoreRequestDto.getStudentId()));

        // Validasi: Siswa harus terdaftar di kelas tugas ini
        if (!student.getClassroom().getId().equals(assignment.getClassroom().getId())) {
            throw new BadRequestException(String.format(
                    "Siswa '%s' terdaftar di kelas '%s', bukan di kelas tugas ini ('%s')!",
                    student.getFullName(),
                    student.getClassroom().getName(),
                    assignment.getClassroom().getName()
            ));
        }

        // Cek apakah sudah ada nilai sebelumnya (Insert atau Update)
        Optional<AssignmentScore> existingScore = assignmentScoreRepository
                .findByAssignmentIdAndStudentId(assignmentId, student.getId());

        AssignmentScore scoreEntity;
        if (existingScore.isPresent()) {
            scoreEntity = existingScore.get();
            scoreEntity.setScore(scoreRequestDto.getScore());
            scoreEntity.setFeedback(scoreRequestDto.getFeedback());
        } else {
            scoreEntity = AssignmentScore.builder()
                    .assignment(assignment)
                    .student(student)
                    .score(scoreRequestDto.getScore())
                    .feedback(scoreRequestDto.getFeedback())
                    .build();
        }

        AssignmentScore savedScore = assignmentScoreRepository.save(scoreEntity);
        return AssignmentMapper.mapToAssignmentScoreResponseDto(savedScore);
    }

    @Override
    public List<AssignmentScoreResponseDto> getScoresByAssignmentId(Long assignmentId) {
        return assignmentScoreRepository.findByAssignmentId(assignmentId).stream()
                .map(AssignmentMapper::mapToAssignmentScoreResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AssignmentScoreResponseDto> getScoresByStudentId(Long studentId) {
        return assignmentScoreRepository.findByStudentId(studentId).stream()
                .map(AssignmentMapper::mapToAssignmentScoreResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public Double getStudentAssignmentAverage(Long studentId, Long subjectId, String academicYear, Integer semester) {
        Double avg = assignmentScoreRepository.getAverageScoreByStudentAndSubject(studentId, subjectId, academicYear, semester);
        return avg != null ? Math.round(avg * 100.0) / 100.0 : 0.0;
    }
}
