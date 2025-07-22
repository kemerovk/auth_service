package me.project.authorization_service.dto;

public record SignUpResponse(
        int id,
        String username,
        String email
) {
}
