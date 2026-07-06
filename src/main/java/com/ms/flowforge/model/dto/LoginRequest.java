package com.ms.flowforge.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Login request payload.
 */
@Data
@Schema(description = "Login request payload")
public class LoginRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be a valid address")
    @Schema(description = "User email", example = "user@example.com", required = true)
    private String email;

    @NotBlank(message = "Password is required")
    @Schema(description = "User password", example = "password123", required = true)
    private String password;
}
