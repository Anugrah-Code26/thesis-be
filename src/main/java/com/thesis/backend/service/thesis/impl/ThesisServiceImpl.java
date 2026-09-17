package com.thesis.backend.service.thesis.impl;

import com.thesis.backend.common.exceptions.UnauthorizedException;
import com.thesis.backend.entity.thesis.Thesis;
import com.thesis.backend.entity.thesis.ThesisStatus;
import com.thesis.backend.entity.user.Lecturer;
import com.thesis.backend.entity.user.RoleType;
import com.thesis.backend.entity.user.Student;
import com.thesis.backend.common.exceptions.DataNotFoundException;
import com.thesis.backend.infrastructure.auth.Claims;
import com.thesis.backend.infrastructure.thesis.dto.ThesisDTO;
import com.thesis.backend.infrastructure.thesis.dto.ThesisSubmissionDTO;
import com.thesis.backend.infrastructure.thesis.mapper.ThesisMapper;
import com.thesis.backend.infrastructure.thesis.repository.ThesisRepository;
import com.thesis.backend.infrastructure.user.repository.LecturerRepository;
import com.thesis.backend.infrastructure.user.repository.StudentRepository;
import com.thesis.backend.service.thesis.ThesisService;
import com.thesis.backend.service.thesis.validator.ThesisStatusValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ThesisServiceImpl implements ThesisService {

    private final ThesisRepository thesisRepository;
    private final StudentRepository studentRepository;
    private final LecturerRepository lecturerRepository;

    @Value("${thesis.upload.dir}")
    private String uploadDir;

    @Override
    @Transactional
    public Thesis registerThesis(ThesisDTO thesisDTO) {
        Long userId = Claims.getUserIdFromJwt();

        Student student = studentRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("Student not found"));

        if (!RoleType.STUDENT.equals(Claims.getRoleFromJwt())) {
            throw new UnauthorizedException("Only Student can register thesis!");
        }

        Thesis thesis = ThesisMapper.toEntity(thesisDTO);
        thesis.setStudent(student);

        thesisRepository.saveAndFlush(thesis);

        List<Lecturer> lecturers = lecturerRepository.findAllById(thesisDTO.getLecturerIds());
        if (lecturers.isEmpty()) {
            throw new DataNotFoundException("No valid lecturers found");
        }

        lecturers.forEach(lecturer -> lecturer.addThesis(thesis));
        return thesis;
    }

    @Override
    @Transactional
    public Thesis editThesis(Long thesisId, ThesisDTO thesisDTO) {
        Long userId = Claims.getUserIdFromJwt();

        Thesis thesis = thesisRepository.findById(thesisId)
                .orElseThrow(() -> new DataNotFoundException("Thesis not found"));

        // Ensure only the owner student can edit
        if (!thesis.getStudent().getId().equals(userId)) {
            throw new UnauthorizedException("You are not allowed to edit this thesis");
        }

        // Ensure thesis is editable
        if (thesis.getStatus() != ThesisStatus.DRAFT &&
                thesis.getStatus() != ThesisStatus.REVISION_REQUIRED) {
            throw new IllegalStateException("Only theses in DRAFT or REVISION_REQUIRED can be edited");
        }

        // Update fields
        thesis.setTitle(thesisDTO.getTitle());
        thesis.setAbstractText(thesisDTO.getAbstractText());
        thesis.setKeywords(thesisDTO.getKeywords());
        thesis.setUpdatedAt(OffsetDateTime.now());

        // Update lecturers (if provided)
        if (thesisDTO.getLecturerIds() != null && !thesisDTO.getLecturerIds().isEmpty()) {
            List<Lecturer> lecturers = lecturerRepository.findAllById(thesisDTO.getLecturerIds());
            if (lecturers.isEmpty()) {
                throw new DataNotFoundException("No valid lecturers found");
            }
            lecturers.forEach(lecturer -> lecturer.addThesis(thesis));
        }

        return thesisRepository.save(thesis);
    }

    @Override
    public Thesis submitThesis(ThesisSubmissionDTO submissionDTO) {
        Thesis thesis = thesisRepository.findById(submissionDTO.getThesisId())
                .orElseThrow(() -> new DataNotFoundException("Thesis not found"));

        if (thesis.getStatus() != ThesisStatus.DRAFT &&
                thesis.getStatus() != ThesisStatus.REVISION_REQUIRED) {
            throw new IllegalStateException("Thesis cannot be submitted in its current status");
        }

        try {
            String fileName = "thesis_" + thesis.getId() + "_" + System.currentTimeMillis() + ".pdf";
            Path filePath = Paths.get(uploadDir, fileName);
            Files.copy(submissionDTO.getThesisFile().getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            thesis.setFilePath(filePath.toString());
            thesis.setStatus(ThesisStatus.SUBMITTED);
            thesis.setSubmissionDate(OffsetDateTime.now());

            return thesisRepository.save(thesis);
        } catch (IOException e) {
            throw new RuntimeException("Failed to store thesis file", e);
        }
    }

    @Override
    public Thesis approveThesis(Long thesisId) {
        Thesis thesis = thesisRepository.findById(thesisId)
                .orElseThrow(() -> new DataNotFoundException("Thesis not found"));

        if (thesis.getStatus() != ThesisStatus.SUBMITTED) {
            throw new IllegalStateException("Only submitted theses can be approved");
        }

        thesis.setStatus(ThesisStatus.APPROVED);
        return thesisRepository.save(thesis);
    }

    @Override
    public Thesis requestRevision(Long thesisId, String comments) {
        Thesis thesis = thesisRepository.findById(thesisId)
                .orElseThrow(() -> new DataNotFoundException("Thesis not found"));

        if (thesis.getStatus() != ThesisStatus.SUBMITTED) {
            throw new IllegalStateException("Only submitted theses can be requested for revision");
        }

        thesis.setStatus(ThesisStatus.REVISION_REQUIRED);
        // Store comments in a separate table or field
        return thesisRepository.save(thesis);
    }

    @Override
    public Thesis changeStatus(Long thesisId, ThesisStatus newStatus, String comments) {
        Thesis thesis = thesisRepository.findById(thesisId)
                .orElseThrow(() -> new DataNotFoundException("Thesis not found"));

        if (!ThesisStatusValidator.isValidTransition(thesis.getStatus(), newStatus)) {
            throw new IllegalStateException(
                    String.format("Invalid status transition from %s to %s",
                            thesis.getStatus(), newStatus));
        }

        thesis.setStatus(newStatus);
//        ThesisStatusHistory history = new ThesisStatusHistory(thesis, newStatus, comments);

        return thesisRepository.save(thesis);
    }

    @Override
    @Transactional
    public ThesisDTO getThesisByStudent() {
        Long userId = Claims.getUserIdFromJwt();

        Optional<Thesis> studentThesis = thesisRepository.findByStudentId(userId);

        return studentThesis
                .map(ThesisMapper::toDTO)
                .orElse(null);
    }

    @Override
    public List<Thesis> getThesesByStatus(ThesisStatus status) {
        return thesisRepository.findByStatus(status);
    }

    // Other service methods...
}
