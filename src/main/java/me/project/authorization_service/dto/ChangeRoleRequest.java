package me.project.authorization_service.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import me.project.authorization_service.model.Role;
public record ChangeRoleRequest(
        @NotBlank
        @NotEmpty
        String username,
        Role role
) {
}
