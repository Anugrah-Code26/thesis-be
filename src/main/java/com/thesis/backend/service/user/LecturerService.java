package com.thesis.backend.service.user;

import com.thesis.backend.infrastructure.user.dto.LecturerDTO;

import java.util.List;

public interface LecturerService {
    List<LecturerDTO> getAllLecturer();
}
