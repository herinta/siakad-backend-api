package com.sekolah.sisteminformasi.dto.report;

import com.sekolah.sisteminformasi.dto.student.StudentResponseDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CumulativeTranscriptResponseDto {
    private StudentResponseDto student;
    private String schoolLevel; // "SMP (Sekolah Menengah Pertama)"
    private List<TranscriptSubjectDto> transcripts;
    private Double overallCumulativeAverage;
    private String graduationStatus; // "LULUS" / "BELUM_LULUS"
}
