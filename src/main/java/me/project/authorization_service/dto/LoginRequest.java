package me.project.authorization_service.dto;

public record LoginRequest(
        String username,
        String password
) {
}
