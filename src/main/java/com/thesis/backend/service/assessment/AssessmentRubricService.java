package com.thesis.backend.service.assessment;

import com.thesis.backend.entity.assessment.AssessmentRubric;
import com.thesis.backend.infrastructure.assessment.dto.AssessmentRubricDTO;

import java.util.List;

public interface AssessmentRubricService {
    AssessmentRubric createAssessmentRubricByExam(Long examId, AssessmentRubricDTO assessmentRubricDTO);
    List<AssessmentRubricDTO> getAssessmentRubricByExam(Long examId);
    AssessmentRubric editAssessmentRubric(Long assessmentRubricId, AssessmentRubricDTO assessmentRubricDTO);
}
