package com.sekolah.sisteminformasi.service.impl;

import com.sekolah.sisteminformasi.dto.subject.SubjectDto;
import com.sekolah.sisteminformasi.entity.Subject;
import com.sekolah.sisteminformasi.exception.BadRequestException;
import com.sekolah.sisteminformasi.exception.ResourceNotFoundException;
import com.sekolah.sisteminformasi.mapper.SubjectMapper;
import com.sekolah.sisteminformasi.repository.SubjectRepository;
import com.sekolah.sisteminformasi.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubjectServiceImpl implements SubjectService {

    private final SubjectRepository subjectRepository;

    @Override
    @Transactional
    public SubjectDto createSubject(SubjectDto subjectDto) {
        if (subjectRepository.existsByCode(subjectDto.getCode())) {
            throw new BadRequestException("Kode mata pelajaran '" + subjectDto.getCode() + "' sudah ada!");
        }

        Subject subject = SubjectMapper.mapToSubject(subjectDto);
        Subject savedSubject = subjectRepository.save(subject);
        return SubjectMapper.mapToSubjectDto(savedSubject);
    }

    @Override
    public SubjectDto getSubjectById(Long id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mata Pelajaran", "id", id));
        return SubjectMapper.mapToSubjectDto(subject);
    }

    @Override
    public List<SubjectDto> getAllSubjects() {
        List<Subject> subjects = subjectRepository.findAll();
        return subjects.stream()
                .map(SubjectMapper::mapToSubjectDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public SubjectDto updateSubject(Long id, SubjectDto subjectDto) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mata Pelajaran", "id", id));

        if (!subject.getCode().equalsIgnoreCase(subjectDto.getCode()) && subjectRepository.existsByCode(subjectDto.getCode())) {
            throw new BadRequestException("Kode mata pelajaran '" + subjectDto.getCode() + "' sudah digunakan!");
        }

        subject.setCode(subjectDto.getCode());
        subject.setName(subjectDto.getName());
        subject.setDescription(subjectDto.getDescription());
        subject.setCreditHours(subjectDto.getCreditHours());

        Subject updatedSubject = subjectRepository.save(subject);
        return SubjectMapper.mapToSubjectDto(updatedSubject);
    }

    @Override
    @Transactional
    public void deleteSubject(Long id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mata Pelajaran", "id", id));
        subjectRepository.delete(subject);
    }
}
