package com.iam.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iam.models.Client;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByClientId(String clientId);
}
