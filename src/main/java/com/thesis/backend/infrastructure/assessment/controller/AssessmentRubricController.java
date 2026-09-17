package com.thesis.backend.infrastructure.assessment.controller;

import com.thesis.backend.common.responses.ApiResponse;
import com.thesis.backend.entity.assessment.AssessmentRubric;
import com.thesis.backend.infrastructure.assessment.dto.AssessmentRubricDTO;
import com.thesis.backend.infrastructure.assessment.mapper.AssessmentRubricMapper;
import com.thesis.backend.service.assessment.AssessmentRubricService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/assessment/rubrics")
public class AssessmentRubricController {

    private final AssessmentRubricService rubricService;

    @PostMapping
    public ResponseEntity<ApiResponse<AssessmentRubricDTO>> createRubric(
            @RequestParam Long examId,
            @RequestBody AssessmentRubricDTO dto
    ) {
        try {
            AssessmentRubric saved = rubricService.createAssessmentRubricByExam(examId, dto);
            return ApiResponse.success(HttpStatus.CREATED.value(), "Rubric created successfully", AssessmentRubricMapper.toDTO(saved));
        } catch (Exception e) {
            return ApiResponse.failed(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @PutMapping
    public ResponseEntity<ApiResponse<AssessmentRubricDTO>> updateRubric(
            @RequestParam Long rubricId,
            @RequestBody AssessmentRubricDTO dto) {
        try {
            AssessmentRubric updated = rubricService.editAssessmentRubric(rubricId, dto);
            return ApiResponse.success("Rubric updated successfully", AssessmentRubricMapper.toDTO(updated));
        } catch (Exception e) {
            return ApiResponse.failed(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AssessmentRubricDTO>>> getRubricsByExam(
            @RequestParam Long examId
    ) {
        try {
            List<AssessmentRubricDTO> rubrics = rubricService.getAssessmentRubricByExam(examId);
            return ApiResponse.success("Rubrics fetched successfully", rubrics);
        } catch (Exception e) {
            return ApiResponse.failed(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }
}

