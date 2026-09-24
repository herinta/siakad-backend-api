package com.sekolah.sisteminformasi.service.impl;

import com.sekolah.sisteminformasi.dto.teacher.TeacherRequestDto;
import com.sekolah.sisteminformasi.dto.teacher.TeacherResponseDto;
import com.sekolah.sisteminformasi.entity.Subject;
import com.sekolah.sisteminformasi.entity.Teacher;
import com.sekolah.sisteminformasi.entity.User;
import com.sekolah.sisteminformasi.exception.BadRequestException;
import com.sekolah.sisteminformasi.exception.ResourceNotFoundException;
import com.sekolah.sisteminformasi.mapper.TeacherMapper;
import com.sekolah.sisteminformasi.repository.SubjectRepository;
import com.sekolah.sisteminformasi.repository.TeacherRepository;
import com.sekolah.sisteminformasi.repository.UserRepository;
import com.sekolah.sisteminformasi.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;
    private final SubjectRepository subjectRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public TeacherResponseDto createTeacher(TeacherRequestDto teacherRequestDto) {
        if (teacherRepository.existsByNip(teacherRequestDto.getNip())) {
            throw new BadRequestException("NIP '" + teacherRequestDto.getNip() + "' sudah terdaftar!");
        }

        if (teacherRepository.existsByEmail(teacherRequestDto.getEmail())) {
            throw new BadRequestException("Email guru '" + teacherRequestDto.getEmail() + "' sudah terdaftar!");
        }

        Subject subject = null;
        if (teacherRequestDto.getSubjectId() != null) {
            subject = subjectRepository.findById(teacherRequestDto.getSubjectId())
                    .orElseThrow(() -> new ResourceNotFoundException("Mata Pelajaran", "id", teacherRequestDto.getSubjectId()));
        }

        User user = null;
        if (teacherRequestDto.getUserId() != null) {
            user = userRepository.findById(teacherRequestDto.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", teacherRequestDto.getUserId()));
        }

        Teacher teacher = Teacher.builder()
                .nip(teacherRequestDto.getNip())
                .fullName(teacherRequestDto.getFullName())
                .email(teacherRequestDto.getEmail())
                .phone(teacherRequestDto.getPhone())
                .gender(teacherRequestDto.getGender())
                .address(teacherRequestDto.getAddress())
                .subject(subject)
                .user(user)
                .isActive(true)
                .build();

        Teacher savedTeacher = teacherRepository.save(teacher);
        return TeacherMapper.mapToTeacherResponseDto(savedTeacher);
    }

    @Override
    public TeacherResponseDto getTeacherById(Long id) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Guru", "id", id));
        return TeacherMapper.mapToTeacherResponseDto(teacher);
    }

    @Override
    public TeacherResponseDto getTeacherByUserId(Long userId) {
        Teacher teacher = teacherRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Profil Guru untuk Akun User ini", "userId", userId));
        return TeacherMapper.mapToTeacherResponseDto(teacher);
    }

    @Override
    public List<TeacherResponseDto> getAllTeachers() {
        return teacherRepository.findAll().stream()
                .map(TeacherMapper::mapToTeacherResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TeacherResponseDto updateTeacher(Long id, TeacherRequestDto teacherRequestDto) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Guru", "id", id));

        if (!teacher.getNip().equalsIgnoreCase(teacherRequestDto.getNip()) && teacherRepository.existsByNip(teacherRequestDto.getNip())) {
            throw new BadRequestException("NIP '" + teacherRequestDto.getNip() + "' sudah digunakan oleh guru lain!");
        }

        if (!teacher.getEmail().equalsIgnoreCase(teacherRequestDto.getEmail()) && teacherRepository.existsByEmail(teacherRequestDto.getEmail())) {
            throw new BadRequestException("Email '" + teacherRequestDto.getEmail() + "' sudah digunakan oleh guru lain!");
        }

        if (teacherRequestDto.getSubjectId() != null) {
            Subject subject = subjectRepository.findById(teacherRequestDto.getSubjectId())
                    .orElseThrow(() -> new ResourceNotFoundException("Mata Pelajaran", "id", teacherRequestDto.getSubjectId()));
            teacher.setSubject(subject);
        }

        if (teacherRequestDto.getUserId() != null) {
            User user = userRepository.findById(teacherRequestDto.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", teacherRequestDto.getUserId()));
            teacher.setUser(user);
        }

        teacher.setNip(teacherRequestDto.getNip());
        teacher.setFullName(teacherRequestDto.getFullName());
        teacher.setEmail(teacherRequestDto.getEmail());
        teacher.setPhone(teacherRequestDto.getPhone());
        teacher.setGender(teacherRequestDto.getGender());
        teacher.setAddress(teacherRequestDto.getAddress());

        Teacher updatedTeacher = teacherRepository.save(teacher);
        return TeacherMapper.mapToTeacherResponseDto(updatedTeacher);
    }

    @Override
    @Transactional
    public void deleteTeacher(Long id) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Guru", "id", id));
        // Soft delete / nonaktifkan
        teacher.setIsActive(false);
        teacherRepository.save(teacher);
    }
}
