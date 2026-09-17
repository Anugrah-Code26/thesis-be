package com.thesis.backend.service.thesis;

import com.thesis.backend.entity.assessment.Exam;
import com.thesis.backend.infrastructure.thesis.dto.ExamDTO;

import java.util.List;

public interface ExamService {
    Exam createExam(ExamDTO examDTO);
    Exam editExam(Long examId, ExamDTO examDTO);
    List<ExamDTO> getAllExams();
}
