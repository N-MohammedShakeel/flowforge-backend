package com.ms.flowforge.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Payload for the token refresh endpoint.
 */
@Data
public class RefreshRequest {

    @NotBlank(message = "Refresh token is required")
    private String refreshToken;
}
