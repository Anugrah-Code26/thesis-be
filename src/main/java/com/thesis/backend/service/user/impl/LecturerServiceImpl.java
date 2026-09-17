package com.thesis.backend.service.user.impl;

import com.thesis.backend.entity.user.Lecturer;
import com.thesis.backend.infrastructure.user.dto.LecturerDTO;
import com.thesis.backend.infrastructure.user.mapper.LecturerMapper;
import com.thesis.backend.infrastructure.user.repository.LecturerRepository;
import com.thesis.backend.service.user.LecturerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LecturerServiceImpl implements LecturerService {

    private final LecturerRepository lecturerRepository;

    @Override
    @Transactional
    public List<LecturerDTO> getAllLecturer() {

        List<Lecturer> lecturers = lecturerRepository.findAll();
        return lecturers.stream()
                .map(LecturerMapper::toDTO)
                .toList();
    }
}
