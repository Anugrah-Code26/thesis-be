package com.thesis.backend.infrastructure.user.dto;

import com.thesis.backend.entity.user.*;
import lombok.Data;

import java.util.Set;
import java.util.stream.Collectors;

@Data
public class UserProfileDTO {
    private Long id;
    private String email;
    private String name;
    private Set<String> roles;
    private String uniqueId;
    private String university;
    private String faculty;
    private String department;

    public static UserProfileDTO fromEntity(User user) {
        UserProfileDTO dto = new UserProfileDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());

        dto.setRoles(
                user.getRoles().stream()
                        .map(Role::getName)
                        .collect(Collectors.toSet())
        );

        if (user instanceof Student student) {
            dto.setUniqueId(student.getUniqueId());
        }

        if (user instanceof Lecturer lecturer) {
            dto.setUniqueId(lecturer.getUniqueId());
        }

        return dto;
    }
}
