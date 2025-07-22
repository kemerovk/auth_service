package me.project.authorization_service.dto;
import me.project.authorization_service.model.Role;
public record ChangeRoleRequest(
        String username,
        Role role
) {
}
