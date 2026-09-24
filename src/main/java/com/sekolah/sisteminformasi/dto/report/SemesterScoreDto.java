package com.sekolah.sisteminformasi.dto.report;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SemesterScoreDto {
    private Double semester1; // Ganjil
    private Double semester2; // Genap
}
