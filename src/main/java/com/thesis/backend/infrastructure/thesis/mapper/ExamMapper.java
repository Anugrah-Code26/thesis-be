package com.thesis.backend.infrastructure.thesis.mapper;

import com.thesis.backend.entity.assessment.Exam;
import com.thesis.backend.infrastructure.thesis.dto.ExamDTO;

public class ExamMapper {

    public static Exam toEntity(ExamDTO dto) {
        Exam exam = new Exam();
        exam.setName(dto.getName());
        exam.setDescription(dto.getDescription());

        return exam;
    }

    public static ExamDTO toDTO(Exam exam) {
        ExamDTO dto = new ExamDTO();
        dto.setId(exam.getId());
        dto.setName(exam.getName());
        dto.setDescription(exam.getDescription());

        return dto;
    }
}
