package com.sekolah.sisteminformasi.repository;

import com.sekolah.sisteminformasi.entity.Attendance;
import com.sekolah.sisteminformasi.entity.AttendanceStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    @EntityGraph(attributePaths = {"schedule", "schedule.subject", "schedule.classroom", "student", "student.classroom"})
    Optional<Attendance> findByScheduleIdAndStudentIdAndAttendanceDate(Long scheduleId, Long studentId, LocalDate attendanceDate);

    @EntityGraph(attributePaths = {"schedule", "schedule.subject", "schedule.classroom", "student", "student.classroom"})
    List<Attendance> findByScheduleIdAndAttendanceDate(Long scheduleId, LocalDate attendanceDate);

    @EntityGraph(attributePaths = {"schedule", "schedule.subject", "schedule.classroom", "student", "student.classroom"})
    List<Attendance> findByStudentId(Long studentId);

    // =========================================================================
    // 🧮 QUERY AGGREGATE REKAP KEHADIRAN SISWA PER SEMESTER
    // =========================================================================
    @Query("SELECT COUNT(a) FROM Attendance a " +
            "WHERE a.student.id = :studentId " +
            "AND a.schedule.academicYear = :academicYear " +
            "AND a.schedule.semester = :semester " +
            "AND a.status = :status")
    Long countByStudentAndSemesterAndStatus(
            @Param("studentId") Long studentId,
            @Param("academicYear") String academicYear,
            @Param("semester") Integer semester,
            @Param("status") AttendanceStatus status
    );

    @Query("SELECT COUNT(a) FROM Attendance a " +
            "WHERE a.student.id = :studentId " +
            "AND a.schedule.academicYear = :academicYear " +
            "AND a.schedule.semester = :semester")
    Long countTotalByStudentAndSemester(
            @Param("studentId") Long studentId,
            @Param("academicYear") String academicYear,
            @Param("semester") Integer semester
    );
}
