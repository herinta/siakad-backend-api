package com.sekolah.sisteminformasi.dto.grade;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GradeRequestDto {

    @NotNull(message = "ID Siswa wajib diisi")
    private Long studentId;

    @NotNull(message = "ID Mata Pelajaran wajib diisi")
    private Long subjectId;

    @NotNull(message = "ID Guru wajib diisi")
    private Long teacherId;

    @NotBlank(message = "Tahun ajaran wajib diisi (contoh: 2026/2027)")
    private String academicYear;

    @NotNull(message = "Semester wajib diisi (1 atau 2)")
    @Min(1)
    @Max(2)
    private Integer semester;

    @Min(value = 0, message = "Nilai UTS minimal 0")
    @Max(value = 100, message = "Nilai UTS maksimal 100")
    private Double midtermScore;

    @Min(value = 0, message = "Nilai UAS minimal 0")
    @Max(value = 100, message = "Nilai UAS maksimal 100")
    private Double finalScore;
}
