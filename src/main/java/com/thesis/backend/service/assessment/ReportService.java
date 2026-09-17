package com.thesis.backend.service.assessment;

import com.thesis.backend.infrastructure.assessment.dto.ReportDTO;

import java.time.LocalDate;
import java.util.List;

public interface ReportService {
    List<ReportDTO> generateExamReports(LocalDate startDate, LocalDate endDate);
}
