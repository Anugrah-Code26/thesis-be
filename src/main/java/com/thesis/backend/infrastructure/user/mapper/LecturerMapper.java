package com.thesis.backend.infrastructure.user.mapper;

import com.thesis.backend.entity.user.Lecturer;
import com.thesis.backend.infrastructure.user.dto.LecturerDTO;

public class LecturerMapper {

    public static LecturerDTO toDTO(Lecturer lecturer) {
        LecturerDTO dto = new LecturerDTO();
        dto.setId(lecturer.getId());
        dto.setName(lecturer.getName());
        dto.setEmail(lecturer.getEmail());
        dto.setUniqueId(lecturer.getUniqueId());
        dto.setUniversity(lecturer.getUniversity());
        dto.setFaculty(lecturer.getFaculty());
        dto.setDepartment(lecturer.getDepartment());
        return dto;
    }
}
