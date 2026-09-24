package com.sekolah.sisteminformasi.dto.assignment;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssignmentRequestDto {

    @NotBlank(message = "Judul tugas tidak boleh kosong")
    private String title;

    private String description;
    private LocalDate dueDate;

    @NotNull(message = "ID Mata Pelajaran wajib diisi")
    private Long subjectId;

    @NotNull(message = "ID Kelas wajib diisi")
    private Long classroomId;

    @NotNull(message = "ID Guru wajib diisi")
    private Long teacherId;

    @NotBlank(message = "Tahun ajaran wajib diisi")
    private String academicYear;

    @NotNull(message = "Semester wajib diisi (1 atau 2)")
    @Min(1)
    @Max(2)
    private Integer semester;
}
