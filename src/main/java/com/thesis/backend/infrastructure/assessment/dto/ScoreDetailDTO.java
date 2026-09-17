package com.thesis.backend.infrastructure.assessment.dto;

import lombok.Data;

@Data
public class ScoreDetailDTO {
    private Long id;
    private Integer score;
    private String label;
    private String description;

    private Long assessmentRubricItemId;
}
