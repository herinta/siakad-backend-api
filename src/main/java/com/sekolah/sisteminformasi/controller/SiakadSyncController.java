package com.sekolah.sisteminformasi.controller;

import com.sekolah.sisteminformasi.dto.common.ApiResponse;
import com.sekolah.sisteminformasi.dto.sync.SyncAttendanceDto;
import com.sekolah.sisteminformasi.dto.sync.SyncGradeDto;
import com.sekolah.sisteminformasi.entity.*;
import com.sekolah.sisteminformasi.repository.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/sync")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "SIAKAD Microservice Sync", description = "Sinkronisasi data otomatis dari modul LMS ke Core SIAKAD")
public class SiakadSyncController {

    private final StudentRepository studentRepository;
    private final ClassroomRepository classroomRepository;
    private final SubjectRepository subjectRepository;
    private final TeacherRepository teacherRepository;
    private final ScheduleRepository scheduleRepository;
    private final AttendanceRepository attendanceRepository;
    private final AssignmentRepository assignmentRepository;
    private final AssignmentScoreRepository assignmentScoreRepository;

    @PostMapping("/attendance")
    @Transactional
    @Operation(summary = "Sinkronisasi presensi pertemuan LMS ke tabel Attendance SIAKAD")
    public ResponseEntity<ApiResponse<Void>> syncAttendance(@RequestBody SyncAttendanceDto dto) {
        log.info("Menerima sinkronisasi presensi dari LMS untuk Siswa ID: {}", dto.getStudentId());

        Student student = studentRepository.findById(dto.getStudentId()).orElse(null);
        if (student == null) {
            log.warn("Siswa dengan ID {} tidak ditemukan di SIAKAD, lewati sinkronisasi presensi", dto.getStudentId());
            return ResponseEntity.ok(ApiResponse.success("Siswa tidak ditemukan, lewati", null));
        }

        // Cari jadwal aktif kelas siswa jika ada
        List<Schedule> schedules = scheduleRepository.findByClassroomId(dto.getClassroomId());
        Schedule schedule = schedules.isEmpty() ? null : schedules.get(0);

        if (schedule != null) {
            LocalDate date = dto.getDate() != null ? dto.getDate() : LocalDate.now();
            AttendanceStatus status = AttendanceStatus.HADIR;
            try {
                status = AttendanceStatus.valueOf(dto.getStatus().toUpperCase());
            } catch (Exception ignored) {}

            Attendance attendance = attendanceRepository
                    .findByScheduleIdAndStudentIdAndAttendanceDate(schedule.getId(), student.getId(), date)
                    .orElse(Attendance.builder()
                            .schedule(schedule)
                            .student(student)
                            .attendanceDate(date)
                            .build());

            attendance.setStatus(status);
            attendance.setRemarks(dto.getNotes() != null ? dto.getNotes() : "Sinkronisasi Presensi LMS");
            attendanceRepository.save(attendance);
        }

        return ResponseEntity.ok(ApiResponse.success("Presensi berhasil disinkronkan ke SIAKAD", null));
    }

    @PostMapping("/grade")
    @Transactional
    @Operation(summary = "Sinkronisasi nilai tugas/kuis LMS ke tabel AssignmentScore SIAKAD")
    public ResponseEntity<ApiResponse<Void>> syncGrade(@RequestBody SyncGradeDto dto) {
        log.info("Menerima sinkronisasi nilai dari LMS untuk Siswa ID: {}, Nilai: {}", dto.getStudentId(), dto.getScore());

        Student student = studentRepository.findById(dto.getStudentId()).orElse(null);
        if (student == null) {
            log.warn("Siswa dengan ID {} tidak ditemukan di SIAKAD", dto.getStudentId());
            return ResponseEntity.ok(ApiResponse.success("Siswa tidak ditemukan, lewati", null));
        }

        Classroom classroom = dto.getClassroomId() != null ? classroomRepository.findById(dto.getClassroomId()).orElse(student.getClassroom()) : student.getClassroom();
        Subject subject = dto.getSubjectId() != null ? subjectRepository.findById(dto.getSubjectId()).orElse(null) : null;
        Teacher teacher = dto.getTeacherId() != null ? teacherRepository.findById(dto.getTeacherId()).orElse(null) : null;

        if (classroom == null || subject == null) {
            log.warn("Classroom atau Subject tidak valid untuk sinkronisasi nilai");
            return ResponseEntity.ok(ApiResponse.success("Data kelas/mapel tidak lengkap, lewati", null));
        }

        // Cari atau buat assignment untuk tugas/kuis LMS ini
        List<Assignment> existingAssignments = assignmentRepository.findByClassroomIdAndSubjectId(classroom.getId(), subject.getId());
        Assignment targetAssignment = null;
        for (Assignment a : existingAssignments) {
            if (a.getTitle().equalsIgnoreCase(dto.getTitle())) {
                targetAssignment = a;
                break;
            }
        }

        if (targetAssignment == null) {
            targetAssignment = Assignment.builder()
                    .title(dto.getTitle())
                    .description("Sinkronisasi otomatis dari LMS (" + dto.getCategory() + ")")
                    .classroom(classroom)
                    .subject(subject)
                    .teacher(teacher != null ? teacher : (classroom.getHomeroomTeacher() != null ? classroom.getHomeroomTeacher() : null))
                    .academicYear(classroom.getAcademicYear() != null ? classroom.getAcademicYear() : "2026/2027")
                    .semester(1)
                    .dueDate(LocalDate.now())
                    .build();
            targetAssignment = assignmentRepository.save(targetAssignment);
        }

        AssignmentScore score = assignmentScoreRepository
                .findByAssignmentIdAndStudentId(targetAssignment.getId(), student.getId())
                .orElse(AssignmentScore.builder()
                        .assignment(targetAssignment)
                        .student(student)
                        .build());

        score.setScore(dto.getScore());
        score.setFeedback("Nilai otomatis dari LMS");
        assignmentScoreRepository.save(score);

        return ResponseEntity.ok(ApiResponse.success("Nilai berhasil disinkronkan ke SIAKAD", null));
    }
}
