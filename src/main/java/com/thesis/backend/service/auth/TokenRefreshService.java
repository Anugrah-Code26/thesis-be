package com.thesis.backend.service.auth;

import com.thesis.backend.infrastructure.auth.dto.TokenPairResponseDTO;

public interface TokenRefreshService {
    TokenPairResponseDTO refreshAccessToken(String refreshToken);
}
