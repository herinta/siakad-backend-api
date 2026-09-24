package com.sekolah.sisteminformasi.repository;

import com.sekolah.sisteminformasi.entity.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassroomRepository extends JpaRepository<Classroom, Long> {
    Optional<Classroom> findByCode(String code);
    Boolean existsByCode(String code);
    List<Classroom> findByGradeLevel(Integer gradeLevel);
}
