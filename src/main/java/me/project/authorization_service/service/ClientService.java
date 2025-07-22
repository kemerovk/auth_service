package me.project.authorization_service.service;

import me.project.authorization_service.dto.ChangeRoleRequest;
import me.project.authorization_service.dto.Jwt;
import me.project.authorization_service.dto.LoginRequest;
import me.project.authorization_service.dto.SignUpRequest;
import me.project.authorization_service.model.Role;

public interface ClientService {

    Jwt registerClient(SignUpRequest signUpRequest);
    Jwt registerPremiumUser(SignUpRequest signUpRequest);
    Jwt registerAdmin(SignUpRequest signUpRequest);

    Jwt register(SignUpRequest signUpRequest, Role role);

    boolean changeRole(ChangeRoleRequest request);

    Jwt login(LoginRequest loginRequest);
    Jwt refreshToken(Jwt jwt);

    boolean logout(Jwt jwt);
}
