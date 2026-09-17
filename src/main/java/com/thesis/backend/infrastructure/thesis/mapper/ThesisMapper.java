package com.thesis.backend.infrastructure.thesis.mapper;

import com.thesis.backend.entity.thesis.Thesis;
import com.thesis.backend.entity.thesis.ThesisStatus;
import com.thesis.backend.entity.user.Lecturer;
import com.thesis.backend.entity.user.Supervisor;
import com.thesis.backend.infrastructure.thesis.dto.ThesisDTO;
import com.thesis.backend.infrastructure.user.dto.LecturerDTO;
import com.thesis.backend.infrastructure.user.dto.SupervisorDTO;

import java.util.List;
import java.util.stream.Collectors;

public class ThesisMapper {

    public static Thesis toEntity(ThesisDTO dto) {
        Thesis thesis = new Thesis();
        thesis.setTitle(dto.getTitle());
        thesis.setAbstractText(dto.getAbstractText());
        thesis.setKeywords(dto.getKeywords());
        thesis.setStatus(ThesisStatus.DRAFT);
        return thesis;
    }

    public static ThesisDTO toDTO(Thesis thesis) {
        ThesisDTO dto = new ThesisDTO();
        dto.setId(thesis.getId());
        dto.setTitle(thesis.getTitle());
        dto.setAbstractText(thesis.getAbstractText());
        dto.setKeywords(thesis.getKeywords());
        dto.setStatus(ThesisStatus.valueOf(thesis.getStatus().name()));

        List<SupervisorDTO> supervisors = thesis.getSupervisors().stream().map(ThesisMapper::toSupervisorDTO).collect(Collectors.toList());
        dto.setSupervisors(supervisors);

        return dto;
    }

    private static SupervisorDTO toSupervisorDTO(Supervisor supervisor) {
        SupervisorDTO sDto = new SupervisorDTO();
        sDto.setId(supervisor.getId());

        Lecturer lecturer = supervisor.getLecturer();
        LecturerDTO lDto = new LecturerDTO();
        lDto.setId(lecturer.getId());
        lDto.setEmail(lecturer.getEmail());
        lDto.setName(lecturer.getName());
        lDto.setUniversity(lecturer.getUniversity());
        lDto.setFaculty(lecturer.getFaculty());
        lDto.setDepartment(lecturer.getDepartment());
        lDto.setUniqueId(lecturer.getUniqueId());

        sDto.setLecturer(lDto);
        return sDto;
    }
}
