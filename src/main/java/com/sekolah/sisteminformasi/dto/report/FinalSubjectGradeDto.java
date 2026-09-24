package com.sekolah.sisteminformasi.dto.report;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FinalSubjectGradeDto {
    private String subjectCode;
    private String subjectName;
    private String teacherName;
    private Double assignmentAverage; // Rata-rata Tugas (30%)
    private Double midtermScore;      // Nilai UTS (30%)
    private Double finalScore;        // Nilai UAS (40%)
    private Double totalScore;        // (Tugas * 0.3) + (UTS * 0.3) + (UAS * 0.4)
    private String letterGrade;       // A (>=85), B (>=75), C (>=60), D (<60)
    private Boolean isPassed;         // KKM >= 75
}
