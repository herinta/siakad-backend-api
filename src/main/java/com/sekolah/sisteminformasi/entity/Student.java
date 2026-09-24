package com.sekolah.sisteminformasi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "students")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 10)
    private String nisn; // Nomor Induk Siswa Nasional (10 digit)

    @Column(nullable = false, unique = true, length = 20)
    private String nis; // Nomor Induk Sekolah

    @Column(nullable = false, length = 100)
    private String fullName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 5)
    private Gender gender; // L / P

    private String birthPlace;
    private LocalDate birthDate;
    private String religion;
    private String address;

    private String parentName;
    private String parentPhone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classroom_id", nullable = false)
    private Classroom classroom;

    @Column(nullable = false, length = 10)
    private String admissionYear; // Tahun Masuk, misal: "2024"

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private StudentStatus status = StudentStatus.ACTIVE;
}
