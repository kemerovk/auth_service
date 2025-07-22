package me.project.authorization_service.service;

import lombok.RequiredArgsConstructor;
import me.project.authorization_service.dto.Jwt;
import me.project.authorization_service.dto.LoginRequest;
import me.project.authorization_service.dto.SignUpRequest;
import me.project.authorization_service.dto.SignUpResponse;
import me.project.authorization_service.model.Client;
import me.project.authorization_service.model.Role;
import me.project.authorization_service.repository.ClientRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UserDetailsService userDetailsService;


    @Override
    public Jwt registerClient(SignUpRequest signUpRequest){
        if (clientRepository.existsByUsername(signUpRequest.username())){
            throw new RuntimeException("User already exists");
        }
        else if (clientRepository.existsByEmail(signUpRequest.email())){
            throw new RuntimeException("User with email " + signUpRequest.email() + " already exists");
        }

        Client client = new Client();
        client.setUsername(signUpRequest.username());
        client.setPassword(passwordEncoder.encode(signUpRequest.password()));
        client.setEmail(signUpRequest.email());
        client.setRole(Role.GUEST);
        System.out.println(Role.GUEST);

        clientRepository.save(client);
        return tokenService.createToken(client);

    }


    public Jwt login(LoginRequest loginRequest){
        try {
            authenticationManager.authenticate
                    (new UsernamePasswordAuthenticationToken
                            (loginRequest.username(), loginRequest.password())
                    );

        }
        catch (BadCredentialsException e) {
            throw new RuntimeException("Bad credentials");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.username());
        return tokenService.createToken((Client) userDetails);
    }

    public Jwt refreshToken(Jwt jwt){
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
