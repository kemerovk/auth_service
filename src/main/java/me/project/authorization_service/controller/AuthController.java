package me.project.authorization_service.controller;

import lombok.RequiredArgsConstructor;
import me.project.authorization_service.dto.SignUpRequest;
import me.project.authorization_service.service.ClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/auth")
public class AuthController {

    private final ClientService clientService;


    @PostMapping("register")
    public ResponseEntity<?> registerUser(@RequestBody SignUpRequest signUpRequest){
        return ResponseEntity.ok(clientService.registerClient(signUpRequest));
    }
}
