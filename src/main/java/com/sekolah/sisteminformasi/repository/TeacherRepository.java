package com.sekolah.sisteminformasi.repository;

import com.sekolah.sisteminformasi.entity.Teacher;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    @EntityGraph(attributePaths = {"subject", "user"})
    Optional<Teacher> findById(Long id);

    @EntityGraph(attributePaths = {"subject", "user"})
    List<Teacher> findAll();

    Optional<Teacher> findByNip(String nip);
    Optional<Teacher> findByEmail(String email);
    Optional<Teacher> findByUserId(Long userId);

    Boolean existsByNip(String nip);
    Boolean existsByEmail(String email);
}
