package com.thesis.backend.service.assessment.impl;

import com.thesis.backend.infrastructure.assessment.dto.ReportDTO;
import com.thesis.backend.infrastructure.thesis.repository.ThesisExamRepository;
import com.thesis.backend.service.assessment.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ThesisExamRepository examRepository;

    public List<ReportDTO> generateExamReports(LocalDate startDate, LocalDate endDate) {
        OffsetDateTime start = startDate.atStartOfDay().atOffset(OffsetDateTime.now().getOffset());
        OffsetDateTime end = endDate.atTime(LocalTime.MAX).atOffset(OffsetDateTime.now().getOffset());

        return examRepository.findByScheduledDateBetween(start, end).stream()
                .map(exam -> {
                    ReportDTO report = new ReportDTO();
                    report.setExamId(exam.getId());
                    report.setStudentName(exam.getThesis().getStudent().getName());
                    report.setThesisTitle(exam.getThesis().getTitle());
//                    report.setSupervisorName(exam.getThesis().getLecturer().getName());
//                    report.setExaminerName(exam.getLecturer().getName());
                    report.setExamDate(exam.getScheduledDate());

//                    double averageScore = exam.getEvaluations().stream()
//                            .mapToInt(AssessmentScore::getScore)
//                            .average()
//                            .orElse(0.0);
//
//                    report.setScore((int) averageScore);
//                    report.setResult(averageScore >= 70 ? "PASS" : "FAIL");

                    return report;
                })
                .collect(Collectors.toList());
    }
}
