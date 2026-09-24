package com.sekolah.sisteminformasi.service;

import com.sekolah.sisteminformasi.dto.student.StudentRequestDto;
import com.sekolah.sisteminformasi.dto.student.StudentResponseDto;

import java.util.List;

public interface StudentService {
    StudentResponseDto createStudent(StudentRequestDto studentRequestDto);
    StudentResponseDto getStudentById(Long id);
    StudentResponseDto getStudentByNisn(String nisn);
    List<StudentResponseDto> getAllStudents();
    List<StudentResponseDto> getStudentsByClassroomId(Long classroomId);
    StudentResponseDto updateStudent(Long id, StudentRequestDto studentRequestDto);
    void deleteStudent(Long id);
}
