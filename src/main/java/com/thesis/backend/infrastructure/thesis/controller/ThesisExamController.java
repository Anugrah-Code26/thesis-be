package com.thesis.backend.infrastructure.thesis.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thesis.backend.common.responses.ApiResponse;
import com.thesis.backend.entity.assessment.AssessmentScore;
import com.thesis.backend.entity.thesis.ThesisExam;
import com.thesis.backend.entity.thesis.ThesisExamStatus;
import com.thesis.backend.infrastructure.assessment.dto.AssessmentScoreDTO;
import com.thesis.backend.infrastructure.thesis.dto.FileDownloadDTO;
import com.thesis.backend.infrastructure.thesis.dto.ThesisExamDTO;
import com.thesis.backend.infrastructure.thesis.mapper.ThesisExamMapper;
import com.thesis.backend.service.thesis.ThesisExamService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/thesis/exams")
public class ThesisExamController {

    private final ThesisExamService examService;
    private final ObjectMapper objectMapper; // for parsing JSON part

    @PostMapping(value = "/request", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ThesisExamDTO>> requestExam(
//            @RequestParam Long examId,
            @RequestPart("data") String thesisExamJson,
            @RequestPart("file") MultipartFile file) {
        try {
            ThesisExamDTO dto = objectMapper.readValue(thesisExamJson, ThesisExamDTO.class);
            ThesisExam exam = examService.requestExam(dto, file);
            ThesisExamDTO responseDto = ThesisExamMapper.toDTO(exam);
            return ApiResponse.success(HttpStatus.CREATED.value(), "Exam requested!", responseDto);
        } catch (Exception e) {
            return ApiResponse.failed(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @PutMapping("/schedule")
    public ResponseEntity<?> scheduleExam(
            @RequestParam Long examId,
            @RequestBody ThesisExamDTO scheduleDTO) {

        ThesisExam scheduledExam = examService.scheduleExam(examId, scheduleDTO);

        return ApiResponse.success(
                HttpStatus.OK.value(),
                "Thesis updated successfully",
                ThesisExamMapper.toDTO(scheduledExam)
        );
    }

    @GetMapping("/{id}/file")
    public ResponseEntity<Resource> getExamFile(@PathVariable Long id) {
        try {
            // service loads file metadata and returns Resource
            FileDownloadDTO download = examService.getExamFile(id); // custom DTO below
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(download.getContentType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + download.getFilename() + "\"")
                    .body(download.getResource());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/{examId}/status")
    public ResponseEntity<ApiResponse<ThesisExam>> updateExamStatus(
            @PathVariable Long examId,
            @RequestParam ThesisExamStatus status) {
        try {
            ThesisExam exam = examService.updateExamStatus(examId, status);
            return ApiResponse.success("Exam status updated successfully", exam);
        } catch (Exception e) {
            return ApiResponse.failed(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @PostMapping("/evaluate")
    public ResponseEntity<ApiResponse<AssessmentScore>> submitEvaluation(@RequestBody AssessmentScoreDTO evaluationDTO) {
        try {
            AssessmentScore evaluation = examService.submitEvaluation(evaluationDTO);
            return ApiResponse.success("Evaluation submitted successfully", evaluation);
        } catch (Exception e) {
            return ApiResponse.failed(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getExams(
            @RequestParam(required = false) String date,
            @RequestParam(required = false) Long examId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long supervisorId,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ApiResponse.success(
            HttpStatus.OK.value(),
            "All Exams fetched successfully!",
            examService.searchExams(date, examId, status, supervisorId, sortBy, sortDir, page, size)
        );
    }

    @GetMapping("/student")
    public ResponseEntity<?> getExamsByStudent(
            @RequestParam(required = false) String date,
            @RequestParam(required = false) Long examId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long supervisorId,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ApiResponse.success(
                HttpStatus.OK.value(),
                "All Exams fetched successfully!",
                examService.searchExamsByThesis(date, examId, status, supervisorId, sortBy, sortDir, page, size)
        );
    }

    @GetMapping("/request")
    public ResponseEntity<?> getAllRequestedExams() {

        return ApiResponse.success(
            HttpStatus.OK.value(),
            "All Requested Exams fetched successfully!",
            examService.getAllRequestedExams()
        );
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<ThesisExam>>> getExamsByStatus(
            @PathVariable ThesisExamStatus status) {
        try {
            List<ThesisExam> exams = examService.getExamsByStatus(status);
            return ApiResponse.success("Exams retrieved successfully", exams);
        } catch (Exception e) {
            return ApiResponse.failed(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }
}
