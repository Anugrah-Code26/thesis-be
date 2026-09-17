package com.thesis.backend.infrastructure.user.dto;

import lombok.Data;

import java.util.Set;

@Data
public class UserDTO {
    private Long id;
    private String email;
    private String name;
    private Set<String> roles;
    private String uniqueId;
    private String university;
    private String faculty;
    private String department;
}
