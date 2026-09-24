package com.sekolah.sisteminformasi.config;

import com.sekolah.sisteminformasi.entity.*;
import com.sekolah.sisteminformasi.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;
    private final ClassroomRepository classroomRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final ScheduleRepository scheduleRepository;
    private final AssignmentRepository assignmentRepository;
    private final AssignmentScoreRepository assignmentScoreRepository;
    private final AttendanceRepository attendanceRepository;
    private final GradeRepository gradeRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            log.info("Database sudah memiliki data awal, melewati seed data.");
            return;
        }

        log.info("Sedang mengisi data awal (Seeding Default Demo Data)...");

        // 1. Seed Users (Admin & Guru)
        User adminUser = userRepository.save(User.builder()
                .username("admin")
                .email("admin@sekolah.com")
                .password(passwordEncoder.encode("admin123"))
                .fullName("Administrator Sistem")
                .role(Role.ROLE_ADMIN)
                .isActive(true)
                .build());

        User teacherUser1 = userRepository.save(User.builder()
                .username("guru_budi")
                .email("budi.santoso@sekolah.com")
                .password(passwordEncoder.encode("guru123"))
                .fullName("Budi Santoso, S.Pd")
                .role(Role.ROLE_TEACHER)
                .isActive(true)
                .build());

        User teacherUser2 = userRepository.save(User.builder()
                .username("guru_siti")
                .email("siti.rahma@sekolah.com")
                .password(passwordEncoder.encode("guru123"))
                .fullName("Siti Rahma, M.Sc")
                .role(Role.ROLE_TEACHER)
                .isActive(true)
                .build());

        // 2. Seed Subjects
        Subject mtk = subjectRepository.save(Subject.builder()
                .code("MTK-01")
                .name("Matematika")
                .description("Matematika Wajib Kurikulum Nasional")
                .creditHours(4)
                .build());

        Subject ipa = subjectRepository.save(Subject.builder()
                .code("IPA-01")
                .name("Ilmu Pengetahuan Alam")
                .description("Fisika & Biologi Terpadu")
                .creditHours(4)
                .build());

        Subject indo = subjectRepository.save(Subject.builder()
                .code("BIN-01")
                .name("Bahasa Indonesia")
                .description("Tata Bahasa & Sastra")
                .creditHours(3)
                .build());

        Subject inggris = subjectRepository.save(Subject.builder()
                .code("ENG-01")
                .name("Bahasa Inggris")
                .description("English Communication & Grammar")
                .creditHours(3)
                .build());

        // 3. Seed Classrooms
        Classroom kls7a = classroomRepository.save(Classroom.builder()
                .code("7A")
                .name("Kelas 7A")
                .gradeLevel(7)
                .academicYear("2026/2027")
                .build());

        Classroom kls8a = classroomRepository.save(Classroom.builder()
                .code("8A")
                .name("Kelas 8A")
                .gradeLevel(8)
                .academicYear("2026/2027")
                .build());

        Classroom kls9a = classroomRepository.save(Classroom.builder()
                .code("9A")
                .name("Kelas 9A")
                .gradeLevel(9)
                .academicYear("2026/2027")
                .build());

        // 4. Seed Teachers
        Teacher teacher1 = teacherRepository.save(Teacher.builder()
                .nip("198501152010011001")
                .fullName("Budi Santoso, S.Pd")
                .email("budi.santoso@sekolah.com")
                .phone("081234567890")
                .gender("L")
                .address("Jl. Pendidikan No. 10")
                .subject(mtk)
                .user(teacherUser1)
                .isActive(true)
                .build());

        Teacher teacher2 = teacherRepository.save(Teacher.builder()
                .nip("199003202015022002")
                .fullName("Siti Rahma, M.Sc")
                .email("siti.rahma@sekolah.com")
                .phone("081298765432")
                .gender("P")
                .address("Jl. Melati No. 25")
                .subject(ipa)
                .user(teacherUser2)
                .isActive(true)
                .build());

        // 5. Seed Students
        Student student1 = studentRepository.save(Student.builder()
                .nisn("0081234567")
                .nis("242507001")
                .fullName("Herin Amanda")
                .gender(Gender.P)
                .birthPlace("Jakarta")
                .birthDate(LocalDate.of(2012, 5, 14))
                .religion("Islam")
                .address("Jl. Cemara No. 12")
                .parentName("Bambang")
                .parentPhone("081311223344")
                .classroom(kls7a)
                .admissionYear("2026")
                .status(StudentStatus.ACTIVE)
                .build());

        Student student2 = studentRepository.save(Student.builder()
                .nisn("0081234568")
                .nis("242507002")
                .fullName("Budi Pratama")
                .gender(Gender.L)
                .birthPlace("Bandung")
                .birthDate(LocalDate.of(2012, 8, 20))
                .religion("Islam")
                .address("Jl. Kenanga No. 8")
                .parentName("Dedi")
                .parentPhone("081355667788")
                .classroom(kls7a)
                .admissionYear("2026")
                .status(StudentStatus.ACTIVE)
                .build());

        // 6. Seed Schedule (Matematika di Kelas 7A Hari Senin 07:30 - 09:00)
        Schedule sched1 = scheduleRepository.save(Schedule.builder()
                .teacher(teacher1)
                .subject(mtk)
                .classroom(kls7a)
                .dayOfWeek(DayOfWeekEnum.SENIN)
                .startTime(LocalTime.of(7, 30))
                .endTime(LocalTime.of(9, 0))
                .academicYear("2026/2027")
                .semester(1)
                .build());

        // 7. Seed Assignment & Score
        Assignment assign1 = assignmentRepository.save(Assignment.builder()
                .title("Tugas 1: Bilangan Bulat & Pecahan")
                .description("Kerjakan soal latihan halaman 24 nomor 1-10")
                .dueDate(LocalDate.now().plusDays(5))
                .subject(mtk)
                .classroom(kls7a)
                .teacher(teacher1)
                .academicYear("2026/2027")
                .semester(1)
                .build());

        assignmentScoreRepository.save(AssignmentScore.builder()
                .assignment(assign1)
                .student(student1)
                .score(88.0)
                .feedback("Pengerjaan sangat rapi dan tepat")
                .build());

        assignmentScoreRepository.save(AssignmentScore.builder()
                .assignment(assign1)
                .student(student2)
                .score(80.0)
                .feedback("Bagus, teliti kembali nomor 7")
                .build());

        // 8. Seed Attendance
        attendanceRepository.save(Attendance.builder()
                .schedule(sched1)
                .student(student1)
                .attendanceDate(LocalDate.now())
                .status(AttendanceStatus.HADIR)
                .remarks("Hadir tepat waktu")
                .build());

        attendanceRepository.save(Attendance.builder()
                .schedule(sched1)
                .student(student2)
                .attendanceDate(LocalDate.now())
                .status(AttendanceStatus.HADIR)
                .remarks("Hadir tepat waktu")
                .build());

        // 9. Seed Grades (UTS & UAS)
        gradeRepository.save(Grade.builder()
                .student(student1)
                .subject(mtk)
                .teacher(teacher1)
                .classroom(kls7a)
                .academicYear("2026/2027")
                .semester(1)
                .midtermScore(85.0)
                .finalScore(90.0)
                .build());

        gradeRepository.save(Grade.builder()
                .student(student1)
                .subject(ipa)
                .teacher(teacher2)
                .classroom(kls7a)
                .academicYear("2026/2027")
                .semester(1)
                .midtermScore(88.0)
                .finalScore(92.0)
                .build());

        log.info("Data awal (Seed Demo Data) berhasil dimasukkan ke database!");
    }
}
