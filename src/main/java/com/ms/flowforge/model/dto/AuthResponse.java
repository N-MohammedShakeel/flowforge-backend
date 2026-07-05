package com.ms.flowforge.model.dto;

import com.ms.flowforge.model.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Unified auth response for register, login, and refresh endpoints.
 * The frontend stores accessToken in memory and refreshToken in an
 * httpOnly cookie (or localStorage for MVP simplicity).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    private String accessToken;

    @Builder.Default
    private String tokenType = "Bearer";

    private String refreshToken;

    // User info so the frontend can populate the auth state immediately
    private String userId;
    private String email;
    private String firstName;
    private String lastName;
    private Role role;

    /** Expiry in milliseconds from issuedAt — for frontend countdown. */
    private long expiresIn;
}
