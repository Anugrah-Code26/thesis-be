package com.thesis.backend;

import com.thesis.backend.infrastructure.config.JwtConfigProperties;
import lombok.extern.java.Log;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@Log
@SpringBootApplication
@EnableConfigurationProperties(JwtConfigProperties.class)
@EnableCaching
@EnableScheduling
public class ThesisExaminationSystemApplication {
	public static void main(String[] args) {
		SpringApplication.run(ThesisExaminationSystemApplication.class, args);
	}

//	@Bean
//	public CommandLineRunner initData(
//			UserRepository userRepository,
//			PasswordEncoder passwordEncoder) {
//		return args -> {
//			// Initialize with some test data if needed
//		};
//	}
}