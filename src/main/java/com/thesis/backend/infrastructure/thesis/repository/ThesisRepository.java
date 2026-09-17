package com.thesis.backend.infrastructure.thesis.repository;

import com.thesis.backend.entity.thesis.Thesis;
import com.thesis.backend.entity.thesis.ThesisStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ThesisRepository extends JpaRepository<Thesis, Long> {
    @Query("SELECT t FROM Thesis t WHERE t.student.id = :studentId")
    Optional<Thesis> findByStudentId(@Param("studentId") Long studentId);

//    @Query("SELECT t FROM Thesis t WHERE t.supervisor.supervisorId = :supervisorId")
//    List<Thesis> findBySupervisorId(String supervisorId);

    @Query("SELECT t FROM Thesis t WHERE t.status = :status")
    List<Thesis> findByStatus(ThesisStatus status);

//    List<Thesis> findByStatusIn(List<ThesisStatus> statuses);
}