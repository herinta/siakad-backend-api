package com.sekolah.sisteminformasi.dto.subject;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubjectDto {

    private Long id;

    @NotBlank(message = "Kode mata pelajaran tidak boleh kosong")
    private String code;

    @NotBlank(message = "Nama mata pelajaran tidak boleh kosong")
    private String name;

    private String description;

    @NotNull(message = "Jumlah jam pelajaran wajib diisi")
    @Min(value = 1, message = "Jumlah jam pelajaran minimal 1 jam")
    private Integer creditHours;
}
