package com.sekolah.sisteminformasi.service.impl;

import com.sekolah.sisteminformasi.dto.schedule.ScheduleRequestDto;
import com.sekolah.sisteminformasi.dto.schedule.ScheduleResponseDto;
import com.sekolah.sisteminformasi.entity.*;
import com.sekolah.sisteminformasi.exception.BadRequestException;
import com.sekolah.sisteminformasi.exception.ResourceNotFoundException;
import com.sekolah.sisteminformasi.exception.ScheduleConflictException;
import com.sekolah.sisteminformasi.mapper.ScheduleMapper;
import com.sekolah.sisteminformasi.repository.*;
import com.sekolah.sisteminformasi.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final TeacherRepository teacherRepository;
    private final SubjectRepository subjectRepository;
    private final ClassroomRepository classroomRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ScheduleResponseDto createSchedule(ScheduleRequestDto scheduleRequestDto) {
        validateTimeRange(scheduleRequestDto);

        Teacher teacher = teacherRepository.findById(scheduleRequestDto.getTeacherId())
                .orElseThrow(() -> new ResourceNotFoundException("Guru", "id", scheduleRequestDto.getTeacherId()));

        Subject subject = subjectRepository.findById(scheduleRequestDto.getSubjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Mata Pelajaran", "id", scheduleRequestDto.getSubjectId()));

        Classroom classroom = classroomRepository.findById(scheduleRequestDto.getClassroomId())
                .orElseThrow(() -> new ResourceNotFoundException("Kelas", "id", scheduleRequestDto.getClassroomId()));

        // =========================================================================
        // 🛡️ EKSEKUSI ALGORITMA PENGECEKAN KONFLIK WAKTU (OVERLAPPING DETECTION)
        // =========================================================================
        validateScheduleConflicts(scheduleRequestDto, teacher, classroom, null);

        Schedule schedule = Schedule.builder()
                .teacher(teacher)
                .subject(subject)
                .classroom(classroom)
                .dayOfWeek(scheduleRequestDto.getDayOfWeek())
                .startTime(scheduleRequestDto.getStartTime())
                .endTime(scheduleRequestDto.getEndTime())
                .academicYear(scheduleRequestDto.getAcademicYear())
                .semester(scheduleRequestDto.getSemester())
                .build();

        Schedule savedSchedule = scheduleRepository.save(schedule);
        return ScheduleMapper.mapToScheduleResponseDto(savedSchedule);
    }

    @Override
    public ScheduleResponseDto getScheduleById(Long id) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Jadwal", "id", id));
        return ScheduleMapper.mapToScheduleResponseDto(schedule);
    }

    @Override
    public List<ScheduleResponseDto> getAllSchedules() {
        return scheduleRepository.findAll().stream()
                .map(ScheduleMapper::mapToScheduleResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ScheduleResponseDto> getSchedulesByTeacherId(Long teacherId) {
        return scheduleRepository.findByTeacherId(teacherId).stream()
                .map(ScheduleMapper::mapToScheduleResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ScheduleResponseDto> getSchedulesByClassroomId(Long classroomId) {
        return scheduleRepository.findByClassroomId(classroomId).stream()
                .map(ScheduleMapper::mapToScheduleResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ScheduleResponseDto> getSchedulesByDay(DayOfWeekEnum dayOfWeek) {
        return scheduleRepository.findByDayOfWeek(dayOfWeek).stream()
                .map(ScheduleMapper::mapToScheduleResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ScheduleResponseDto> getMyTeacherSchedule(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        Teacher teacher = teacherRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Profil Guru untuk Akun User ini", "userId", user.getId()));

        return scheduleRepository.findByTeacherId(teacher.getId()).stream()
                .map(ScheduleMapper::mapToScheduleResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ScheduleResponseDto updateSchedule(Long id, ScheduleRequestDto scheduleRequestDto) {
        validateTimeRange(scheduleRequestDto);

        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Jadwal", "id", id));

        Teacher teacher = teacherRepository.findById(scheduleRequestDto.getTeacherId())
                .orElseThrow(() -> new ResourceNotFoundException("Guru", "id", scheduleRequestDto.getTeacherId()));

        Subject subject = subjectRepository.findById(scheduleRequestDto.getSubjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Mata Pelajaran", "id", scheduleRequestDto.getSubjectId()));

        Classroom classroom = classroomRepository.findById(scheduleRequestDto.getClassroomId())
                .orElseThrow(() -> new ResourceNotFoundException("Kelas", "id", scheduleRequestDto.getClassroomId()));

        // Validasi konflik dengan mengecualikan ID jadwal yang sedang diedit
        validateScheduleConflicts(scheduleRequestDto, teacher, classroom, id);

        schedule.setTeacher(teacher);
        schedule.setSubject(subject);
        schedule.setClassroom(classroom);
        schedule.setDayOfWeek(scheduleRequestDto.getDayOfWeek());
        schedule.setStartTime(scheduleRequestDto.getStartTime());
        schedule.setEndTime(scheduleRequestDto.getEndTime());
        schedule.setAcademicYear(scheduleRequestDto.getAcademicYear());
        schedule.setSemester(scheduleRequestDto.getSemester());

        Schedule updatedSchedule = scheduleRepository.save(schedule);
        return ScheduleMapper.mapToScheduleResponseDto(updatedSchedule);
    }

    @Override
    @Transactional
    public void deleteSchedule(Long id) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Jadwal", "id", id));
        scheduleRepository.delete(schedule);
    }

    // =========================================================================
    // 🔍 HELPER VALIDATION METHODS
    // =========================================================================

    private void validateTimeRange(ScheduleRequestDto dto) {
        if (!dto.getStartTime().isBefore(dto.getEndTime())) {
            throw new BadRequestException("Jam mulai (" + dto.getStartTime() + ") harus lebih awal daripada jam selesai (" + dto.getEndTime() + ")!");
        }
    }

    private void validateScheduleConflicts(ScheduleRequestDto dto, Teacher teacher, Classroom classroom, Long excludeScheduleId) {
        // 1. Cek apakah Guru memiliki jadwal lain di rentang jam tersebut
        List<Schedule> teacherConflicts = scheduleRepository.findTeacherConflictingSchedules(
                dto.getTeacherId(),
                dto.getDayOfWeek(),
                dto.getStartTime(),
                dto.getEndTime(),
                dto.getAcademicYear(),
                dto.getSemester(),
                excludeScheduleId
        );

        if (!teacherConflicts.isEmpty()) {
            Schedule conflict = teacherConflicts.get(0);
            throw new ScheduleConflictException(String.format(
                    "Konflik Jadwal Guru: Guru '%s' sudah memiliki jadwal mengajar mapel '%s' di kelas '%s' pada hari %s pukul %s - %s!",
                    teacher.getFullName(),
                    conflict.getSubject().getName(),
                    conflict.getClassroom().getName(),
                    conflict.getDayOfWeek(),
                    conflict.getStartTime(),
                    conflict.getEndTime()
            ));
        }

        // 2. Cek apakah Ruang Kelas sudah terpakai di rentang jam tersebut
        List<Schedule> classroomConflicts = scheduleRepository.findClassroomConflictingSchedules(
                dto.getClassroomId(),
                dto.getDayOfWeek(),
                dto.getStartTime(),
                dto.getEndTime(),
                dto.getAcademicYear(),
                dto.getSemester(),
                excludeScheduleId
        );

        if (!classroomConflicts.isEmpty()) {
            Schedule conflict = classroomConflicts.get(0);
            throw new ScheduleConflictException(String.format(
                    "Konflik Ruang Kelas: Kelas '%s' sudah terpakai untuk mapel '%s' oleh Guru '%s' pada hari %s pukul %s - %s!",
                    classroom.getName(),
                    conflict.getSubject().getName(),
                    conflict.getTeacher().getFullName(),
                    conflict.getDayOfWeek(),
                    conflict.getStartTime(),
                    conflict.getEndTime()
            ));
        }
    }
}
