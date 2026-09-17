package com.thesis.backend.infrastructure.auth.dto;

import lombok.Data;

@Data
public class AuthDTO {
    private String email;
    private String password;
}
