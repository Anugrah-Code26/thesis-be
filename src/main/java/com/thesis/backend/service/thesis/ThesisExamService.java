package com.thesis.backend.service.thesis;

import com.thesis.backend.entity.assessment.AssessmentScore;
import com.thesis.backend.entity.thesis.ThesisExam;
import com.thesis.backend.entity.thesis.ThesisExamStatus;
import com.thesis.backend.infrastructure.assessment.dto.AssessmentScoreDTO;
import com.thesis.backend.infrastructure.thesis.dto.FileDownloadDTO;
import com.thesis.backend.infrastructure.thesis.dto.ThesisExamDTO;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ThesisExamService {
    ThesisExam requestExam(ThesisExamDTO thesisExamDTO, MultipartFile file);
    FileDownloadDTO getExamFile(Long examId);
    ThesisExam scheduleExam(Long examId, ThesisExamDTO scheduleDTO);
    ThesisExam updateExamStatus(Long examId, ThesisExamStatus newStatus);
    AssessmentScore submitEvaluation(AssessmentScoreDTO evaluationDTO);
    Page<ThesisExamDTO> searchExams(String date, Long examId, String status, Long supervisorId, String sortBy, String sortDir, int page, int size);
    Page<ThesisExamDTO> searchExamsByThesis(String date, Long examId, String status, Long supervisorId, String sortBy, String sortDir, int page, int size);
    List<ThesisExamDTO> getAllRequestedExams();
    List<ThesisExam> getExamsByStatus(ThesisExamStatus status);
}
