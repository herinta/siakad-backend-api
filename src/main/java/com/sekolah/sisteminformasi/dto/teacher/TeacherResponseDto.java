package com.sekolah.sisteminformasi.dto.teacher;

import com.sekolah.sisteminformasi.dto.subject.SubjectDto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeacherResponseDto {

    private Long id;
    private String nip;
    private String fullName;
    private String email;
    private String phone;
    private String gender;
    private String address;
    private SubjectDto subject;
    private Long userId;
    private String username;
    private Boolean isActive;
}
