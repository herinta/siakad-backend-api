package com.sekolah.sisteminformasi.controller;

import com.sekolah.sisteminformasi.dto.common.ApiResponse;
import com.sekolah.sisteminformasi.dto.student.StudentRequestDto;
import com.sekolah.sisteminformasi.dto.student.StudentResponseDto;
import com.sekolah.sisteminformasi.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<StudentResponseDto>> createStudent(@Valid @RequestBody StudentRequestDto studentRequestDto) {
        StudentResponseDto createdStudent = studentService.createStudent(studentRequestDto);
        return new ResponseEntity<>(
                ApiResponse.created("Data siswa berhasil ditambahkan!", createdStudent),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<ApiResponse<StudentResponseDto>> getStudentById(@PathVariable Long id) {
        StudentResponseDto student = studentService.getStudentById(id);
        return ResponseEntity.ok(ApiResponse.success("Detail siswa berhasil diambil", student));
    }

    @GetMapping("/nisn/{nisn}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<ApiResponse<StudentResponseDto>> getStudentByNisn(@PathVariable String nisn) {
        StudentResponseDto student = studentService.getStudentByNisn(nisn);
        return ResponseEntity.ok(ApiResponse.success("Detail siswa berhasil diambil", student));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<ApiResponse<List<StudentResponseDto>>> getAllStudents(
            @RequestParam(required = false) Long classroomId) {
        List<StudentResponseDto> students;
        if (classroomId != null) {
            students = studentService.getStudentsByClassroomId(classroomId);
        } else {
            students = studentService.getAllStudents();
        }
        return ResponseEntity.ok(ApiResponse.success("Daftar siswa berhasil diambil", students));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<StudentResponseDto>> updateStudent(
            @PathVariable Long id,
            @Valid @RequestBody StudentRequestDto studentRequestDto) {
        StudentResponseDto updatedStudent = studentService.updateStudent(id, studentRequestDto);
        return ResponseEntity.ok(ApiResponse.success("Data siswa berhasil diperbarui!", updatedStudent));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok(ApiResponse.success("Data siswa berhasil dinonaktifkan (soft-delete)!", null));
    }
}
