package com.thesis.backend.infrastructure.user.mapper;

import com.thesis.backend.entity.user.*;
import com.thesis.backend.infrastructure.user.dto.UserDTO;

import java.util.Set;
import java.util.stream.Collectors;

public class UserMapper {

    public static UserDTO toUserDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setName(user.getName());
        dto.setUniversity(user.getUniversity());
        dto.setFaculty(user.getFaculty());
        dto.setDepartment(user.getDepartment());

        if (user instanceof Student) {
            dto.setUniqueId(((Student) user).getUniqueId());
        } else if (user instanceof Lecturer) {
            dto.setUniqueId(((Lecturer) user).getUniqueId());
        }

        Set<String> roles = user.getRoles()
                .stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
        dto.setRoles(roles);

        return dto;
    }
}
