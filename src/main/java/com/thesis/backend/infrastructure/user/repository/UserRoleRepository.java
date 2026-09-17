package com.thesis.backend.infrastructure.user.repository;

import com.thesis.backend.entity.user.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    void deleteByUserIdAndRoleId(Long userId, Long roleId);
}
