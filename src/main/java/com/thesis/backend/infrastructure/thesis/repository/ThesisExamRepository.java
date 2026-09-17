package com.thesis.backend.infrastructure.thesis.repository;

import com.thesis.backend.entity.thesis.ThesisExam;
import com.thesis.backend.entity.thesis.ThesisExamStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface ThesisExamRepository extends JpaRepository<ThesisExam, Long>, JpaSpecificationExecutor<ThesisExam> {
    @Query("SELECT te FROM ThesisExam te WHERE te.thesis.student.id = :studentId")
    List<ThesisExam> findByThesisStudentId(@Param("studentId") String studentId);

    @Query("""
        SELECT te FROM ThesisExam te
        JOIN te.thesis t
        JOIN t.supervisors s
        WHERE (:supervisorId IS NULL OR s.lecturer.id = :supervisorId)
    """)
    Page<ThesisExam> findBySupervisorId(@Param("supervisorId") Long supervisorId, Pageable pageable);

    @Query("SELECT te FROM ThesisExam te WHERE te.status = 'REQUESTED'")
    List<ThesisExam> findAllRequested();

    @Query("SELECT te FROM ThesisExam te WHERE te.status = :status")
    List<ThesisExam> findByStatus(ThesisExamStatus status);

    List<ThesisExam> findByScheduledDateBetween(OffsetDateTime start, OffsetDateTime end);
}