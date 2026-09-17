package com.thesis.backend.infrastructure.thesis.mapper;

import com.thesis.backend.entity.assessment.Exam;
import com.thesis.backend.entity.thesis.Thesis;
import com.thesis.backend.entity.thesis.ThesisExam;
import com.thesis.backend.entity.thesis.ThesisExamStatus;
import com.thesis.backend.entity.user.Examiner;
import com.thesis.backend.entity.user.Lecturer;
import com.thesis.backend.entity.user.Student;
import com.thesis.backend.entity.user.Supervisor;
import com.thesis.backend.infrastructure.thesis.dto.ExamDTO;
import com.thesis.backend.infrastructure.thesis.dto.ThesisDTO;
import com.thesis.backend.infrastructure.thesis.dto.ThesisExamDTO;
import com.thesis.backend.infrastructure.user.dto.ExaminerDTO;
import com.thesis.backend.infrastructure.user.dto.LecturerDTO;
import com.thesis.backend.infrastructure.user.dto.SupervisorDTO;
import com.thesis.backend.infrastructure.user.dto.UserDTO;

import java.util.stream.Collectors;

public class ThesisExamMapper {

    public static ThesisExam toEntity(ThesisExamDTO dto) {
        ThesisExam thesisExam = new ThesisExam();
        thesisExam.setScheduledDate(dto.getScheduledDate());
        thesisExam.setLocation(dto.getLocation());

        if (dto.getStatus() != null) {
            thesisExam.setStatus(dto.getStatus());
        }
        return thesisExam;
    }

    public static ThesisExamDTO toDTO(ThesisExam thesisExam) {
        ThesisExamDTO dto = new ThesisExamDTO();
        dto.setId(thesisExam.getId());
        dto.setThesisId(thesisExam.getThesis().getId());
        dto.setExamId(thesisExam.getExam().getId());
        dto.setScheduledDate(thesisExam.getScheduledDate());
        dto.setLocation(thesisExam.getLocation());
        dto.setStatus(ThesisExamStatus.valueOf(thesisExam.getStatus().name()));

        dto.setExaminers(thesisExam.getExaminers().stream()
                .map(ThesisExamMapper::toExaminerDTO)
                .collect(Collectors.toList()));

        // file metadata
        dto.setFileName(thesisExam.getFileName());
        dto.setFileContentType(thesisExam.getFileContentType());
        dto.setFileSize(thesisExam.getFileSize());

        if (thesisExam.getId() != null && thesisExam.getFileName() != null) {
            dto.setFileUrl("/api/v1/thesis/exams/" + thesisExam.getId() + "/file");
        }

        dto.setExam(toExamDTO(thesisExam.getExam()));

        Thesis thesis = thesisExam.getThesis();
        dto.setThesis(toThesisDTO(thesis));
        dto.setStudent(toUserDTO(thesis.getStudent()));

        dto.setSupervisors(thesis.getSupervisors().stream()
                .map(ThesisExamMapper::toSupervisorDTO)
                .collect(Collectors.toList()));

        return dto;
    }

    private static ThesisDTO toThesisDTO(Thesis thesis) {
        ThesisDTO dto = new ThesisDTO();
        dto.setId(thesis.getId());
        dto.setTitle(thesis.getTitle());
        dto.setStatus(thesis.getStatus());
        dto.setKeywords(thesis.getKeywords());
        return dto;
    }

    private static ExamDTO toExamDTO(Exam exam) {
        ExamDTO dto = new ExamDTO();
        dto.setId(exam.getId());
        dto.setName(exam.getName());
        dto.setDescription(exam.getDescription());
        return dto;
    }

    private static UserDTO toUserDTO(Student student) {
        UserDTO dto = new UserDTO();
        dto.setId(student.getId());
        dto.setName(student.getName());
        dto.setEmail(student.getEmail());
        dto.setUniqueId(student.getUniqueId());
        dto.setUniversity(student.getUniversity());
        dto.setFaculty(student.getFaculty());
        dto.setDepartment(student.getDepartment());
        return dto;
    }

    private static LecturerDTO toLecturerDTO(Lecturer lecturer) {
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

    private static ExaminerDTO toExaminerDTO(Examiner examiner) {
        ExaminerDTO dto = new ExaminerDTO();
        dto.setId(examiner.getId());
        dto.setLecturer(toLecturerDTO(examiner.getLecturer()));
        return dto;
    }

    private static SupervisorDTO toSupervisorDTO(Supervisor supervisor) {
        SupervisorDTO dto = new SupervisorDTO();
        dto.setId(supervisor.getId());
        dto.setLecturer(toLecturerDTO(supervisor.getLecturer()));
        return dto;
    }
}
