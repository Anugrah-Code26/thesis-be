package com.thesis.backend.service.thesis.impl;

import com.thesis.backend.common.exceptions.DataNotFoundException;
import com.thesis.backend.common.exceptions.UnauthorizedException;
import com.thesis.backend.entity.assessment.Exam;
import com.thesis.backend.entity.user.RoleType;
import com.thesis.backend.infrastructure.auth.Claims;
import com.thesis.backend.infrastructure.thesis.dto.ExamDTO;
import com.thesis.backend.infrastructure.thesis.mapper.ExamMapper;
import com.thesis.backend.infrastructure.thesis.repository.ExamRepository;
import com.thesis.backend.service.thesis.ExamService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExamServiceImpl implements ExamService {

    private final ExamRepository examRepository;

    @Override
    @Transactional
    public Exam createExam(ExamDTO examDTO) {
        if (!RoleType.ADMIN.equals(Claims.getRoleFromJwt())) {
            throw new UnauthorizedException("Only Admin can create exam!");
        }

        Exam exam = ExamMapper.toEntity(examDTO);

        examRepository.save(exam);

        return exam;
    }

    @Override
    @Transactional
    public Exam editExam(Long examId, ExamDTO examDTO) {
        if (!RoleType.ADMIN.equals(Claims.getRoleFromJwt())) {
            throw new UnauthorizedException("Only Admin can create exam!");
        }

        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new DataNotFoundException("Exam not found"));

        exam.setName(examDTO.getName());
        exam.setDescription(examDTO.getDescription());

        return examRepository.save(exam);
    }

    @Override
    @Transactional
    public List<ExamDTO> getAllExams() {

        List<Exam> exams = examRepository.findAll();
        return exams.stream()
                .map(ExamMapper::toDTO)
                .toList();
    }
}
