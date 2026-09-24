package com.sekolah.sisteminformasi.repository;

import com.sekolah.sisteminformasi.entity.Student;
import com.sekolah.sisteminformasi.entity.StudentStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    @EntityGraph(attributePaths = {"classroom"})
    Optional<Student> findById(Long id);

    @EntityGraph(attributePaths = {"classroom"})
    List<Student> findAll();

    @EntityGraph(attributePaths = {"classroom"})
    List<Student> findByClassroomId(Long classroomId);

    @EntityGraph(attributePaths = {"classroom"})
    List<Student> findByClassroomIdAndStatus(Long classroomId, StudentStatus status);

    Optional<Student> findByNisn(String nisn);
    Optional<Student> findByNis(String nis);

    Boolean existsByNisn(String nisn);
    Boolean existsByNis(String nis);
}
