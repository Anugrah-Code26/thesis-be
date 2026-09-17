package com.thesis.backend.infrastructure.thesis.controller;

import com.thesis.backend.common.responses.ApiResponse;
import com.thesis.backend.entity.assessment.Exam;
import com.thesis.backend.infrastructure.thesis.dto.ExamDTO;
import com.thesis.backend.infrastructure.thesis.mapper.ExamMapper;
import com.thesis.backend.service.thesis.ExamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/exams")
public class ExamController {

    private final ExamService examService;

    @PostMapping
    public ResponseEntity<ApiResponse<ExamDTO>> createExam(@RequestBody ExamDTO examDTO) {
        try {
            Exam exam = examService.createExam(examDTO);
            ExamDTO responseDto = ExamMapper.toDTO(exam);
            return ApiResponse.success(HttpStatus.CREATED.value(), "Exam created successfully", responseDto);
        } catch (Exception e) {
            return ApiResponse.failed(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @PutMapping
    public ResponseEntity<?> editExam(
            @RequestParam Long examId,
            @RequestBody ExamDTO examDTO) {

        Exam updatedExam = examService.editExam(examId, examDTO);

        return ApiResponse.success(
                HttpStatus.OK.value(),
                "Exam updated successfully",
                ExamMapper.toDTO(updatedExam)
        );
    }

    @GetMapping
    public ResponseEntity<?> getExamByStudent() {
        List<ExamDTO> exam = examService.getAllExams();

        if (exam == null) {
            return ApiResponse.success(
                    HttpStatus.OK.value(),
                    "No exam found for this student",
                    null
            );
        }

        return ApiResponse.success(
                HttpStatus.OK.value(),
                "Exam fetched successfully",
                exam
        );
    }

    // Other endpoints...
}
