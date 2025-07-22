package me.project.authorization_service.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import me.project.authorization_service.dto.SignUpRequest;
import me.project.authorization_service.dto.SignUpResponse;
import me.project.authorization_service.model.Client;
import me.project.authorization_service.repository.ClientRepository;
import me.project.authorization_service.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public SignUpResponse registerClient(SignUpRequest signUpRequest){
        if (clientRepository.findByUsername(signUpRequest.username()).isPresent()){
            throw new RuntimeException("User already exists");
        }
        else if (clientRepository.findByEmail(signUpRequest.email()).isPresent()){
            throw new RuntimeException("User with email " + signUpRequest.email() + " already exists");
        }

        Client client = new Client();
        client.setUsername(signUpRequest.username());
        client.setPassword(passwordEncoder.encode(signUpRequest.password()));
        client.setEmail(signUpRequest.email());

        clientRepository.save(client);
        return new SignUpResponse(client.getClientId(), client.getUsername(), client.getEmail());

    }

}
