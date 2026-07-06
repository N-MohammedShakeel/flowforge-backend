package com.ms.flowforge.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Registration request payload.
 * All fields are validated before hitting the service layer.
 */
@Data
@Schema(description = "Registration request payload")
public class RegisterRequest {

    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    @Schema(description = "User first name", example = "John", required = true)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    @Schema(description = "User last name", example = "Doe", required = true)
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be a valid address")
    @Schema(description = "User email", example = "user@example.com", required = true)
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
    @Schema(description = "User password", example = "password123", required = true)
    private String password;
}
