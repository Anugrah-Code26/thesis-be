package com.thesis.backend.infrastructure.user.controller;

import com.thesis.backend.common.responses.ApiResponse;
import com.thesis.backend.service.user.LecturerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/lecturers")
public class LecturerController {

    private final LecturerService lecturerService;

    @GetMapping
    public ResponseEntity<?> getAllLecturers() {

        return ApiResponse.success(
                HttpStatus.OK.value(),
                "All Lecturers fetched successfully",
                lecturerService.getAllLecturer()
        );
    }
}
