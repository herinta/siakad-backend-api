package com.sekolah.sisteminformasi.dto.sync;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SyncGradeDto {
    private Long studentId;
    private Long classroomId;
    private Long subjectId;
    private Long teacherId;
    private String title;
    private Double score;
    private String category; // TUGAS_LMS, KUIS_LMS
}
