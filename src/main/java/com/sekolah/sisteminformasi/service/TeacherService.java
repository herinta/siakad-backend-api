package com.sekolah.sisteminformasi.service;

import com.sekolah.sisteminformasi.dto.teacher.TeacherRequestDto;
import com.sekolah.sisteminformasi.dto.teacher.TeacherResponseDto;

import java.util.List;

public interface TeacherService {
    TeacherResponseDto createTeacher(TeacherRequestDto teacherRequestDto);
    TeacherResponseDto getTeacherById(Long id);
    TeacherResponseDto getTeacherByUserId(Long userId);
    List<TeacherResponseDto> getAllTeachers();
    TeacherResponseDto updateTeacher(Long id, TeacherRequestDto teacherRequestDto);
    void deleteTeacher(Long id);
}
