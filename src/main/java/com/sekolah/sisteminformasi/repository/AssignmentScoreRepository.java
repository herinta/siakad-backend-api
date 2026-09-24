package com.sekolah.sisteminformasi.repository;

import com.sekolah.sisteminformasi.entity.AssignmentScore;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssignmentScoreRepository extends JpaRepository<AssignmentScore, Long> {

    @EntityGraph(attributePaths = {"assignment", "student", "student.classroom"})
    Optional<AssignmentScore> findByAssignmentIdAndStudentId(Long assignmentId, Long studentId);

    @EntityGraph(attributePaths = {"assignment", "student", "student.classroom"})
    List<AssignmentScore> findByAssignmentId(Long assignmentId);

    @EntityGraph(attributePaths = {"assignment", "student", "student.classroom"})
    List<AssignmentScore> findByStudentId(Long studentId);

    // =========================================================================
    // 🧮 QUERY AGGREGATE OTOMATIS MENGHITUNG RATA-RATA TUGAS HARIAN
    // =========================================================================
    @Query("SELECT AVG(asc.score) FROM AssignmentScore asc " +
            "WHERE asc.student.id = :studentId " +
            "AND asc.assignment.subject.id = :subjectId " +
            "AND asc.assignment.academicYear = :academicYear " +
            "AND asc.assignment.semester = :semester")
    Double getAverageScoreByStudentAndSubject(
            @Param("studentId") Long studentId,
            @Param("subjectId") Long subjectId,
            @Param("academicYear") String academicYear,
            @Param("semester") Integer semester
    );
}
