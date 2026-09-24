package com.sekolah.sisteminformasi.repository;

import com.sekolah.sisteminformasi.entity.DayOfWeekEnum;
import com.sekolah.sisteminformasi.entity.Schedule;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    @EntityGraph(attributePaths = {"teacher", "teacher.subject", "subject", "classroom"})
    Optional<Schedule> findById(Long id);

    @EntityGraph(attributePaths = {"teacher", "teacher.subject", "subject", "classroom"})
    List<Schedule> findAll();

    @EntityGraph(attributePaths = {"teacher", "teacher.subject", "subject", "classroom"})
    List<Schedule> findByTeacherId(Long teacherId);

    @EntityGraph(attributePaths = {"teacher", "teacher.subject", "subject", "classroom"})
    List<Schedule> findByClassroomId(Long classroomId);

    @EntityGraph(attributePaths = {"teacher", "teacher.subject", "subject", "classroom"})
    List<Schedule> findByDayOfWeek(DayOfWeekEnum dayOfWeek);

    // =========================================================================
    // 🧠 ALGORITMA QUERY PENDETEKSI OVERLAPPING / KONFLIK JADWAL
    // Formula Interval Tabrakan: (StartA < EndB) AND (EndA > StartB)
    // =========================================================================

    @Query("SELECT s FROM Schedule s WHERE s.teacher.id = :teacherId " +
            "AND s.dayOfWeek = :dayOfWeek " +
            "AND s.academicYear = :academicYear " +
            "AND s.semester = :semester " +
            "AND (:excludeScheduleId IS NULL OR s.id != :excludeScheduleId) " +
            "AND (s.startTime < :endTime AND s.endTime > :startTime)")
    List<Schedule> findTeacherConflictingSchedules(
            @Param("teacherId") Long teacherId,
            @Param("dayOfWeek") DayOfWeekEnum dayOfWeek,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime,
            @Param("academicYear") String academicYear,
            @Param("semester") Integer semester,
            @Param("excludeScheduleId") Long excludeScheduleId
    );

    @Query("SELECT s FROM Schedule s WHERE s.classroom.id = :classroomId " +
            "AND s.dayOfWeek = :dayOfWeek " +
            "AND s.academicYear = :academicYear " +
            "AND s.semester = :semester " +
            "AND (:excludeScheduleId IS NULL OR s.id != :excludeScheduleId) " +
            "AND (s.startTime < :endTime AND s.endTime > :startTime)")
    List<Schedule> findClassroomConflictingSchedules(
            @Param("classroomId") Long classroomId,
            @Param("dayOfWeek") DayOfWeekEnum dayOfWeek,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime,
            @Param("academicYear") String academicYear,
            @Param("semester") Integer semester,
            @Param("excludeScheduleId") Long excludeScheduleId
    );
}
