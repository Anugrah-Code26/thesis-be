package com.thesis.backend.infrastructure.assessment.controller;

import com.thesis.backend.common.responses.ApiResponse;
import com.thesis.backend.infrastructure.assessment.dto.ReportDTO;
import com.thesis.backend.service.assessment.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ReportDTO>>> generateReports(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            List<ReportDTO> reports = reportService.generateExamReports(startDate, endDate);
            return ApiResponse.success("Reports generated successfully", reports);
        } catch (Exception e) {
            return ApiResponse.failed(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }
}
