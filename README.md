# 🎓 Sistem Informasi Akademik Sekolah (Backend REST API)

Backend REST API Enterprise untuk **Sistem Informasi Akademik Sekolah** yang dibangun dengan **Java 17**, **Spring Boot 4.x**, **Spring Security 6**, **JWT (JSON Web Token)**, **Spring Data JPA & Hibernate**, **H2 / MySQL**, dan didokumentasikan interaktif via **Swagger OpenAPI 3**.

---

## 🌟 Fitur Unggulan Arsitektur Backend:

1. 🛡️ **Autentikasi & Otorisasi RBAC (Spring Security + JWT):**
   * Role: `ROLE_ADMIN` dan `ROLE_TEACHER`.
   * Enkripsi kata sandi menggunakan `BCryptPasswordEncoder`.
   * Stateless Session & Filter Interceptor Token `JwtAuthenticationFilter`.
2. 🏛️ **Manajemen Master Data Lengkap:**
   * Mata Pelajaran (`Subject`), Kelas (`Classroom`), Profil Guru (`Teacher`), dan Siswa (`Student`).
   * Pencegahan **N+1 Query Problem** menggunakan `@EntityGraph` pada JPA Repository.
3. ⏰ **Algoritma Penjadwalan Cerdas (*Overlapping Interval Detection*):**
   * Validasi matematika interval `(StartTimeA < EndTimeB && EndTimeA > StartTimeB)` untuk mencegah tabrakan jam mengajar guru maupun pemakaian ruang kelas.
4. 📝 **Operasional Akademik Harian Guru:**
   * Manajemen Penugasan (`Assignment`) & Input Nilai Tugas (`AssignmentScore`).
   * Presensi Kehadiran Siswa Massal (`Attendance`) dengan **`@Transactional` ACID Batch Processing**.
   * Pencatatan Nilai Murni Ujian Tengah Semester (UTS) dan Ujian Akhir Semester (UAS).
5. 🏆 **Mesin Kalkulasi Rapor Otomatis & Transkrip Kumulatif:**
   * **Rapor UTS:** Auto-calculate bobot (Tugas 40% + UTS 60%) + Rekap Kehadiran.
   * **Rapor Akhir Semester:** Auto-calculate bobot (Tugas 30% + UTS 30% + UAS 40%), penentuan huruf mutu (*A, B, C, D*), dan status KKM.
   * **Transkrip Nilai Kumulatif SMP:** Agregasi nilai multi-semester (Kelas 7, 8, dan 9 / Semester 1 s/d 6) untuk penerbitan Ijazah.
6. 🧪 **Automated Testing:**
   * Unit Test menggunakan **JUnit 5** dan **Mockito**.

---

## 🚀 Cara Menjalankan Aplikasi

Jalankan perintah berikut di terminal:
```powershell
.\mvnw.cmd spring-boot:run
```

Aplikasi akan aktif di `http://localhost:8080`.

---

## 📖 Dokumentasi & Testing API (Swagger UI & H2 Console)

| Layanan | URL Browser | Keterangan |
| :--- | :--- | :--- |
| 🌐 **Swagger OpenAPI UI** | `http://localhost:8080/swagger-ui/index.html` | Dokumentasi dan uji coba seluruh endpoint REST API secara interaktif |
| 🗄️ **H2 Database Console** | `http://localhost:8080/h2-console` | JDBC URL: `jdbc:h2:mem:sisteminformasidb`, User: `sa`, Password: *(kosong)* |

---

## 🔑 Akun Demo Bawaan (Auto-Seeded)

Saat aplikasi pertama kali dijalankan, sistem otomatis membuat akun demo berikut:

| Role | Username | Password | Keterangan |
| :--- | :--- | :--- | :--- |
| 👑 **ADMIN** | `admin` | `admin123` | Akses penuh seluruh master data, kelas, guru, dan jadwal |
| 👨‍🏫 **GURU (MTK)** | `guru_budi` | `guru123` | Guru Matematika (Budi Santoso, S.Pd) |
| 👩‍🏫 **GURU (IPA)** | `guru_siti` | `guru123` | Guru IPA (Siti Rahma, M.Sc) |

---

## 📑 Dokumentasi Desain Sistem
* Blueprint ERD & Flowchart: [docs/SYSTEM_DESIGN.md](docs/SYSTEM_DESIGN.md)
* Roadmap Task Eksekusi: [docs/TASK_ROADMAP.md](docs/TASK_ROADMAP.md)
