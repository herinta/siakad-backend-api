package com.sekolah.sisteminformasi.repository;

import com.sekolah.sisteminformasi.entity.Grade;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {

    @EntityGraph(attributePaths = {"student", "student.classroom", "subject", "teacher", "classroom"})
    Optional<Grade> findByStudentIdAndSubjectIdAndAcademicYearAndSemester(
            Long studentId, Long subjectId, String academicYear, Integer semester
    );

    @EntityGraph(attributePaths = {"student", "student.classroom", "subject", "teacher", "classroom"})
    List<Grade> findByStudentIdAndAcademicYearAndSemester(Long studentId, String academicYear, Integer semester);

    @EntityGraph(attributePaths = {"student", "student.classroom", "subject", "teacher", "classroom"})
    List<Grade> findByStudentId(Long studentId);

    @EntityGraph(attributePaths = {"student", "student.classroom", "subject", "teacher", "classroom"})
    List<Grade> findByClassroomIdAndSubjectIdAndAcademicYearAndSemester(
            Long classroomId, Long subjectId, String academicYear, Integer semester
    );
}
