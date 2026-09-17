package com.thesis.backend.infrastructure.user.dto;

import lombok.Data;

@Data
public class ExaminerDTO {
    private Long id;
    private LecturerDTO lecturer;
}
