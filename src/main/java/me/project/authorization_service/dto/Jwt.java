package me.project.authorization_service.dto;

public record Jwt(
        String accessToken,
        String refreshToken){
}
