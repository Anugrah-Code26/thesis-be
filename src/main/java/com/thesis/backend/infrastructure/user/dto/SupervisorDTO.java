package com.thesis.backend.infrastructure.user.dto;

import lombok.Data;

@Data
public class SupervisorDTO {
    private Long id;
    private LecturerDTO lecturer;
}
