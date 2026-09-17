package com.thesis.backend.infrastructure.assessment.dto;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class ReportDTO {
    private Long examId;
    private String studentName;
    private String thesisTitle;
    private String supervisorName;
    private String examinerName;
    private OffsetDateTime examDate;
    private Integer score;
    private String result;
}
