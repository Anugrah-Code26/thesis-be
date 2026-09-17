package com.thesis.backend.infrastructure.user.dto;

import lombok.Data;

@Data
public class LecturerDTO {
    private Long id;
    private String email;
    private String name;
    private String uniqueId;
    private String university;
    private String faculty;
    private String department;
}
