package com.sekolah.sisteminformasi.dto.sync;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SyncAttendanceDto {
    private Long studentId;
    private Long classroomId;
    private LocalDate date;
    private String status; // HADIR, SAKIT, IZIN, ALPA
    private String notes;
}
