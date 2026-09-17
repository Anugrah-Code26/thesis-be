package com.thesis.backend.infrastructure.assessment.repository;

import com.thesis.backend.entity.assessment.AssessmentRole;
import com.thesis.backend.entity.assessment.AssessmentRubric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssessmentRubricRepository extends JpaRepository<AssessmentRubric, Long> {

    @Query("SELECT ar FROM AssessmentRubric ar WHERE ar.exam.id = :examId")
    List<AssessmentRubric> findByExamId(Long examId);

    @Query("SELECT ar FROM AssessmentRubric ar WHERE ar.assessmentRole = :assessmentRole")
    List<AssessmentRubric> findByAssessmentRole(AssessmentRole assessmentRole);

}