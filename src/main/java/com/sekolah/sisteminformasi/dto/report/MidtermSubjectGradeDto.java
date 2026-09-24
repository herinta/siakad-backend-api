package com.sekolah.sisteminformasi.dto.report;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MidtermSubjectGradeDto {
    private String subjectCode;
    private String subjectName;
    private String teacherName;
    private Double assignmentAverage; // Rata-rata Tugas (40%)
    private Double midtermScore;      // Nilai Murni UTS (60%)
    private Double finalMidtermScore; // (Tugas * 0.4) + (UTS * 0.6)
    private String letterGrade;       // A, B, C, D
    private Boolean isPassed;         // Berdasarkan KKM standar (>= 75)
}
