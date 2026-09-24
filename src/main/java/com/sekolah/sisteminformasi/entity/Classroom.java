package com.sekolah.sisteminformasi.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "classrooms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Classroom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String code; // Contoh: "7A", "8B", "9C"

    @Column(nullable = false, length = 100)
    private String name; // "Kelas 7A", "Kelas 8B"

    @Column(nullable = false)
    private Integer gradeLevel; // 7, 8, 9

    @Column(nullable = false, length = 20)
    private String academicYear; // "2026/2027"
}
