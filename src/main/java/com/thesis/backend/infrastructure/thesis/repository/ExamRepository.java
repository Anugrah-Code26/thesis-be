package com.thesis.backend.infrastructure.thesis.repository;

import com.thesis.backend.entity.assessment.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {

}