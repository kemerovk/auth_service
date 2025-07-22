package me.project.authorization_service.service;

import me.project.authorization_service.dto.Jwt;
import me.project.authorization_service.dto.SignUpRequest;
import me.project.authorization_service.dto.SignUpResponse;

public interface ClientService {

    public Jwt registerClient(SignUpRequest signUpRequest);
}
