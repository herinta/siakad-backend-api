package com.sekolah.sisteminformasi.dto.schedule;

import com.sekolah.sisteminformasi.entity.DayOfWeekEnum;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleRequestDto {

    @NotNull(message = "ID Guru wajib diisi")
    private Long teacherId;

    @NotNull(message = "ID Mata Pelajaran wajib diisi")
    private Long subjectId;

    @NotNull(message = "ID Kelas wajib diisi")
    private Long classroomId;

    @NotNull(message = "Hari wajib dipilih (SENIN, SELASA, RABU, KAMIS, JUMAT, SABTU)")
    private DayOfWeekEnum dayOfWeek;

    @NotNull(message = "Jam mulai wajib diisi (Format HH:mm:ss, misal: 07:30:00)")
    private LocalTime startTime;

    @NotNull(message = "Jam selesai wajib diisi (Format HH:mm:ss, misal: 09:00:00)")
    private LocalTime endTime;

    @NotBlank(message = "Tahun ajaran wajib diisi (contoh: 2026/2027)")
    private String academicYear;

    @NotNull(message = "Semester wajib diisi (1 atau 2)")
    @Min(value = 1, message = "Semester minimal 1 (Ganjil)")
    @Max(value = 2, message = "Semester maksimal 2 (Genap)")
    private Integer semester;
}
