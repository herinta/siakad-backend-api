package com.sekolah.sisteminformasi.controller;

import com.sekolah.sisteminformasi.dto.classroom.ClassroomDto;
import com.sekolah.sisteminformasi.dto.common.ApiResponse;
import com.sekolah.sisteminformasi.service.ClassroomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/classrooms")
@RequiredArgsConstructor
public class ClassroomController {

    private final ClassroomService classroomService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ClassroomDto>> createClassroom(@Valid @RequestBody ClassroomDto classroomDto) {
        ClassroomDto createdClassroom = classroomService.createClassroom(classroomDto);
        return new ResponseEntity<>(
                ApiResponse.created("Kelas berhasil ditambahkan!", createdClassroom),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<ApiResponse<ClassroomDto>> getClassroomById(@PathVariable Long id) {
        ClassroomDto classroomDto = classroomService.getClassroomById(id);
        return ResponseEntity.ok(ApiResponse.success("Detail kelas berhasil diambil", classroomDto));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<ApiResponse<List<ClassroomDto>>> getAllClassrooms(
            @RequestParam(required = false) Integer gradeLevel) {
        List<ClassroomDto> classrooms;
        if (gradeLevel != null) {
            classrooms = classroomService.getClassroomsByGradeLevel(gradeLevel);
        } else {
            classrooms = classroomService.getAllClassrooms();
        }
        return ResponseEntity.ok(ApiResponse.success("Daftar kelas berhasil diambil", classrooms));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ClassroomDto>> updateClassroom(@PathVariable Long id, @Valid @RequestBody ClassroomDto classroomDto) {
        ClassroomDto updatedClassroom = classroomService.updateClassroom(id, classroomDto);
        return ResponseEntity.ok(ApiResponse.success("Data kelas berhasil diperbarui!", updatedClassroom));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteClassroom(@PathVariable Long id) {
        classroomService.deleteClassroom(id);
        return ResponseEntity.ok(ApiResponse.success("Kelas berhasil dihapus!", null));
    }
}
