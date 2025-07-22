package me.project.authorization_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record ChangePasswordRequest(
        @NotEmpty
        @NotBlank
        String username,
        @NotEmpty
        @NotBlank
        String oldPassword,
        @NotEmpty
        @NotBlank
        String newPassword
) {
}
