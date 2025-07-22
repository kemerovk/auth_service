package me.project.authorization_service.dto;


public record SignUpRequest(
        String username,
        String password,
        String email
)
{}
