package com.sekolah.sisteminformasi.dto.assignment;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssignmentScoreRequestDto {

    @NotNull(message = "ID Siswa wajib diisi")
    private Long studentId;

    @NotNull(message = "Nilai wajib diisi")
    @Min(value = 0, message = "Nilai minimal 0")
    @Max(value = 100, message = "Nilai maksimal 100")
    private Double score;

    private String feedback;
}
