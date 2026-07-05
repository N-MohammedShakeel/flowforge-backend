package com.ms.flowforge.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

/**
 * Persisted refresh token for token rotation.
 * On refresh, the old token is deleted and a new pair (access + refresh) is issued.
 * This prevents replay attacks on stolen refresh tokens.
 */
@Entity
@Table(name = "refresh_tokens")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    /**
     * The actual refresh token string (UUID v4, stored as-is).
     */
    @Column(nullable = false, unique = true)
    private String token;

    /**
     * Owner of this token. Cascading kept manual for clarity.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Absolute expiry instant (epoch milliseconds).
     */
    @Column(nullable = false)
    private Instant expiresAt;

    public boolean isExpired() {
        return Instant.now().isAfter(expiresAt);
    }
}
