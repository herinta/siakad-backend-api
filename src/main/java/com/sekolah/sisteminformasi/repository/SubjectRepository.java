package com.sekolah.sisteminformasi.repository;

import com.sekolah.sisteminformasi.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    Optional<Subject> findByCode(String code);
    Boolean existsByCode(String code);
}
