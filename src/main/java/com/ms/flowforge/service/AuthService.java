package com.ms.flowforge.service;

import com.ms.flowforge.exception.AuthException;
import com.ms.flowforge.model.dto.AuthResponse;
import com.ms.flowforge.model.dto.LoginRequest;
import com.ms.flowforge.model.dto.RefreshRequest;
import com.ms.flowforge.model.dto.RegisterRequest;
import com.ms.flowforge.model.entity.RefreshToken;
import com.ms.flowforge.model.entity.Role;
import com.ms.flowforge.model.entity.User;
import com.ms.flowforge.repository.RefreshTokenRepository;
import com.ms.flowforge.repository.UserRepository;
import com.ms.flowforge.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

/**
 * Handles the full authentication lifecycle:
 * register → login → refresh → logout
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Value("${app.jwt.expiration-ms}")
    private long accessTokenExpirationMs;

    @Value("${app.jwt.refresh-expiration-ms}")
    private long refreshTokenExpirationMs;

    // ===== Register =====

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AuthException("An account with this email already exists");
        }

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .enabled(true)
                .build();

        user = userRepository.save(user);
        log.info("New user registered: {}", user.getEmail());

        return buildAuthResponse(user);
    }

    // ===== Login =====

    @Transactional
    public AuthResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (BadCredentialsException e) {
            // Use a generic message to prevent user enumeration
            throw new AuthException("Invalid email or password");
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AuthException("Invalid email or password"));

        log.info("User logged in: {}", user.getEmail());
        return buildAuthResponse(user);
    }

    // ===== Refresh =====

    @Transactional
    public AuthResponse refresh(RefreshRequest request) {
        RefreshToken storedToken = refreshTokenRepository.findByToken(request.getRefreshToken())
                .orElseThrow(() -> new AuthException("Refresh token not found or already used"));

        if (storedToken.isExpired()) {
            // Clean up the expired token
            refreshTokenRepository.delete(storedToken);
            throw new AuthException("Refresh token has expired. Please log in again.");
        }

        User user = storedToken.getUser();

        // Token rotation — delete old refresh token, issue new pair
        refreshTokenRepository.delete(storedToken);

        log.info("Token refreshed for user: {}", user.getEmail());
        return buildAuthResponse(user);
    }

    // ===== Logout =====

    @Transactional
    public void logout(String userEmail) {
        userRepository.findByEmail(userEmail).ifPresent(user -> {
            refreshTokenRepository.deleteAllByUser(user);
            log.info("User logged out, tokens revoked: {}", userEmail);
        });
    }

    // ===== Private helpers =====

    private AuthResponse buildAuthResponse(User user) {
        String accessToken = jwtService.generateToken(user);
        String refreshToken = createRefreshToken(user);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .tokenType("Bearer")
                .refreshToken(refreshToken)
                .userId(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole())
                .expiresIn(accessTokenExpirationMs)
                .build();
    }

    private String createRefreshToken(User user) {
        RefreshToken refreshToken = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .user(user)
                .expiresAt(Instant.now().plusMillis(refreshTokenExpirationMs))
                .build();

        refreshTokenRepository.save(refreshToken);
        return refreshToken.getToken();
    }
}
