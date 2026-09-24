package com.sekolah.sisteminformasi.service.impl;

import com.sekolah.sisteminformasi.dto.attendance.AttendanceItemDto;
import com.sekolah.sisteminformasi.dto.attendance.AttendanceResponseDto;
import com.sekolah.sisteminformasi.dto.attendance.AttendanceSummaryDto;
import com.sekolah.sisteminformasi.dto.attendance.BatchAttendanceRequestDto;
import com.sekolah.sisteminformasi.entity.Attendance;
import com.sekolah.sisteminformasi.entity.AttendanceStatus;
import com.sekolah.sisteminformasi.entity.Schedule;
import com.sekolah.sisteminformasi.entity.Student;
import com.sekolah.sisteminformasi.exception.BadRequestException;
import com.sekolah.sisteminformasi.exception.ResourceNotFoundException;
import com.sekolah.sisteminformasi.mapper.AttendanceMapper;
import com.sekolah.sisteminformasi.repository.AttendanceRepository;
import com.sekolah.sisteminformasi.repository.ScheduleRepository;
import com.sekolah.sisteminformasi.repository.StudentRepository;
import com.sekolah.sisteminformasi.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final ScheduleRepository scheduleRepository;
    private final StudentRepository studentRepository;

    @Override
    @Transactional
    public List<AttendanceResponseDto> recordBatchAttendance(BatchAttendanceRequestDto batchRequest) {
        Schedule schedule = scheduleRepository.findById(batchRequest.getScheduleId())
                .orElseThrow(() -> new ResourceNotFoundException("Jadwal", "id", batchRequest.getScheduleId()));

        List<Attendance> attendancesToSave = new ArrayList<>();

        for (AttendanceItemDto item : batchRequest.getAttendances()) {
            Student student = studentRepository.findById(item.getStudentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Siswa", "id", item.getStudentId()));

            // Validasi: Siswa harus berasal dari kelas pada jadwal tersebut
            if (!student.getClassroom().getId().equals(schedule.getClassroom().getId())) {
                throw new BadRequestException(String.format(
                        "Siswa '%s' bukan anggota kelas '%s'!",
                        student.getFullName(),
                        schedule.getClassroom().getName()
                ));
            }

            // Upsert: Cek apakah sudah pernah diabsen di tanggal ini
            Optional<Attendance> existingAttendance = attendanceRepository
                    .findByScheduleIdAndStudentIdAndAttendanceDate(
                            schedule.getId(),
                            student.getId(),
                            batchRequest.getAttendanceDate()
                    );

            Attendance attendance;
            if (existingAttendance.isPresent()) {
                attendance = existingAttendance.get();
                attendance.setStatus(item.getStatus());
                attendance.setRemarks(item.getRemarks());
            } else {
                attendance = Attendance.builder()
                        .schedule(schedule)
                        .student(student)
                        .attendanceDate(batchRequest.getAttendanceDate())
                        .status(item.getStatus())
                        .remarks(item.getRemarks())
                        .build();
            }

            attendancesToSave.add(attendance);
        }

        List<Attendance> savedAttendances = attendanceRepository.saveAll(attendancesToSave);
        return savedAttendances.stream()
                .map(AttendanceMapper::mapToAttendanceResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AttendanceResponseDto> getAttendanceByScheduleAndDate(Long scheduleId, LocalDate date) {
        return attendanceRepository.findByScheduleIdAndAttendanceDate(scheduleId, date).stream()
                .map(AttendanceMapper::mapToAttendanceResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AttendanceResponseDto> getAttendanceByStudentId(Long studentId) {
        return attendanceRepository.findByStudentId(studentId).stream()
                .map(AttendanceMapper::mapToAttendanceResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public AttendanceSummaryDto getStudentAttendanceSummary(Long studentId, String academicYear, Integer semester) {
        Long hadir = attendanceRepository.countByStudentAndSemesterAndStatus(studentId, academicYear, semester, AttendanceStatus.HADIR);
        Long sakit = attendanceRepository.countByStudentAndSemesterAndStatus(studentId, academicYear, semester, AttendanceStatus.SAKIT);
        Long izin = attendanceRepository.countByStudentAndSemesterAndStatus(studentId, academicYear, semester, AttendanceStatus.IZIN);
        Long alpa = attendanceRepository.countByStudentAndSemesterAndStatus(studentId, academicYear, semester, AttendanceStatus.ALPA);
        Long total = attendanceRepository.countTotalByStudentAndSemester(studentId, academicYear, semester);

        Double percentage = (total != null && total > 0)
                ? Math.round(((double) hadir / total * 100.0) * 100.0) / 100.0
                : 0.0;

        return AttendanceSummaryDto.builder()
                .hadir(hadir != null ? hadir : 0L)
                .sakit(sakit != null ? sakit : 0L)
                .izin(izin != null ? izin : 0L)
                .alpa(alpa != null ? alpa : 0L)
                .totalPertemuan(total != null ? total : 0L)
                .attendancePercentage(percentage)
                .build();
    }
}
