package com.thesis.backend.infrastructure.user.repository;

import com.thesis.backend.entity.user.Lecturer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LecturerRepository extends JpaRepository<Lecturer, Long> {
    @Query(value = "SELECT l FROM Lecturer l WHERE l.uniqueId = :uniqueId")
    Optional<Lecturer> findByUniqueId(@Param("uniqueId") String uniqueId);
}
