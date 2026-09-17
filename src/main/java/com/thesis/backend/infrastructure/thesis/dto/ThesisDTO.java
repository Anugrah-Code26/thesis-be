package com.thesis.backend.infrastructure.thesis.dto;

import com.thesis.backend.entity.thesis.ThesisStatus;
import com.thesis.backend.infrastructure.user.dto.SupervisorDTO;
import lombok.Data;

import java.util.List;

@Data
public class ThesisDTO {
    private Long id;
    private String title;
    private String abstractText;
    private String keywords;
    private ThesisStatus status;

    private List<Long> lecturerIds;
    private List<SupervisorDTO> supervisors;
}

