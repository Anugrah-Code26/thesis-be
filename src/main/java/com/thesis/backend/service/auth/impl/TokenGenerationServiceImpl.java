package com.thesis.backend.service.auth.impl;

import com.thesis.backend.common.exceptions.DataNotFoundException;
import com.thesis.backend.service.auth.TokenGenerationService;
import com.thesis.backend.entity.user.User;
import com.thesis.backend.infrastructure.user.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class TokenGenerationServiceImpl implements TokenGenerationService {
    private final JwtEncoder jwtEncoder;
    private final UserRepository userRepository;
    private final JwtDecoder jwtDecoder;

    private final long ACCESS_TOKEN_EXPIRY = 7200L; // 2 hours
    private final long REFRESH_TOKEN_EXPIRY = 86400L; // 24 hours

    public TokenGenerationServiceImpl(JwtEncoder jwtEncoder, UserRepository userRepository, JwtDecoder jwtDecoder) {
        this.jwtEncoder = jwtEncoder;
        this.userRepository = userRepository;
        this.jwtDecoder = jwtDecoder;
    }

    @Override
    public String generateToken(Authentication authentication, TokenType tokenType) {
        Instant now = Instant.now();
        long expiry = (tokenType == TokenType.ACCESS) ? ACCESS_TOKEN_EXPIRY : REFRESH_TOKEN_EXPIRY;

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new DataNotFoundException("User not found"));

        String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .reduce((a, b) -> a + " " + b)
                .orElse("");

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiry))
                .subject(email)
                .claim("role", role)
                .claim("userId", user.getId())
                .claim("type", tokenType.name())
                .build();

        JwsHeader jwsHeader = JwsHeader.with(() -> "HS256").build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }

    @Override
    public String refreshAccessToken(String refreshToken) {
        Jwt jwt = this.jwtDecoder.decode(refreshToken);
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(now.plusSeconds(ACCESS_TOKEN_EXPIRY))
                .subject(jwt.getSubject())
                .claim("scope", jwt.getClaimAsString("scope"))
                .claim("userId", jwt.getClaimAsString("userId"))
                .claim("type", TokenType.ACCESS.name())
                .build();

        JwsHeader jwsHeader = JwsHeader.with(() -> "HS256").build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }
}
