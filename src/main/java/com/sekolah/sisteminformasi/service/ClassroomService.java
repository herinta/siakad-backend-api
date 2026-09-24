package com.sekolah.sisteminformasi.service;

import com.sekolah.sisteminformasi.dto.classroom.ClassroomDto;

import java.util.List;

public interface ClassroomService {
    ClassroomDto createClassroom(ClassroomDto classroomDto);
    ClassroomDto getClassroomById(Long id);
    List<ClassroomDto> getAllClassrooms();
    List<ClassroomDto> getClassroomsByGradeLevel(Integer gradeLevel);
    ClassroomDto updateClassroom(Long id, ClassroomDto classroomDto);
    void deleteClassroom(Long id);
}
