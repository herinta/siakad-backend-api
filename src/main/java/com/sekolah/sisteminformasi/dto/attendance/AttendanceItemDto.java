package com.sekolah.sisteminformasi.dto.attendance;

import com.sekolah.sisteminformasi.entity.AttendanceStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceItemDto {

    @NotNull(message = "ID Siswa wajib diisi")
    private Long studentId;

    @NotNull(message = "Status kehadiran wajib diisi (HADIR, SAKIT, IZIN, ALPA)")
    private AttendanceStatus status;

    private String remarks;
}
