package com.sekolah.sisteminformasi.dto.student;

import com.sekolah.sisteminformasi.dto.classroom.ClassroomDto;
import com.sekolah.sisteminformasi.entity.Gender;
import com.sekolah.sisteminformasi.entity.StudentStatus;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentResponseDto {

    private Long id;
    private String nisn;
    private String nis;
    private String fullName;
    private Gender gender;
    private String birthPlace;
    private LocalDate birthDate;
    private String religion;
    private String address;
    private String parentName;
    private String parentPhone;
    private ClassroomDto classroom;
    private String admissionYear;
    private StudentStatus status;
}
