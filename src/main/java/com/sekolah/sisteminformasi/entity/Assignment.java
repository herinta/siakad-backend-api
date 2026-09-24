package com.sekolah.sisteminformasi.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "assignments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String title; // Contoh: "Tugas 1: Persamaan Linier", "Kuis 2: Tata Surya"

    @Column(length = 500)
    private String description;

    private LocalDate dueDate; // Batas Waktu Pengumpulan

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classroom_id", nullable = false)
    private Classroom classroom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

    @Column(nullable = false, length = 20)
    private String academicYear;

    @Column(nullable = false)
    private Integer semester;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
