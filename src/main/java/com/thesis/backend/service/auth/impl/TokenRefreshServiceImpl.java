package com.thesis.backend.service.auth.impl;

import com.thesis.backend.infrastructure.auth.dto.TokenPairResponseDTO;
import com.thesis.backend.service.auth.TokenGenerationService;
import com.thesis.backend.service.auth.TokenRefreshService;
import org.springframework.stereotype.Service;

@Service
public class TokenRefreshServiceImpl implements TokenRefreshService {
    private final TokenGenerationService tokenService;

    public TokenRefreshServiceImpl(TokenGenerationService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public TokenPairResponseDTO refreshAccessToken(String refreshToken) {
        String newAccessToken = tokenService.refreshAccessToken(refreshToken);
        return new TokenPairResponseDTO(newAccessToken, refreshToken, "Bearer");
    }
}
