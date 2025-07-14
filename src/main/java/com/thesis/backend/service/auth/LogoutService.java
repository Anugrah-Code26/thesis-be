package com.thesis.backend.service.auth;

import com.thesis.backend.infrastructure.auth.dto.LogoutRequestDTO;

public interface LogoutService {
    Boolean logoutUser(LogoutRequestDTO req);
}
