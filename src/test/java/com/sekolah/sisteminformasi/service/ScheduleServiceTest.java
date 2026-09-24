package com.sekolah.sisteminformasi.service;

import com.sekolah.sisteminformasi.dto.schedule.ScheduleRequestDto;
import com.sekolah.sisteminformasi.dto.schedule.ScheduleResponseDto;
import com.sekolah.sisteminformasi.entity.*;
import com.sekolah.sisteminformasi.exception.BadRequestException;
import com.sekolah.sisteminformasi.exception.ScheduleConflictException;
import com.sekolah.sisteminformasi.repository.*;
import com.sekolah.sisteminformasi.service.impl.ScheduleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScheduleServiceTest {

    @Mock
    private ScheduleRepository scheduleRepository;
    @Mock
    private TeacherRepository teacherRepository;
    @Mock
    private SubjectRepository subjectRepository;
    @Mock
    private ClassroomRepository classroomRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ScheduleServiceImpl scheduleService;

    private Teacher sampleTeacher;
    private Subject sampleSubject;
    private Classroom sampleClassroom;
    private ScheduleRequestDto validRequestDto;

    @BeforeEach
    void setUp() {
        sampleTeacher = Teacher.builder()
                .id(1L)
                .fullName("Budi Santoso, S.Pd")
                .nip("198501152010011001")
                .email("budi@sekolah.com")
                .build();

        sampleSubject = Subject.builder()
                .id(1L)
                .code("MTK-01")
                .name("Matematika")
                .creditHours(4)
                .build();

        sampleClassroom = Classroom.builder()
                .id(1L)
                .code("7A")
                .name("Kelas 7A")
                .gradeLevel(7)
                .academicYear("2026/2027")
                .build();

        validRequestDto = ScheduleRequestDto.builder()
                .teacherId(1L)
                .subjectId(1L)
                .classroomId(1L)
                .dayOfWeek(DayOfWeekEnum.SENIN)
                .startTime(LocalTime.of(7, 30))
                .endTime(LocalTime.of(9, 0))
                .academicYear("2026/2027")
                .semester(1)
                .build();
    }

    @Test
    @DisplayName("Unit Test 1: Pembuatan jadwal berhasil jika tidak ada konflik")
    void testCreateSchedule_Success() {
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(sampleTeacher));
        when(subjectRepository.findById(1L)).thenReturn(Optional.of(sampleSubject));
        when(classroomRepository.findById(1L)).thenReturn(Optional.of(sampleClassroom));
        when(scheduleRepository.findTeacherConflictingSchedules(any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(Collections.emptyList());
        when(scheduleRepository.findClassroomConflictingSchedules(any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(Collections.emptyList());

        Schedule savedSchedule = Schedule.builder()
                .id(100L)
                .teacher(sampleTeacher)
                .subject(sampleSubject)
                .classroom(sampleClassroom)
                .dayOfWeek(DayOfWeekEnum.SENIN)
                .startTime(LocalTime.of(7, 30))
                .endTime(LocalTime.of(9, 0))
                .academicYear("2026/2027")
                .semester(1)
                .build();

        when(scheduleRepository.save(any(Schedule.class))).thenReturn(savedSchedule);

        ScheduleResponseDto response = scheduleService.createSchedule(validRequestDto);

        assertNotNull(response);
        assertEquals(100L, response.getId());
        assertEquals("Matematika", response.getSubject().getName());
        assertEquals("Budi Santoso, S.Pd", response.getTeacher().getFullName());
        verify(scheduleRepository, times(1)).save(any(Schedule.class));
    }

    @Test
    @DisplayName("Unit Test 2: Gagal dan lempar ScheduleConflictException jika jam guru bentrok")
    void testCreateSchedule_TeacherConflict_ThrowsException() {
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(sampleTeacher));
        when(subjectRepository.findById(1L)).thenReturn(Optional.of(sampleSubject));
        when(classroomRepository.findById(1L)).thenReturn(Optional.of(sampleClassroom));

        Schedule conflictingSchedule = Schedule.builder()
                .id(99L)
                .teacher(sampleTeacher)
                .subject(sampleSubject)
                .classroom(sampleClassroom)
                .dayOfWeek(DayOfWeekEnum.SENIN)
                .startTime(LocalTime.of(7, 0))
                .endTime(LocalTime.of(8, 30))
                .build();

        when(scheduleRepository.findTeacherConflictingSchedules(any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(List.of(conflictingSchedule));

        assertThrows(ScheduleConflictException.class, () -> {
            scheduleService.createSchedule(validRequestDto);
        });

        verify(scheduleRepository, never()).save(any());
    }

    @Test
    @DisplayName("Unit Test 3: Gagal dan lempar BadRequestException jika jam mulai >= jam selesai")
    void testCreateSchedule_InvalidTimeRange_ThrowsException() {
        ScheduleRequestDto invalidTimeDto = ScheduleRequestDto.builder()
                .teacherId(1L)
                .subjectId(1L)
                .classroomId(1L)
                .dayOfWeek(DayOfWeekEnum.SENIN)
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(8, 0)) // Salah: jam selesai lebih awal
                .academicYear("2026/2027")
                .semester(1)
                .build();

        assertThrows(BadRequestException.class, () -> {
            scheduleService.createSchedule(invalidTimeDto);
        });

        verify(scheduleRepository, never()).save(any());
    }
}
