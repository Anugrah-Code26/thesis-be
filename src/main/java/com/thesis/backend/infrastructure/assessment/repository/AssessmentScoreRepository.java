package com.thesis.backend.infrastructure.assessment.repository;

import com.thesis.backend.entity.assessment.AssessmentScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssessmentScoreRepository extends JpaRepository<AssessmentScore, Long> {
    @Query("SELECT e FROM AssessmentScore e WHERE e.thesisExam.id = :examId")
    List<AssessmentScore> findByExamId(@Param("examId") Long examId);

    @Query("SELECT e FROM AssessmentScore e WHERE e.evaluator.id = :evaluatorId")
    List<AssessmentScore> findByEvaluatorId(@Param("evaluatorId") Long evaluatorId);
}