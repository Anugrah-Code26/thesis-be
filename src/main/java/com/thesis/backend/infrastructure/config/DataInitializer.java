package com.thesis.backend.infrastructure.config;

import com.thesis.backend.entity.user.Role;
import com.thesis.backend.entity.user.RoleType;
import com.thesis.backend.infrastructure.user.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {
    @Bean
    CommandLineRunner initRoles(RoleRepository roleRepository) {
        return args -> {
            if (roleRepository.count() == 0) {
                Role adminRole = new Role();
                adminRole.setName(RoleType.ADMIN);
                roleRepository.save(adminRole);

                Role studentRole = new Role();
                studentRole.setName(RoleType.STUDENT);
                roleRepository.save(studentRole);

                Role supervisorRole = new Role();
                supervisorRole.setName(RoleType.SUPERVISOR);
                roleRepository.save(supervisorRole);

                Role examinerRole = new Role();
                examinerRole.setName(RoleType.EXAMINER);
                roleRepository.save(examinerRole);
            }
        };
    }
}
