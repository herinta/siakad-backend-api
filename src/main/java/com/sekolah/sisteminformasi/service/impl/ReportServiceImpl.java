package com.sekolah.sisteminformasi.service.impl;

import com.sekolah.sisteminformasi.dto.attendance.AttendanceSummaryDto;
import com.sekolah.sisteminformasi.dto.report.*;
import com.sekolah.sisteminformasi.entity.Grade;
import com.sekolah.sisteminformasi.entity.Student;
import com.sekolah.sisteminformasi.entity.Subject;
import com.sekolah.sisteminformasi.exception.ResourceNotFoundException;
import com.sekolah.sisteminformasi.mapper.StudentMapper;
import com.sekolah.sisteminformasi.repository.GradeRepository;
import com.sekolah.sisteminformasi.repository.StudentRepository;
import com.sekolah.sisteminformasi.repository.SubjectRepository;
import com.sekolah.sisteminformasi.service.AssignmentService;
import com.sekolah.sisteminformasi.service.AttendanceService;
import com.sekolah.sisteminformasi.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private static final double STANDARD_KKM = 75.0;

    private final StudentRepository studentRepository;
    private final GradeRepository gradeRepository;
    private final SubjectRepository subjectRepository;
    private final AssignmentService assignmentService;
    private final AttendanceService attendanceService;

    @Override
    public MidtermReportResponseDto generateMidtermReport(Long studentId, String academicYear, Integer semester) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Siswa", "id", studentId));

        List<Grade> grades = gradeRepository.findByStudentIdAndAcademicYearAndSemester(studentId, academicYear, semester);
        List<MidtermSubjectGradeDto> subjectGrades = new ArrayList<>();

        double sumScores = 0.0;
        int passedCount = 0;
        int remedialCount = 0;

        for (Grade grade : grades) {
            Double assignmentAvg = assignmentService.getStudentAssignmentAverage(
                    studentId, grade.getSubject().getId(), academicYear, semester
            );

            double midterm = grade.getMidtermScore() != null ? grade.getMidtermScore() : 0.0;
            // Formula Nilai UTS: (Rata-rata Tugas * 40%) + (Nilai UTS * 60%)
            double finalMidtermScore = Math.round(((assignmentAvg * 0.4) + (midterm * 0.6)) * 100.0) / 100.0;
            String letterGrade = determineLetterGrade(finalMidtermScore);
            boolean isPassed = finalMidtermScore >= STANDARD_KKM;

            if (isPassed) passedCount++;
            else remedialCount++;

            sumScores += finalMidtermScore;

            subjectGrades.add(MidtermSubjectGradeDto.builder()
                    .subjectCode(grade.getSubject().getCode())
                    .subjectName(grade.getSubject().getName())
                    .teacherName(grade.getTeacher() != null ? grade.getTeacher().getFullName() : "-")
                    .assignmentAverage(assignmentAvg)
                    .midtermScore(midterm)
                    .finalMidtermScore(finalMidtermScore)
                    .letterGrade(letterGrade)
                    .isPassed(isPassed)
                    .build());
        }

        double totalAverage = subjectGrades.isEmpty() ? 0.0 : Math.round((sumScores / subjectGrades.size()) * 100.0) / 100.0;
        AttendanceSummaryDto attendance = attendanceService.getStudentAttendanceSummary(studentId, academicYear, semester);

        return MidtermReportResponseDto.builder()
                .student(StudentMapper.mapToStudentResponseDto(student))
                .academicYear(academicYear)
                .semester(semester)
                .subjectGrades(subjectGrades)
                .totalAverageScore(totalAverage)
                .totalSubjects(subjectGrades.size())
                .totalPassed(passedCount)
                .totalRemedial(remedialCount)
                .attendance(attendance)
                .build();
    }

    @Override
    public FinalReportResponseDto generateFinalReport(Long studentId, String academicYear, Integer semester) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Siswa", "id", studentId));

        List<Grade> grades = gradeRepository.findByStudentIdAndAcademicYearAndSemester(studentId, academicYear, semester);
        List<FinalSubjectGradeDto> subjectGrades = new ArrayList<>();

        double sumScores = 0.0;
        int passedCount = 0;
        int remedialCount = 0;

        for (Grade grade : grades) {
            Double assignmentAvg = assignmentService.getStudentAssignmentAverage(
                    studentId, grade.getSubject().getId(), academicYear, semester
            );

            double midterm = grade.getMidtermScore() != null ? grade.getMidtermScore() : 0.0;
            double finalExam = grade.getFinalScore() != null ? grade.getFinalScore() : 0.0;

            // Formula Rapor Semester: (Tugas * 30%) + (UTS * 30%) + (UAS * 40%)
            double totalScore = Math.round(((assignmentAvg * 0.3) + (midterm * 0.3) + (finalExam * 0.4)) * 100.0) / 100.0;
            String letterGrade = determineLetterGrade(totalScore);
            boolean isPassed = totalScore >= STANDARD_KKM;

            if (isPassed) passedCount++;
            else remedialCount++;

            sumScores += totalScore;

            subjectGrades.add(FinalSubjectGradeDto.builder()
                    .subjectCode(grade.getSubject().getCode())
                    .subjectName(grade.getSubject().getName())
                    .teacherName(grade.getTeacher() != null ? grade.getTeacher().getFullName() : "-")
                    .assignmentAverage(assignmentAvg)
                    .midtermScore(midterm)
                    .finalScore(finalExam)
                    .totalScore(totalScore)
                    .letterGrade(letterGrade)
                    .isPassed(isPassed)
                    .build());
        }

        double semesterAverage = subjectGrades.isEmpty() ? 0.0 : Math.round((sumScores / subjectGrades.size()) * 100.0) / 100.0;
        AttendanceSummaryDto attendance = attendanceService.getStudentAttendanceSummary(studentId, academicYear, semester);

        String academicStatus;
        if (remedialCount > 3) {
            academicStatus = "PERLU_REMEDIAL_INTENSIF";
        } else if (semester == 2 && student.getClassroom().getGradeLevel() == 9) {
            academicStatus = (semesterAverage >= STANDARD_KKM) ? "LULUS" : "BELUM_LULUS";
        } else if (semester == 2) {
            academicStatus = (remedialCount <= 2) ? "NAIK_KELAS" : "TINGGAL_KELAS";
        } else {
            academicStatus = "SEMESTER_GANJIL_SELESAI";
        }

        return FinalReportResponseDto.builder()
                .student(StudentMapper.mapToStudentResponseDto(student))
                .academicYear(academicYear)
                .semester(semester)
                .subjectGrades(subjectGrades)
                .semesterAverageScore(semesterAverage)
                .totalSubjects(subjectGrades.size())
                .totalPassed(passedCount)
                .totalRemedial(remedialCount)
                .academicStatus(academicStatus)
                .attendance(attendance)
                .build();
    }

    @Override
    public CumulativeTranscriptResponseDto generateCumulativeTranscript(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Siswa", "id", studentId));

        List<Grade> allGrades = gradeRepository.findByStudentId(studentId);
        List<Subject> allSubjects = subjectRepository.findAll();

        List<TranscriptSubjectDto> transcripts = new ArrayList<>();
        double totalCumulativeScore = 0.0;
        int evaluatedSubjectsCount = 0;

        for (Subject subject : allSubjects) {
            SemesterScoreDto g7 = new SemesterScoreDto();
            SemesterScoreDto g8 = new SemesterScoreDto();
            SemesterScoreDto g9 = new SemesterScoreDto();

            List<Double> availableSubjectScores = new ArrayList<>();

            for (Grade g : allGrades) {
                if (g.getSubject().getId().equals(subject.getId())) {
                    int gradeLevel = g.getClassroom().getGradeLevel();
                    int sem = g.getSemester();

                    Double assignmentAvg = assignmentService.getStudentAssignmentAverage(
                            studentId, subject.getId(), g.getAcademicYear(), sem
                    );
                    double m = g.getMidtermScore() != null ? g.getMidtermScore() : 0.0;
                    double f = g.getFinalScore() != null ? g.getFinalScore() : 0.0;
                    double finalSemScore = Math.round(((assignmentAvg * 0.3) + (m * 0.3) + (f * 0.4)) * 100.0) / 100.0;

                    availableSubjectScores.add(finalSemScore);

                    if (gradeLevel == 7) {
                        if (sem == 1) g7.setSemester1(finalSemScore);
                        else if (sem == 2) g7.setSemester2(finalSemScore);
                    } else if (gradeLevel == 8) {
                        if (sem == 1) g8.setSemester1(finalSemScore);
                        else if (sem == 2) g8.setSemester2(finalSemScore);
                    } else if (gradeLevel == 9) {
                        if (sem == 1) g9.setSemester1(finalSemScore);
                        else if (sem == 2) g9.setSemester2(finalSemScore);
                    }
                }
            }

            Double subjectAverage = availableSubjectScores.isEmpty()
                    ? 0.0
                    : Math.round(availableSubjectScores.stream().mapToDouble(Double::doubleValue).average().orElse(0.0) * 100.0) / 100.0;

            if (!availableSubjectScores.isEmpty()) {
                totalCumulativeScore += subjectAverage;
                evaluatedSubjectsCount++;
            }

            transcripts.add(TranscriptSubjectDto.builder()
                    .subjectCode(subject.getCode())
                    .subjectName(subject.getName())
                    .grade7(g7)
                    .grade8(g8)
                    .grade9(g9)
                    .cumulativeAverage(subjectAverage)
                    .letterGrade(determineLetterGrade(subjectAverage))
                    .build());
        }

        double overallAverage = evaluatedSubjectsCount > 0
                ? Math.round((totalCumulativeScore / evaluatedSubjectsCount) * 100.0) / 100.0
                : 0.0;

        String graduationStatus = (overallAverage >= STANDARD_KKM) ? "LULUS" : "BELUM_LULUS";

        return CumulativeTranscriptResponseDto.builder()
                .student(StudentMapper.mapToStudentResponseDto(student))
                .schoolLevel("SMP (Sekolah Menengah Pertama)")
                .transcripts(transcripts)
                .overallCumulativeAverage(overallAverage)
                .graduationStatus(graduationStatus)
                .build();
    }

    private String determineLetterGrade(double score) {
        if (score >= 85.0) return "A";
        if (score >= 75.0) return "B";
        if (score >= 60.0) return "C";
        if (score >= 50.0) return "D";
        return "E";
    }
}
