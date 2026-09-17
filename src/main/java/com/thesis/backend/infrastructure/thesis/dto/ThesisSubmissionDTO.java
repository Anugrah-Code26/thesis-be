package com.thesis.backend.infrastructure.thesis.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ThesisSubmissionDTO {
    private Long thesisId;
    private MultipartFile thesisFile;
}
