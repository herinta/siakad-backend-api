package com.sekolah.sisteminformasi.dto.classroom;

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
public class ClassroomDto {

    private Long id;

    @NotBlank(message = "Kode kelas tidak boleh kosong (contoh: 7A, 8B)")
    private String code;

    @NotBlank(message = "Nama kelas tidak boleh kosong (contoh: Kelas 7A)")
    private String name;

    @NotNull(message = "Tingkat kelas wajib diisi")
    @Min(value = 1, message = "Tingkat kelas minimal 1")
    @Max(value = 12, message = "Tingkat kelas maksimal 12")
    private Integer gradeLevel;

    @NotBlank(message = "Tahun ajaran tidak boleh kosong (contoh: 2026/2027)")
    private String academicYear;
}
