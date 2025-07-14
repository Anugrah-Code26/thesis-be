package com.thesis.backend.service.auth;

import com.thesis.backend.infrastructure.auth.dto.LoginRequestDTO;
import com.thesis.backend.infrastructure.auth.dto.TokenPairResponseDTO;

public interface LoginService {
    TokenPairResponseDTO authenticateUser(LoginRequestDTO req);
}
