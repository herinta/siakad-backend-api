package com.sekolah.sisteminformasi.repository;

import com.sekolah.sisteminformasi.entity.Assignment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    @EntityGraph(attributePaths = {"subject", "classroom", "teacher"})
    Optional<Assignment> findById(Long id);

    @EntityGraph(attributePaths = {"subject", "classroom", "teacher"})
    List<Assignment> findByClassroomIdAndSubjectId(Long classroomId, Long subjectId);

    @EntityGraph(attributePaths = {"subject", "classroom", "teacher"})
    List<Assignment> findByClassroomId(Long classroomId);

    @EntityGraph(attributePaths = {"subject", "classroom", "teacher"})
    List<Assignment> findByTeacherId(Long teacherId);
}
