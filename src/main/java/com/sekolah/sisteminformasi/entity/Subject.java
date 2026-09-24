package com.sekolah.sisteminformasi.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "subjects")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String code; // Contoh: MTK-01, IPA-01

    @Column(nullable = false, length = 100)
    private String name; // Matematika, IPA

    @Column(length = 255)
    private String description;

    @Column(nullable = false)
    private Integer creditHours; // Jumlah jam pelajaran per minggu (misal: 4 jam)
}
