package com.sekolah.sisteminformasi.service.impl;

import com.sekolah.sisteminformasi.dto.student.StudentRequestDto;
import com.sekolah.sisteminformasi.dto.student.StudentResponseDto;
import com.sekolah.sisteminformasi.entity.Classroom;
import com.sekolah.sisteminformasi.entity.Student;
import com.sekolah.sisteminformasi.entity.StudentStatus;
import com.sekolah.sisteminformasi.exception.BadRequestException;
import com.sekolah.sisteminformasi.exception.ResourceNotFoundException;
import com.sekolah.sisteminformasi.mapper.StudentMapper;
import com.sekolah.sisteminformasi.repository.ClassroomRepository;
import com.sekolah.sisteminformasi.repository.StudentRepository;
import com.sekolah.sisteminformasi.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final ClassroomRepository classroomRepository;

    @Override
    @Transactional
    public StudentResponseDto createStudent(StudentRequestDto studentRequestDto) {
        if (studentRepository.existsByNisn(studentRequestDto.getNisn())) {
            throw new BadRequestException("NISN '" + studentRequestDto.getNisn() + "' sudah terdaftar!");
        }

        if (studentRepository.existsByNis(studentRequestDto.getNis())) {
            throw new BadRequestException("NIS '" + studentRequestDto.getNis() + "' sudah terdaftar!");
        }

        Classroom classroom = classroomRepository.findById(studentRequestDto.getClassroomId())
                .orElseThrow(() -> new ResourceNotFoundException("Kelas", "id", studentRequestDto.getClassroomId()));

        Student student = Student.builder()
                .nisn(studentRequestDto.getNisn())
                .nis(studentRequestDto.getNis())
                .fullName(studentRequestDto.getFullName())
                .gender(studentRequestDto.getGender())
                .birthPlace(studentRequestDto.getBirthPlace())
                .birthDate(studentRequestDto.getBirthDate())
                .religion(studentRequestDto.getReligion())
                .address(studentRequestDto.getAddress())
                .parentName(studentRequestDto.getParentName())
                .parentPhone(studentRequestDto.getParentPhone())
                .classroom(classroom)
                .admissionYear(studentRequestDto.getAdmissionYear())
                .status(studentRequestDto.getStatus() != null ? studentRequestDto.getStatus() : StudentStatus.ACTIVE)
                .build();

        Student savedStudent = studentRepository.save(student);
        return StudentMapper.mapToStudentResponseDto(savedStudent);
    }

    @Override
    public StudentResponseDto getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Siswa", "id", id));
        return StudentMapper.mapToStudentResponseDto(student);
    }

    @Override
    public StudentResponseDto getStudentByNisn(String nisn) {
        Student student = studentRepository.findByNisn(nisn)
                .orElseThrow(() -> new ResourceNotFoundException("Siswa", "NISN", nisn));
        return StudentMapper.mapToStudentResponseDto(student);
    }

    @Override
    public List<StudentResponseDto> getAllStudents() {
        return studentRepository.findAll().stream()
                .map(StudentMapper::mapToStudentResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<StudentResponseDto> getStudentsByClassroomId(Long classroomId) {
        return studentRepository.findByClassroomId(classroomId).stream()
                .map(StudentMapper::mapToStudentResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public StudentResponseDto updateStudent(Long id, StudentRequestDto studentRequestDto) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Siswa", "id", id));

        if (!student.getNisn().equalsIgnoreCase(studentRequestDto.getNisn()) && studentRepository.existsByNisn(studentRequestDto.getNisn())) {
            throw new BadRequestException("NISN '" + studentRequestDto.getNisn() + "' sudah digunakan oleh siswa lain!");
        }

        if (!student.getNis().equalsIgnoreCase(studentRequestDto.getNis()) && studentRepository.existsByNis(studentRequestDto.getNis())) {
            throw new BadRequestException("NIS '" + studentRequestDto.getNis() + "' sudah digunakan oleh siswa lain!");
        }

        Classroom classroom = classroomRepository.findById(studentRequestDto.getClassroomId())
                .orElseThrow(() -> new ResourceNotFoundException("Kelas", "id", studentRequestDto.getClassroomId()));

        student.setNisn(studentRequestDto.getNisn());
        student.setNis(studentRequestDto.getNis());
        student.setFullName(studentRequestDto.getFullName());
        student.setGender(studentRequestDto.getGender());
        student.setBirthPlace(studentRequestDto.getBirthPlace());
        student.setBirthDate(studentRequestDto.getBirthDate());
        student.setReligion(studentRequestDto.getReligion());
        student.setAddress(studentRequestDto.getAddress());
        student.setParentName(studentRequestDto.getParentName());
        student.setParentPhone(studentRequestDto.getParentPhone());
        student.setClassroom(classroom);
        student.setAdmissionYear(studentRequestDto.getAdmissionYear());
        if (studentRequestDto.getStatus() != null) {
            student.setStatus(studentRequestDto.getStatus());
        }

        Student updatedStudent = studentRepository.save(student);
        return StudentMapper.mapToStudentResponseDto(updatedStudent);
    }

    @Override
    @Transactional
    public void deleteStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Siswa", "id", id));
        student.setStatus(StudentStatus.DROPOUT); // Soft delete status
        studentRepository.save(student);
    }
}
