package com.thesis.backend.infrastructure.thesis.dto;

import com.thesis.backend.entity.thesis.ExamType;
import com.thesis.backend.entity.thesis.ThesisExamStatus;
import com.thesis.backend.infrastructure.user.dto.ExaminerDTO;
import com.thesis.backend.infrastructure.user.dto.SupervisorDTO;
import com.thesis.backend.infrastructure.user.dto.UserDTO;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Data
public class ThesisExamDTO {
    private Long id;
    private Long thesisId;
    private Long examId;
    private OffsetDateTime scheduledDate;
    private String location;
    private ThesisExamStatus status;

    private List<Long> lecturerIds;
    private List<ExaminerDTO> examiners;

    // file info for responses
    private String fileName;
    private String fileUrl; // optional - a URL that your front-end can call
    private String fileContentType;
    private Long fileSize;

    private ExamDTO exam;
    private ThesisDTO thesis;
    private UserDTO student;

    private List<SupervisorDTO> supervisors;
}
