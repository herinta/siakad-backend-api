# 📚 DOKUMENTASI DESAIN SISTEM INFORMASI SEKOLAH (BACKEND REST API)

Dokumentasi ini berisi cetak biru (*blueprint*) arsitektur backend, **Entity Relationship Diagram (ERD)**, **Flowchart**, **Activity Diagram**, dan **Daftar Endpoint REST API**.

---

## 1. 🗄️ Entity Relationship Diagram (ERD)

Diagram relasi antar tabel dalam database:

```mermaid
erDiagram
    TEACHER ||--o{ SCHEDULE : "mengajar di"
    SUBJECT ||--o{ SCHEDULE : "diajarkan dalam"
    CLASSROOM ||--o{ SCHEDULE : "tempat jadwal"
    CLASSROOM ||--o{ STUDENT : "memiliki siswa"
    STUDENT ||--o{ ATTENDANCE : "memiliki catatan"
    SCHEDULE ||--o{ ATTENDANCE : "sesi absensi"
    STUDENT ||--o{ GRADE : "memiliki nilai"
    SUBJECT ||--o{ GRADE : "nilai untuk mapel"
    TEACHER ||--o{ GRADE : "diinput oleh"

    TEACHER {
        Long id PK
        String nip UK "Nomor Induk Pegawai"
        String fullName "Nama Lengkap"
        String email UK
        String phone
        String gender "L / P"
        String address
        Boolean isActive
    }

    SUBJECT {
        Long id PK
        String code UK "Contoh: MTK-01"
        String name "Matematika"
        String description
        Integer creditHours "Jumlah Jam Pelajaran"
    }

    CLASSROOM {
        Long id PK
        String code UK "Contoh: X-IPA-1"
        String name "10 IPA 1"
        Integer gradeLevel "10, 11, 12"
        String academicYear "2026/2027"
    }

    STUDENT {
        Long id PK
        Long classroomId FK
        String nisn UK "Nomor Induk Siswa Nasional"
        String fullName "Nama Lengkap Siswa"
        String gender "L / P"
        String birthDate
        String status "ACTIVE, GRADUATED, DROPOUT"
    }

    SCHEDULE {
        Long id PK
        Long teacherId FK
        Long subjectId FK
        Long classroomId FK
        String dayOfWeek "MONDAY, TUESDAY, etc."
        Time startTime "07:30:00"
        Time endTime "09:00:00"
        String academicYear "2026/2027"
        Integer semester "1 / 2"
    }

    ATTENDANCE {
        Long id PK
        Long scheduleId FK
        Long studentId FK
        Date attendanceDate "YYYY-MM-DD"
        String status "HADIR, SAKIT, IZIN, ALPA"
        String remarks "Catatan tambahan"
    }

    GRADE {
        Long id PK
        Long studentId FK
        Long subjectId FK
        Long teacherId FK
        String semester "1 / 2"
        String academicYear "2026/2027"
        Double assignmentScore "Nilai Tugas (30%)"
        Double midtermScore "Nilai UTS (30%)"
        Double finalScore "Nilai UAS (40%)"
        Double totalScore "Nilai Akhir Rata-rata"
        String letterGrade "A, B, C, D, E"
    }
```

---

## 2. 🏗️ Flowchart Arsitektur Backend (Layered Architecture)

Alur pergerakan data dari Request Client hingga masuk ke Database dan kembali sebagai Response:

```mermaid
flowchart TD
    Client([Client / Postman / Swagger]) -->|HTTP Request JSON| Controller[REST Controller]
    
    subgraph Spring Boot Backend
        Controller -->|Validasi Input @Valid| DTO_In[Request DTO]
        DTO_In --> Controller
        Controller -->|Kirim DTO| Service[Service Layer - Business Logic]
        
        Service -->|Panggil Mapper| Mapper[Mapper Layer]
        Mapper -->|Konversi DTO to Entity| Entity[JPA Entity]
        
        Service -->|Jalankan Validasi & Rule Bisnis| Validation{Validasi Bisnis OK?}
        Validation -->|Tidak - Ada Konflik / Data Invalid| Exception[Throw Custom Exception]
        Exception --> GlobalHandler[Global Exception Handler @RestControllerAdvice]
        
        Validation -->|Ya - Data Valid| Repository[Spring Data JPA Repository]
        Repository -->|Hibernate Query SQL| DB[(Database H2 / MySQL)]
        DB -->|Kembalikan Hasil Query| Repository
        Repository -->|Kembalikan Entity| Service
        
        Service -->|Panggil Mapper| MapperResponse[Mapper Layer]
        MapperResponse -->|Konversi Entity to Response DTO| DTO_Out[Response DTO]
        DTO_Out --> Service
        Service -->|Return DTO| Controller
    end
    
    Controller -->|HTTP 200/201 Response JSON| Client
    GlobalHandler -->|HTTP 400/404/409 Error Response JSON| Client
```

---

## 3. 🔄 Activity Diagrams

### A. Activity Diagram: Tambah Jadwal Pelajaran (Dengan Validasi Konflik)
Fitur unggulan: Otomatis mendeteksi jika guru atau kelas sudah memiliki jadwal mengajar di hari dan rentang jam yang sama.

```mermaid
stateDiagram-v2
    [*] --> AdminMengisiFormJadwal
    AdminMengisiFormJadwal --> RequestKirimJadwal: POST /api/schedules
    RequestKirimJadwal --> ValidasiDataDasar: Cek keberadaan Guru, Mapel, Kelas

    state ValidasiDataDasar {
        [*] --> CekId
        CekId --> DataDitemukan: Guru & Kelas Valid
        CekId --> DataTidakAda: Salah Satu ID Invalid
    }

    DataTidakAda --> Return404NotFound: ResourceNotFoundException
    Return404NotFound --> [*]

    DataDitemukan --> CekKonflikJadwalGuru: Cek jadwal bentrok guru di Hari & Jam tsb
    
    state CekKonflikJadwalGuru {
        [*] --> QueryGuruSchedule
        QueryGuruSchedule --> GuruSibuk: Guru sudah mengajar di kelas lain
        QueryGuruSchedule --> GuruFree: Guru siap
    }

    GuruSibuk --> Return409Conflict: ScheduleConflictException ("Guru sedang mengajar di jam tersebut")
    Return409Conflict --> [*]

    GuruFree --> CekKonflikRuangan: Cek kelas/ruangan di Hari & Jam tsb

    state CekKonflikRuangan {
        [*] --> QueryRoomSchedule
        QueryRoomSchedule --> RuanganPenuh: Kelas sedang dipakai mapel lain
        QueryRoomSchedule --> RuanganKosong: Ruangan siap
    }

    RuanganPenuh --> Return409Conflict: ScheduleConflictException ("Kelas sudah ada jadwal lain")
    
    RuanganKosong --> SimpanJadwalKeDB: Repository.save(schedule)
    SimpanJadwalKeDB --> ResponseSuccess201: Return ScheduleResponseDto (Status 201 Created)
    ResponseSuccess201 --> [*]
```

---

### B. Activity Diagram: Input & Rekap Nilai Siswa (Oleh Guru)

```mermaid
stateDiagram-v2
    [*] --> GuruMemilihKelasDanMapel
    GuruMemilihKelasDanMapel --> TampilkanDaftarSiswa: GET /api/students/classroom/{classId}
    TampilkanDaftarSiswa --> GuruInputNilai: Input Nilai Tugas, UTS, UAS per Siswa
    GuruInputNilai --> KirimNilai: POST / PUT /api/grades
    
    state LogikaHitungNilai {
        [*] --> HitungTotalScore: (Tugas*0.3) + (UTS*0.3) + (UAS*0.4)
        HitungTotalScore --> TentukanHurufMutu: >=85:A, >=75:B, >=60:C, dst.
    }
    
    KirimNilai --> LogikaHitungNilai
    LogikaHitungNilai --> SimpanNilaiDB: gradeRepository.save(grade)
    SimpanNilaiDB --> ResponseNilaiTersimpan: 200 OK (Nilai Akhir & Predikat Terbit)
    ResponseNilaiTersimpan --> [*]
```

---

## 4. 📡 Rencana Spesifikasi Endpoint REST API

| Modul | Method | Endpoint | Deskripsi | Status Code |
| :--- | :--- | :--- | :--- | :--- |
| **Subjects** | `POST` | `/api/subjects` | Menambah mata pelajaran baru | `201 Created` |
| | `GET` | `/api/subjects` | Mengambil seluruh mata pelajaran | `200 OK` |
| | `GET` | `/api/subjects/{id}` | Mengambil detail mata pelajaran | `200 OK` |
| | `PUT` | `/api/subjects/{id}` | Mengubah data mata pelajaran | `200 OK` |
| | `DELETE` | `/api/subjects/{id}` | Menghapus mata pelajaran | `204 No Content` |
| **Teachers** | `POST` | `/api/teachers` | Menambah data guru baru | `201 Created` |
| | `GET` | `/api/teachers` | Mengambil semua guru (Support filter/search) | `200 OK` |
| | `GET` | `/api/teachers/{id}` | Mengambil detail profil guru | `200 OK` |
| | `PUT` | `/api/teachers/{id}` | Memperbarui data guru | `200 OK` |
| | `DELETE` | `/api/teachers/{id}` | Menghapus guru | `204 No Content` |
| **Classrooms** | `POST` | `/api/classrooms` | Membuat kelas baru | `201 Created` |
| | `GET` | `/api/classrooms` | Mengambil daftar semua kelas | `200 OK` |
| | `GET` | `/api/classrooms/{id}` | Detail kelas beserta daftar siswanya | `200 OK` |
| **Schedules** | `POST` | `/api/schedules` | Membuat jadwal baru (+ Validasi tabrakan) | `201 Created` / `409 Conflict` |
| | `GET` | `/api/schedules` | Mengambil semua jadwal | `200 OK` |
| | `GET` | `/api/schedules/teacher/{teacherId}` | Mengambil jadwal mengajar guru tertentu | `200 OK` |
| | `GET` | `/api/schedules/classroom/{classroomId}` | Mengambil jadwal kelas tertentu | `200 OK` |
| **Students** | `POST` | `/api/students` | Mendaftarkan siswa ke kelas | `201 Created` |
| | `GET` | `/api/students` | Mengambil semua siswa (Support search nama/nisn) | `200 OK` |
| | `GET` | `/api/students/{id}` | Detail profil lengkap siswa | `200 OK` |
| | `GET` | `/api/students/classroom/{classroomId}` | Daftar siswa dalam kelas tertentu | `200 OK` |
| | `PUT` | `/api/students/{id}` | Memperbarui data siswa | `200 OK` |
| | `DELETE` | `/api/students/{id}` | Menghapus data siswa (Soft delete) | `204 No Content` |
| **Assignments** | `POST` | `/api/assignments` | Guru membuat tugas baru | `201 Created` |
| | `POST` | `/api/assignments/{id}/scores` | Input nilai tugas siswa | `200 OK` |
| **Grades** | `POST` | `/api/grades` | Input nilai UTS & UAS per mapel | `201 Created` |
| | `GET` | `/api/grades/student/{studentId}` | Mengambil semua nilai mentah siswa | `200 OK` |
| **Attendance** | `POST` | `/api/attendances/batch` | Input absensi massal satu kelas | `201 Created` |
| | `GET` | `/api/attendances/schedule/{scheduleId}` | Rekap absensi sesi jadwal | `200 OK` |
| **Reports & Transkrip** 🌟 | `GET` | `/api/reports/midterm` | **Rapor UTS** (Nilai Tugas + UTS + Absensi) | `200 OK` |
| | `GET` | `/api/reports/final` | **Rapor Akhir Semester** (Tugas + UTS + UAS + Predikat) | `200 OK` |
| | `GET` | `/api/reports/transcript/{studentId}` | **Transkrip Nilai Kumulatif** (Kelas 1 s/d 3 / Seluruh Semester) | `200 OK` |

---

Dokumentasi ini menjadi panduan baku selama proses pengerjaan kode backend.
