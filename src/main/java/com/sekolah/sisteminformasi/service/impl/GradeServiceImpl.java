package com.sekolah.sisteminformasi.service.impl;

import com.sekolah.sisteminformasi.dto.grade.GradeRequestDto;
import com.sekolah.sisteminformasi.dto.grade.GradeResponseDto;
import com.sekolah.sisteminformasi.entity.*;
import com.sekolah.sisteminformasi.exception.BadRequestException;
import com.sekolah.sisteminformasi.exception.ResourceNotFoundException;
import com.sekolah.sisteminformasi.mapper.GradeMapper;
import com.sekolah.sisteminformasi.repository.*;
import com.sekolah.sisteminformasi.service.GradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GradeServiceImpl implements GradeService {

    private final GradeRepository gradeRepository;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final TeacherRepository teacherRepository;

    @Override
    @Transactional
    public GradeResponseDto inputOrUpdateGrade(GradeRequestDto dto) {
        Student student = studentRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Siswa", "id", dto.getStudentId()));

        Subject subject = subjectRepository.findById(dto.getSubjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Mata Pelajaran", "id", dto.getSubjectId()));

        Teacher teacher = teacherRepository.findById(dto.getTeacherId())
                .orElseThrow(() -> new ResourceNotFoundException("Guru", "id", dto.getTeacherId()));

        // Upsert logic: Cek apakah nilai untuk siswa + mapel + semester ini sudah ada
        Optional<Grade> existingGrade = gradeRepository.findByStudentIdAndSubjectIdAndAcademicYearAndSemester(
                student.getId(),
                subject.getId(),
                dto.getAcademicYear(),
                dto.getSemester()
        );

        Grade grade;
        if (existingGrade.isPresent()) {
            grade = existingGrade.get();
            if (dto.getMidtermScore() != null) grade.setMidtermScore(dto.getMidtermScore());
            if (dto.getFinalScore() != null) grade.setFinalScore(dto.getFinalScore());
            grade.setTeacher(teacher);
        } else {
            grade = Grade.builder()
                    .student(student)
                    .subject(subject)
                    .teacher(teacher)
                    .classroom(student.getClassroom())
                    .academicYear(dto.getAcademicYear())
                    .semester(dto.getSemester())
                    .midtermScore(dto.getMidtermScore())
                    .finalScore(dto.getFinalScore())
                    .build();
        }

        Grade savedGrade = gradeRepository.save(grade);
        return GradeMapper.mapToGradeResponseDto(savedGrade);
    }

    @Override
    public GradeResponseDto getGradeById(Long id) {
        Grade grade = gradeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Nilai", "id", id));
        return GradeMapper.mapToGradeResponseDto(grade);
    }

    @Override
    public List<GradeResponseDto> getGradesByStudent(Long studentId, String academicYear, Integer semester) {
        return gradeRepository.findByStudentIdAndAcademicYearAndSemester(studentId, academicYear, semester).stream()
                .map(GradeMapper::mapToGradeResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<GradeResponseDto> getGradesByClassroomAndSubject(Long classroomId, Long subjectId, String academicYear, Integer semester) {
        return gradeRepository.findByClassroomIdAndSubjectIdAndAcademicYearAndSemester(classroomId, subjectId, academicYear, semester).stream()
                .map(GradeMapper::mapToGradeResponseDto)
                .collect(Collectors.toList());
    }
}
