package com.sekolah.sisteminformasi.dto.report;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TranscriptSubjectDto {
    private String subjectCode;
    private String subjectName;
    private SemesterScoreDto grade7; // Kelas 7 (Smt 1 & 2)
    private SemesterScoreDto grade8; // Kelas 8 (Smt 1 & 2)
    private SemesterScoreDto grade9; // Kelas 9 (Smt 1 & 2)
    private Double cumulativeAverage;
    private String letterGrade;
}
