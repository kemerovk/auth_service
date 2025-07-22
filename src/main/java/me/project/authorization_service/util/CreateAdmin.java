package me.project.authorization_service.util;

import lombok.RequiredArgsConstructor;
import me.project.authorization_service.dto.SignUpRequest;
import me.project.authorization_service.exception.UserAlreadyExistsException;
import me.project.authorization_service.service.ClientService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateAdmin implements ApplicationRunner {
    private final ClientService clientService;
    private final PasswordEncoder passwordEncoder;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        try {
            clientService.registerAdmin(new SignUpRequest("admin", "admin", "admin@admin.com"));
            clientService.registerPremiumUser(new SignUpRequest("premium", "premium", "premium@premium.com"));

        } catch (UserAlreadyExistsException e) {
            System.out.println("users already exist");
        }
        }
}
