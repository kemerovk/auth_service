package me.project.authorization_service.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record SignUpRequest(
        @NotEmpty
        @NotBlank
        String username,
        @NotEmpty
        @NotBlank
        String password,
        @Email
        String email
)
{}
