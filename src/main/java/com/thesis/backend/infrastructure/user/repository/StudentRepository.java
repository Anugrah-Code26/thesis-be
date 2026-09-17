package com.thesis.backend.infrastructure.user.repository;

import com.thesis.backend.entity.user.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    @Query(value = "SELECT s FROM Student s WHERE s.uniqueId = :uniqueId")
    Optional<Student> findByStudentId(@Param("uniqueId") String uniqueId);

    @Query(value = "SELECT s FROM Student s WHERE s.department = :department")
    Optional<Student> findByDepartment(@Param("department") String department);
}
