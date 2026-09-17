package com.thesis.backend.infrastructure.assessment.dto;

import com.thesis.backend.entity.assessment.AssessmentRole;
import com.thesis.backend.infrastructure.thesis.dto.ExamDTO;
import lombok.Data;

import java.util.List;

@Data
public class AssessmentRubricDTO {
    private Long id;
    private String name;
    private String description;
    private AssessmentRole assessmentRole;

    private ExamDTO exam;
    private List<AssessmentRubricItemDTO> assessmentRubricItems;
}
