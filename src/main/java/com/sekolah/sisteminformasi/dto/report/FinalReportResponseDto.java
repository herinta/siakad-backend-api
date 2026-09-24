package com.sekolah.sisteminformasi.dto.report;

import com.sekolah.sisteminformasi.dto.attendance.AttendanceSummaryDto;
import com.sekolah.sisteminformasi.dto.student.StudentResponseDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FinalReportResponseDto {
    private StudentResponseDto student;
    private String academicYear;
    private Integer semester;
    private List<FinalSubjectGradeDto> subjectGrades;
    private Double semesterAverageScore;
    private Integer totalSubjects;
    private Integer totalPassed;
    private Integer totalRemedial;
    private String academicStatus; // LULUS / NAIK_KELAS / TINGGAL_KELAS
    private AttendanceSummaryDto attendance;
}
