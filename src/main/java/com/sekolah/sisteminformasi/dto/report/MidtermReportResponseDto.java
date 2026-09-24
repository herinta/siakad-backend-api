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
public class MidtermReportResponseDto {
    private StudentResponseDto student;
    private String academicYear;
    private Integer semester;
    private List<MidtermSubjectGradeDto> subjectGrades;
    private Double totalAverageScore;
    private Integer totalSubjects;
    private Integer totalPassed;
    private Integer totalRemedial;
    private AttendanceSummaryDto attendance;
}
