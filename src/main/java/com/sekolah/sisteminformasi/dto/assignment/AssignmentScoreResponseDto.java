package com.sekolah.sisteminformasi.dto.assignment;

import com.sekolah.sisteminformasi.dto.student.StudentResponseDto;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssignmentScoreResponseDto {

    private Long id;
    private Long assignmentId;
    private String assignmentTitle;
    private StudentResponseDto student;
    private Double score;
    private String feedback;
    private LocalDateTime updatedAt;
}
