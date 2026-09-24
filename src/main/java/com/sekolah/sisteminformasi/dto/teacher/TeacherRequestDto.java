package com.sekolah.sisteminformasi.dto.teacher;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeacherRequestDto {

    @NotBlank(message = "NIP tidak boleh kosong")
    private String nip;

    @NotBlank(message = "Nama lengkap guru tidak boleh kosong")
    private String fullName;

    @NotBlank(message = "Email tidak boleh kosong")
    @Email(message = "Format email tidak valid")
    private String email;

    private String phone;
    private String gender; // L / P
    private String address;

    private Long subjectId; // ID Spesialisasi Mata Pelajaran
    private Long userId;    // ID Akun User terkait (opsional)
}
