package com.thesis.backend.service.user.impl;

import com.thesis.backend.common.exceptions.EmailAlreadyExistsException;
import com.thesis.backend.common.exceptions.UnauthorizedException;
import com.thesis.backend.entity.user.*;
import com.thesis.backend.infrastructure.auth.Claims;
import com.thesis.backend.infrastructure.user.dto.UserDTO;
import com.thesis.backend.infrastructure.user.mapper.UserMapper;
import com.thesis.backend.infrastructure.user.repository.RoleRepository;
import com.thesis.backend.infrastructure.user.repository.UserRepository;
import com.thesis.backend.service.user.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserDTO registerUser(UserDTO userDTO) throws EmailAlreadyExistsException {
        String userRole = Claims.getRoleFromJwt();
        if (userRole == null || !userRole.equals(RoleType.ADMIN)) {
            throw new UnauthorizedException("Only Admin can create new User!");
        }

        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new EmailAlreadyExistsException("Email already in use");
        }

        // Determine if it's Lecturer (special case)
        boolean isLecturer = userDTO.getRoles().stream()
                .anyMatch(r -> r.equalsIgnoreCase("LECTURER"));

        User user;
        if (userDTO.getRoles().contains(RoleType.STUDENT)) {
            Student student = new Student();
            student.setUniqueId(userDTO.getUniqueId());
            user = student;
        } else if (isLecturer || userDTO.getRoles().contains(RoleType.SUPERVISOR)
                || userDTO.getRoles().contains(RoleType.EXAMINER)) {
            Lecturer lecturer = new Lecturer(); // new entity extending User
            lecturer.setUniqueId(userDTO.getUniqueId());
            user = lecturer;
        } else {
            user = new User(); // Default for ADMIN
        }

        // Common fields
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode("User1234!")); // Default password
        user.setName(userDTO.getName());
        user.setUniversity(userDTO.getUniversity());
        user.setFaculty(userDTO.getFaculty());
        user.setDepartment(userDTO.getDepartment());

        // Save user (needed for ID)
        User savedUser = userRepository.save(user);

        // Assign roles
        if (isLecturer) {
            roleRepository.findByName(RoleType.SUPERVISOR).ifPresent(savedUser::addRole);
            roleRepository.findByName(RoleType.EXAMINER).ifPresent(savedUser::addRole);
        } else {
            for (String roleName : userDTO.getRoles()) {
                roleRepository.findByName(roleName)
                        .ifPresentOrElse(
                                savedUser::addRole,
                                () -> { throw new IllegalArgumentException("Role not found: " + roleName); }
                        );
            }
        }

        savedUser = userRepository.save(savedUser);
        return UserMapper.toUserDTO(savedUser);
    }
}
