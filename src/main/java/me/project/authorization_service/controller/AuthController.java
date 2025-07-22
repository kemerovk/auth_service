package me.project.authorization_service.controller;

import lombok.RequiredArgsConstructor;
import me.project.authorization_service.dto.Jwt;
import me.project.authorization_service.dto.LoginRequest;
import me.project.authorization_service.dto.SignUpRequest;
import me.project.authorization_service.service.ClientServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/auth")
public class AuthController {

    private final ClientServiceImpl clientService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("register")
    public ResponseEntity<?> registerUser(@RequestBody SignUpRequest signUpRequest){
        return ResponseEntity.ok(clientService.registerClient(signUpRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(clientService.login(request));
    }

    @PostMapping("refresh_token")
    public ResponseEntity<?> refreshToken(@RequestBody Jwt jwt){
        return ResponseEntity.ok(clientService.refreshToken(jwt));
    }

    @PostMapping("logout")
    public ResponseEntity<?> logout(Jwt jwt){
        return  ResponseEntity.ok(clientService.logout(jwt));
    }
}
