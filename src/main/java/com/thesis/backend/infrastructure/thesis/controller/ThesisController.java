package com.thesis.backend.infrastructure.thesis.controller;

import com.thesis.backend.common.responses.ApiResponse;
import com.thesis.backend.entity.thesis.Thesis;
import com.thesis.backend.entity.thesis.ThesisStatus;
import com.thesis.backend.infrastructure.thesis.dto.ThesisDTO;
import com.thesis.backend.infrastructure.thesis.dto.ThesisSubmissionDTO;
import com.thesis.backend.infrastructure.thesis.mapper.ThesisMapper;
import com.thesis.backend.service.thesis.ThesisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/theses")
public class ThesisController {

    private final ThesisService thesisService;

    @PostMapping
    public ResponseEntity<ApiResponse<ThesisDTO>> registerThesis(@RequestBody ThesisDTO thesisDTO) {
        try {
            Thesis thesis = thesisService.registerThesis(thesisDTO);
            ThesisDTO responseDto = ThesisMapper.toDTO(thesis);
            return ApiResponse.success(HttpStatus.CREATED.value(), "Thesis registered successfully", responseDto);
        } catch (Exception e) {
            return ApiResponse.failed(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @PostMapping("/{thesisId}/submit")
    public ResponseEntity<ApiResponse<Thesis>> submitThesis(
            @PathVariable Long thesisId,
            @RequestParam("file") MultipartFile file) {
        try {
            ThesisSubmissionDTO submissionDTO = new ThesisSubmissionDTO();
            submissionDTO.setThesisId(thesisId);
            submissionDTO.setThesisFile(file);

            Thesis thesis = thesisService.submitThesis(submissionDTO);
            return ApiResponse.success("Thesis submitted successfully", thesis);
        } catch (Exception e) {
            return ApiResponse.failed(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @PutMapping
    public ResponseEntity<?> editThesis(
            @RequestParam Long thesisId,
            @RequestBody ThesisDTO thesisDTO) {

        Thesis updatedThesis = thesisService.editThesis(thesisId, thesisDTO);

        return ApiResponse.success(
                HttpStatus.OK.value(),
                "Thesis updated successfully",
                ThesisMapper.toDTO(updatedThesis)
        );
    }

    @PutMapping("/{thesisId}/approve")
    public ResponseEntity<ApiResponse<Thesis>> approveThesis(@PathVariable Long thesisId) {
        try {
            Thesis thesis = thesisService.approveThesis(thesisId);
            return ApiResponse.success("Thesis approved successfully", thesis);
        } catch (Exception e) {
            return ApiResponse.failed(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @PutMapping("/{thesisId}/request-revision")
    public ResponseEntity<ApiResponse<Thesis>> requestRevision(
            @PathVariable Long thesisId,
            @RequestParam String comments) {
        try {
            Thesis thesis = thesisService.requestRevision(thesisId, comments);
            return ApiResponse.success("Revision requested successfully", thesis);
        } catch (Exception e) {
            return ApiResponse.failed(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @GetMapping("/student")
    public ResponseEntity<?> getThesisByStudent() {
        ThesisDTO thesis = thesisService.getThesisByStudent();

        if (thesis == null) {
            return ApiResponse.success(
                    HttpStatus.OK.value(),
                    "No thesis found for this student",
                    null
            );
        }

        return ApiResponse.success(
                HttpStatus.OK.value(),
                "Thesis fetched successfully",
                thesis
        );
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<Thesis>>> getThesesByStatus(
            @PathVariable ThesisStatus status) {
        try {
            List<Thesis> theses = thesisService.getThesesByStatus(status);
            return ApiResponse.success("Theses retrieved successfully", theses);
        } catch (Exception e) {
            return ApiResponse.failed(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    // Other endpoints...
}
