package com.thesis.backend.service.auth;

public interface TokenBlacklistService {
    void blacklistToken(String token, String expiredAt);
    boolean isTokenBlacklisted(String token);
}
