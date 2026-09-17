package com.thesis.backend.service.thesis.impl;

import com.thesis.backend.common.exceptions.BadRequestException;
import com.thesis.backend.common.exceptions.DataNotFoundException;
import com.thesis.backend.common.exceptions.UnauthorizedException;
import com.thesis.backend.entity.assessment.AssessmentScore;
import com.thesis.backend.entity.assessment.Exam;
import com.thesis.backend.entity.thesis.*;
import com.thesis.backend.entity.user.Lecturer;
import com.thesis.backend.entity.user.RoleType;
import com.thesis.backend.infrastructure.auth.Claims;
import com.thesis.backend.infrastructure.assessment.dto.AssessmentScoreDTO;
import com.thesis.backend.infrastructure.thesis.dto.FileDownloadDTO;
import com.thesis.backend.infrastructure.thesis.dto.ThesisExamDTO;
import com.thesis.backend.infrastructure.thesis.mapper.ThesisExamMapper;
import com.thesis.backend.infrastructure.assessment.repository.AssessmentScoreRepository;
import com.thesis.backend.infrastructure.thesis.repository.ExamRepository;
import com.thesis.backend.infrastructure.thesis.repository.ThesisExamRepository;
import com.thesis.backend.infrastructure.thesis.repository.ThesisRepository;
import com.thesis.backend.infrastructure.user.repository.LecturerRepository;
import com.thesis.backend.infrastructure.user.repository.StudentRepository;
import com.thesis.backend.service.thesis.ThesisExamService;
import com.thesis.backend.service.thesis.FileStorageService;
import com.thesis.backend.service.thesis.specification.ThesisExamSpecification;
import com.thesis.backend.service.thesis.validator.ThesisExamStatusValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ThesisExamServiceImpl implements ThesisExamService {

    private final ThesisExamRepository thesisExamRepository;
    private final ThesisRepository thesisRepository;
    private final ExamRepository examRepository;
    private final LecturerRepository lecturerRepository;
    private final StudentRepository studentRepository;
    private final AssessmentScoreRepository evaluationRepository;
    private final FileStorageService fileStorageService;

    @Override
    @Transactional
    public ThesisExam requestExam(ThesisExamDTO thesisExamDTO, MultipartFile file) {
        if (!RoleType.STUDENT.equals(Claims.getRoleFromJwt())) {
            throw new UnauthorizedException("Only Student can request exam!");
        }

        Thesis thesis = thesisRepository.findByStudentId(Claims.getUserIdFromJwt())
                .orElseThrow(() -> new DataNotFoundException("You still have not register the thesis!"));

        ThesisExam thesisExam = ThesisExamMapper.toEntity(thesisExamDTO);
        thesisExam.setThesis(thesis);

        Exam exam = examRepository.findById(thesisExamDTO.getExamId())
                .orElseThrow(() -> new DataNotFoundException("Exam not found!"));

        thesisExam.setExam(exam);

        if (thesisExam.getStatus() == null) {
            thesisExam.setStatus(ThesisExamStatus.REQUESTED);
        }

        // Validate file present
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("You must upload a file when requesting an exam.");
        }

        // Save file to storage
        String savedPath = fileStorageService.store(file, "thesis-exams/" + thesis.getId());
        thesisExam.setFilePath(savedPath);
        thesisExam.setFileName(file.getOriginalFilename());
        thesisExam.setFileContentType(file.getContentType());
        thesisExam.setFileSize(file.getSize());

        thesisExamRepository.saveAndFlush(thesisExam);

        // Handle optional examiners
        List<Long> lecturerIds = thesisExamDTO.getLecturerIds();
        if (lecturerIds != null && !lecturerIds.isEmpty()) {
            // Find lecturers for the provided ids. This returns only found ones.
            List<Lecturer> lecturers = lecturerRepository.findAllById(lecturerIds);

            // If none found, just skip adding examiners (optional)
            if (!lecturers.isEmpty()) {
                for (Lecturer lecturer : lecturers) {
                    lecturer.addThesisExam(thesisExam);
                }
                thesisExam = thesisExamRepository.saveAndFlush(thesisExam);
            }
        }

//        List<Lecturer> lecturers = lecturerRepository.findAllById(thesisExamDTO.getLecturerIds());
//        if (lecturers.isEmpty()) {
//            throw new DataNotFoundException("No valid lecturers found");
//        }
//
//        lecturers.forEach(lecturer -> lecturer.addThesisExam(thesisExam));
        return thesisExam;
    }

    @Override
    @Transactional
    public FileDownloadDTO getExamFile(Long examId) {
        ThesisExam exam = thesisExamRepository.findById(examId)
                .orElseThrow(() -> new DataNotFoundException("Exam not found"));

        // Authorization: check if current user can access (lecturer assigned, examiner, admin, or same student)
        Long currentUserId = Claims.getUserIdFromJwt();
        String role = Claims.getRoleFromJwt();
        boolean allowed = RoleType.ADMIN.equals(role);
        if (RoleType.STUDENT.equals(role) && exam.getThesis().getStudent().getId().equals(currentUserId)) allowed = true;
        // check assigned examiners / lecturers:
        allowed = allowed || exam.getExaminers().stream()
                .anyMatch(e -> e.getLecturer().getId().equals(currentUserId));
        if (!allowed) throw new UnauthorizedException("You are not allowed to access this file.");

        Resource resource = fileStorageService.loadAsResource(exam.getFilePath());
        FileDownloadDTO dto = new FileDownloadDTO();
        dto.setFilename(exam.getFileName());
        dto.setContentType(exam.getFileContentType() == null ? "application/octet-stream" : exam.getFileContentType());
        dto.setResource(resource);
        return dto;
    }

    @Override
    @Transactional
    public ThesisExam scheduleExam(Long examId, ThesisExamDTO scheduleDTO) {
        if (!RoleType.ADMIN.equals(Claims.getRoleFromJwt())) {
            throw new UnauthorizedException("Only Admin can set schedule exam!");
        }

        ThesisExam exam = thesisExamRepository.findById(examId)
                .orElseThrow(() -> new DataNotFoundException("Exam not found"));

        if (exam.getStatus() != ThesisExamStatus.REQUESTED) {
            throw new IllegalStateException("Only exam in REQUESTED can be scheduled!");
        }

        exam.setScheduledDate(scheduleDTO.getScheduledDate());
        exam.setLocation(scheduleDTO.getLocation());
        exam.setStatus(ThesisExamStatus.SCHEDULED);

        if (scheduleDTO.getLecturerIds() != null && !scheduleDTO.getLecturerIds().isEmpty()) {
            List<Lecturer> lecturers = lecturerRepository.findAllById(scheduleDTO.getLecturerIds());
            if (lecturers.isEmpty()) {
                throw new DataNotFoundException("No valid lecturers found");
            }
            lecturers.forEach(lecturer -> lecturer.addThesisExam(exam));
        }
        return thesisExamRepository.save(exam);
    }

    public ThesisExam updateExamStatus(Long examId, ThesisExamStatus newStatus) {
        ThesisExam exam = thesisExamRepository.findById(examId)
                .orElseThrow(() -> new DataNotFoundException("Exam not found"));

        if (!ThesisExamStatusValidator.isValidTransition(exam.getStatus(), newStatus)) {
            throw new IllegalStateException(
                    String.format("Invalid status transition from %s to %s",
                            exam.getStatus(), newStatus));
        }

        exam.setStatus(newStatus);
        return thesisExamRepository.save(exam);
    }

    public AssessmentScore submitEvaluation(AssessmentScoreDTO evaluationDTO) {
//        ThesisExam exam = thesisExamRepository.findById(evaluationDTO.getExamId())
//                .orElseThrow(() -> new DataNotFoundException("Exam not found"));
//
//        if (exam.getStatus() != ThesisExamStatus.COMPLETED) {
//            throw new IllegalStateException("Evaluations can only be submitted for completed exams");
//        }
//
//        AssessmentScore evaluation = new AssessmentScore();
//        evaluation.setThesisExam(exam);
//        evaluation.setEvaluator(lecturerRepository.findById(evaluationDTO.getEvaluatorId())
//                .orElseThrow(() -> new DataNotFoundException("Evaluator not found")));
//        evaluation.setScore(evaluationDTO.getScore());
//        evaluation.setComments(evaluationDTO.getComments());
////        evaluation.setEvaluationDate(OffsetDateTime.now());
//
//        return evaluationRepository.save(evaluation);
        return  null;
    }

    @Override
    @Transactional
    public Page<ThesisExamDTO> searchExams(
            String date,
            Long examId,
            String status,
            Long supervisorId,
            String sortBy,
            String sortDir,
            int page,
            int size
    ) {
        if (!RoleType.ADMIN.equals(Claims.getRoleFromJwt())) {
            throw new UnauthorizedException("Only Admin can see all exams!");
        }

        Specification<ThesisExam> spec = Specification.where(ThesisExamSpecification.hasScheduledDate(date))
                .and(ThesisExamSpecification.hasExam(examId))
                .and(ThesisExamSpecification.hasStatus(status))
                .and(ThesisExamSpecification.hasSupervisor(supervisorId));


        Sort.Direction direction = sortDir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<ThesisExam> exams = thesisExamRepository.findAll(spec, pageable);
        return exams.map(ThesisExamMapper::toDTO);
    }

    @Override
    @Transactional
    public Page<ThesisExamDTO> searchExamsByThesis(
            String date,
            Long examId,
            String status,
            Long supervisorId,
            String sortBy,
            String sortDir,
            int page,
            int size
    ) {
        if (!RoleType.STUDENT.equals(Claims.getRoleFromJwt())) {
            throw new UnauthorizedException("Only Student can see student's exams!");
        }

        Thesis thesis = thesisRepository.findByStudentId(Claims.getUserIdFromJwt())
                .orElseThrow(() -> new DataNotFoundException("You still have not register the thesis!"));

        Specification<ThesisExam> spec = Specification.where(ThesisExamSpecification.hasThesisId(thesis.getId()))
                .and(ThesisExamSpecification.hasScheduledDate(date))
                .and(ThesisExamSpecification.hasExam(examId))
                .and(ThesisExamSpecification.hasStatus(status))
                .and(ThesisExamSpecification.hasSupervisor(supervisorId));

        Sort.Direction direction = sortDir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<ThesisExam> exams = thesisExamRepository.findAll(spec, pageable);
        return exams.map(ThesisExamMapper::toDTO);
    }

    public List<ThesisExamDTO> getAllRequestedExams() {
        if (!RoleType.ADMIN.equals(Claims.getRoleFromJwt())) {
            throw new UnauthorizedException("Only Admin can see all exam!");
        }

        List<ThesisExam> thesisExams = thesisExamRepository.findAllRequested();
        return thesisExams.stream()
                .map(ThesisExamMapper::toDTO)
                .toList();
    }

    public List<ThesisExam> getExamsByStatus(ThesisExamStatus status) {
        return thesisExamRepository.findByStatus(status);
    }
}
