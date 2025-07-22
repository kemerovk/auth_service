package me.project.authorization_service.service;

import lombok.RequiredArgsConstructor;
import me.project.authorization_service.dto.*;
import me.project.authorization_service.exception.UserAlreadyExistsException;
import me.project.authorization_service.model.Client;
import me.project.authorization_service.model.Role;
import me.project.authorization_service.repository.ClientRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UserDetailsService userDetailsService;

    public Jwt registerClient(SignUpRequest signUpRequest) {
        return register(signUpRequest, Role.GUEST);
    }

    public Jwt registerPremiumUser(SignUpRequest signUpRequest) {
        return register(signUpRequest, Role.PREMIUM_USER);
    }

    public Jwt registerAdmin(SignUpRequest signUpRequest) {
        return register(signUpRequest, Role.ADMIN);
    }

    public boolean changePassword(ChangePasswordRequest request) {
        Optional<Client> clientFromDb = clientRepository.findByUsername(request.username());
        if (clientFromDb.isPresent()) {
            Client client = clientFromDb.get();
            client.setPassword(passwordEncoder.encode(request.newPassword()));
            clientRepository.save(client);
            return true;
        }
        return false;
    }

    public Jwt register(SignUpRequest signUpRequest, Role role) {
        if (clientRepository.existsByUsername(signUpRequest.username()) ||
                clientRepository.existsByEmail(signUpRequest.email())) {
            throw new UserAlreadyExistsException("User already exists");
        }
        Client client = new Client();
        client.setUsername(signUpRequest.username());
        client.setPassword(passwordEncoder.encode(signUpRequest.password()));
        client.setEmail(signUpRequest.email());
        client.setRole(role);

        clientRepository.save(client);
        return tokenService.createToken(client);

    }


    public boolean changeRole(ChangeRoleRequest request) {
        Optional<Client> client = clientRepository.findByUsername(request.username());
        if (client.isPresent()) {
            Client toChange = client.get();
            if (request.role().equals(Role.ADMIN)) {
                return false;
            }
            toChange.setRole(request.role());
            clientRepository.save(toChange);
            return true;
        }
        return false;
    }

    public Jwt login(LoginRequest loginRequest) {
        authenticationManager.authenticate
                (
                        new UsernamePasswordAuthenticationToken
                                (loginRequest.username(), loginRequest.password())
                );


        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.username());
        return tokenService.createToken((Client) userDetails);
    }

    public Jwt refreshToken(Jwt jwt) {
        return tokenService.refreshToken(jwt);
    }


    public boolean logout(Jwt jwt) {
        try {
            tokenService.blacklistToken(jwt.refreshToken());
            return true;
        } catch (Exception e) {
            return false;
        }
    }


}
