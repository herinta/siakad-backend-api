package com.sekolah.sisteminformasi.dto.attendance;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BatchAttendanceRequestDto {

    @NotNull(message = "ID Jadwal wajib diisi")
    private Long scheduleId;

    @NotNull(message = "Tanggal absensi wajib diisi")
    private LocalDate attendanceDate;

    @NotEmpty(message = "Daftar kehadiran siswa tidak boleh kosong")
    @Valid
    private List<AttendanceItemDto> attendances;
}
