package com.ms.flowforge.controller;

import com.ms.flowforge.model.dto.AuthResponse;
import com.ms.flowforge.model.dto.LoginRequest;
import com.ms.flowforge.model.dto.RefreshRequest;
import com.ms.flowforge.model.dto.RegisterRequest;
import com.ms.flowforge.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Authentication endpoints — all publicly accessible (no JWT required).
 *
 * POST /api/auth/register  → create account, returns token pair
 * POST /api/auth/login     → authenticate, returns token pair
 * POST /api/auth/refresh   → rotate refresh token, returns new token pair
 * POST /api/auth/logout    → revoke all refresh tokens for authenticated user
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints for user authentication and authorization")
public class AuthController {

    private final AuthService authService;

    /**
     * Register a new user account.
     * Returns 201 CREATED with the full auth response on success.
     */
    @PostMapping("/register")
    @Operation(summary = "Register user", description = "Create a new user account and return authentication tokens")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User registered successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Authenticate with email + password.
     * Returns 200 OK with the full auth response on success.
     */
    @PostMapping("/login")
    @Operation(summary = "Login user", description = "Authenticate with email and password, return authentication tokens")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login successful"),
        @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Exchange a valid refresh token for a new access + refresh token pair.
     * The old refresh token is invalidated (token rotation).
     */
    @PostMapping("/refresh")
    @Operation(summary = "Refresh token", description = "Exchange a valid refresh token for a new access and refresh token pair")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Token refreshed successfully"),
        @ApiResponse(responseCode = "401", description = "Invalid refresh token")
    })
    public ResponseEntity<AuthResponse> refresh(@Valid @RequestBody RefreshRequest request) {
        AuthResponse response = authService.refresh(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Logout — revokes all refresh tokens for the authenticated user.
     * The client should also delete its stored tokens.
     */
    @PostMapping("/logout")
    @Operation(summary = "Logout user", description = "Revoke all refresh tokens for the authenticated user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Logout successful"),
        @ApiResponse(responseCode = "401", description = "User not authenticated")
    })
    public ResponseEntity<Map<String, String>> logout(
            @AuthenticationPrincipal UserDetails userDetails) {
        authService.logout(userDetails.getUsername());
        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }
}
