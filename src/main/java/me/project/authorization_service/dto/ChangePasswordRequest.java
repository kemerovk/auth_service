package me.project.authorization_service.dto;

public record ChangePasswordRequest(
        String username,
        String oldPassword,
        String newPassword
) {
}
