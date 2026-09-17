package com.thesis.backend.infrastructure.assessment.dto;

import lombok.Data;

import java.util.List;

@Data
public class AssessmentRubricItemDTO {
    private Long id;
    private String name;
    private String description;
    private Integer maxScore;

    private Long assessmentRubricId;
    private List<ScoreDetailDTO> scoreDetails;
}
