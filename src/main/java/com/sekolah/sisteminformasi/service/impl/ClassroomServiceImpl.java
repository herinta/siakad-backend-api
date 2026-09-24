package com.sekolah.sisteminformasi.service.impl;

import com.sekolah.sisteminformasi.dto.classroom.ClassroomDto;
import com.sekolah.sisteminformasi.entity.Classroom;
import com.sekolah.sisteminformasi.exception.BadRequestException;
import com.sekolah.sisteminformasi.exception.ResourceNotFoundException;
import com.sekolah.sisteminformasi.mapper.ClassroomMapper;
import com.sekolah.sisteminformasi.repository.ClassroomRepository;
import com.sekolah.sisteminformasi.service.ClassroomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClassroomServiceImpl implements ClassroomService {

    private final ClassroomRepository classroomRepository;

    @Override
    @Transactional
    public ClassroomDto createClassroom(ClassroomDto classroomDto) {
        if (classroomRepository.existsByCode(classroomDto.getCode())) {
            throw new BadRequestException("Kode kelas '" + classroomDto.getCode() + "' sudah ada!");
        }

        Classroom classroom = ClassroomMapper.mapToClassroom(classroomDto);
        Classroom savedClassroom = classroomRepository.save(classroom);
        return ClassroomMapper.mapToClassroomDto(savedClassroom);
    }

    @Override
    public ClassroomDto getClassroomById(Long id) {
        Classroom classroom = classroomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Kelas", "id", id));
        return ClassroomMapper.mapToClassroomDto(classroom);
    }

    @Override
    public List<ClassroomDto> getAllClassrooms() {
        return classroomRepository.findAll().stream()
                .map(ClassroomMapper::mapToClassroomDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ClassroomDto> getClassroomsByGradeLevel(Integer gradeLevel) {
        return classroomRepository.findByGradeLevel(gradeLevel).stream()
                .map(ClassroomMapper::mapToClassroomDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ClassroomDto updateClassroom(Long id, ClassroomDto classroomDto) {
        Classroom classroom = classroomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Kelas", "id", id));

        if (!classroom.getCode().equalsIgnoreCase(classroomDto.getCode()) && classroomRepository.existsByCode(classroomDto.getCode())) {
            throw new BadRequestException("Kode kelas '" + classroomDto.getCode() + "' sudah digunakan!");
        }

        classroom.setCode(classroomDto.getCode());
        classroom.setName(classroomDto.getName());
        classroom.setGradeLevel(classroomDto.getGradeLevel());
        classroom.setAcademicYear(classroomDto.getAcademicYear());

        Classroom updatedClassroom = classroomRepository.save(classroom);
        return ClassroomMapper.mapToClassroomDto(updatedClassroom);
    }

    @Override
    @Transactional
    public void deleteClassroom(Long id) {
        Classroom classroom = classroomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Kelas", "id", id));
        classroomRepository.delete(classroom);
    }
}
