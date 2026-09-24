package com.sekolah.sisteminformasi.dto.student;

import com.sekolah.sisteminformasi.entity.Gender;
import com.sekolah.sisteminformasi.entity.StudentStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentRequestDto {

    @NotBlank(message = "NISN tidak boleh kosong")
    @Size(min = 10, max = 10, message = "NISN harus tepat 10 digit")
    private String nisn;

    @NotBlank(message = "NIS tidak boleh kosong")
    private String nis;

    @NotBlank(message = "Nama lengkap siswa tidak boleh kosong")
    private String fullName;

    @NotNull(message = "Jenis kelamin wajib dipilih (L / P)")
    private Gender gender;

    private String birthPlace;
    private LocalDate birthDate;
    private String religion;
    private String address;

    private String parentName;
    private String parentPhone;

    @NotNull(message = "Kelas wajib dipilih")
    private Long classroomId;

    @NotBlank(message = "Tahun masuk / angkatan tidak boleh kosong (contoh: 2024)")
    private String admissionYear;

    @Builder.Default
    private StudentStatus status = StudentStatus.ACTIVE;
}
