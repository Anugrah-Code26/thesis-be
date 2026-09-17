package com.thesis.backend.infrastructure.assessment.dto;

import com.thesis.backend.infrastructure.user.dto.LecturerDTO;
import lombok.Data;

@Data
public class AssessmentScoreDTO {
    private Long id;
    private Integer score;

    private Long thesisExamId;
    private LecturerDTO lecturer;
    private AssessmentRubricItemDTO assessmentRubricItem;
}
