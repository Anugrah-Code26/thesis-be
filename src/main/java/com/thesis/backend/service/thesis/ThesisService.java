package com.thesis.backend.service.thesis;

import com.thesis.backend.entity.thesis.Thesis;
import com.thesis.backend.entity.thesis.ThesisStatus;
import com.thesis.backend.infrastructure.thesis.dto.ThesisDTO;
import com.thesis.backend.infrastructure.thesis.dto.ThesisSubmissionDTO;

import java.util.List;

public interface ThesisService {
    Thesis registerThesis(ThesisDTO thesisDTO);
    Thesis editThesis(Long thesisId, ThesisDTO thesisDTO);
    Thesis submitThesis(ThesisSubmissionDTO submissionDTO);
    Thesis approveThesis(Long thesisId);
    Thesis requestRevision(Long thesisId, String comments);
    Thesis changeStatus(Long thesisId, ThesisStatus newStatus, String comments);
    ThesisDTO getThesisByStudent();
    List<Thesis> getThesesByStatus(ThesisStatus status);
}
