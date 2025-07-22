package me.project.authorization_service.repository;

import me.project.authorization_service.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {
    Optional<Client> findByUsername(String email);
    Optional<Client> findByEmail(String email);
}
