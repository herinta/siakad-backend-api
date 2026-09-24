package com.sekolah.sisteminformasi.dto.attendance;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceSummaryDto {
    @Builder.Default
    private Long hadir = 0L;
    @Builder.Default
    private Long sakit = 0L;
    @Builder.Default
    private Long izin = 0L;
    @Builder.Default
    private Long alpa = 0L;
    @Builder.Default
    private Long totalPertemuan = 0L;
    @Builder.Default
    private Double attendancePercentage = 0.0;
}
