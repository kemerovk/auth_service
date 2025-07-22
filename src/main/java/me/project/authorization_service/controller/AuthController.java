package me.project.authorization_service.controller;

import lombok.RequiredArgsConstructor;
import me.project.authorization_service.dto.*;
import me.project.authorization_service.service.ClientServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/auth")
public class AuthController {

    private final ClientServiceImpl clientService;

    @PutMapping("register")
    public ResponseEntity<?> registerUser(@RequestBody SignUpRequest signUpRequest){
        return ResponseEntity.ok(clientService.registerClient(signUpRequest));
    }

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(clientService.login(request));
    }

    @PostMapping("refresh_token")
    public ResponseEntity<?> refreshToken(@RequestBody Jwt jwt){
        return ResponseEntity.ok(clientService.refreshToken(jwt));
    }

    @PostMapping("logout")
    public ResponseEntity<?> logout(Jwt jwt){
        return ResponseEntity.ok(clientService.logout(jwt));
    }

    @PatchMapping("change_password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest){
        return ResponseEntity.ok(clientService.changePassword(changePasswordRequest));
    }

    @PatchMapping("change_role")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> changeRole(@RequestBody ChangeRoleRequest roleRequest){
        return ResponseEntity.ok(clientService.changeRole(roleRequest));
    }
}
