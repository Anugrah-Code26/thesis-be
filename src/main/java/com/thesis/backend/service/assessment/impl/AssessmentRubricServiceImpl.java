package com.thesis.backend.service.assessment.impl;

import com.thesis.backend.common.exceptions.UnauthorizedException;
import com.thesis.backend.entity.assessment.AssessmentRubric;
import com.thesis.backend.entity.assessment.Exam;
import com.thesis.backend.entity.user.RoleType;
import com.thesis.backend.infrastructure.assessment.dto.AssessmentRubricDTO;
import com.thesis.backend.infrastructure.assessment.mapper.AssessmentRubricMapper;
import com.thesis.backend.infrastructure.assessment.repository.AssessmentRubricRepository;
import com.thesis.backend.infrastructure.auth.Claims;
import com.thesis.backend.infrastructure.thesis.repository.ExamRepository;
import com.thesis.backend.service.assessment.AssessmentRubricService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssessmentRubricServiceImpl implements AssessmentRubricService {
    private final AssessmentRubricRepository rubricRepository;
    private final ExamRepository examRepository;

    @Override
    @Transactional
    public AssessmentRubric createAssessmentRubricByExam(Long examId, AssessmentRubricDTO dto) {
        if (!RoleType.ADMIN.equals(Claims.getRoleFromJwt())) {
            throw new UnauthorizedException("Only Admin can create assessment rubric!");
        }

        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new RuntimeException("Exam not found"));

        AssessmentRubric rubric = AssessmentRubricMapper.toEntity(dto);
        rubric.setExam(exam);

        return rubricRepository.save(rubric);
    }

    @Override
    @Transactional
    public List<AssessmentRubricDTO> getAssessmentRubricByExam(Long examId) {
        if (RoleType.STUDENT.equals(Claims.getRoleFromJwt())) {
            throw new UnauthorizedException("Student cannot see assessment rubric!");
        }

        return rubricRepository.findByExamId(examId).stream()
                .map(AssessmentRubricMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AssessmentRubric editAssessmentRubric(Long rubricId, AssessmentRubricDTO dto) {
        if (!RoleType.ADMIN.equals(Claims.getRoleFromJwt())) {
            throw new UnauthorizedException("Only Admin can create assessment rubric!");
        }

        AssessmentRubric existing = rubricRepository.findById(rubricId)
                .orElseThrow(() -> new RuntimeException("Rubric not found"));

        AssessmentRubricMapper.softUpdateEntityFromDTO(existing, dto);

        return rubricRepository.save(existing);
    }

}
