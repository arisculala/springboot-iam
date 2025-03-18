package com.iam.services;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.generic.exceptions.NotFoundException;
import com.iam.dto.ClientDTO;
import com.iam.models.Client;
import com.iam.repositories.ClientRepository;

/**
 * Service class for managing clients.
 */
@Service
public class ClientService {
    private static final Logger logger = LoggerFactory.getLogger(ClientService.class);

    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;

    public ClientService(ClientRepository clientRepository, PasswordEncoder passwordEncoder) {
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registers a new client.
     *
     * @param name the name of the client
     * @return the registered client with raw client secret
     */
    public Client registerClient(String name) {
        logger.info("Registering new client with name: {}", name);

        String clientId = UUID.randomUUID().toString(); // Generate unique client ID
        String rawClientSecret = generateSecureRandomSecret(); // Generate secure random secret
        String hashedSecret = passwordEncoder.encode(rawClientSecret); // Hash secret before saving

        Client client = new Client();
        client.setName(name);
        client.setClientId(clientId);
        client.setClientSecret(hashedSecret);

        clientRepository.save(client);
        logger.info("Client registered successfully: {}", clientId);

        // Return the client with raw secret (DO NOT return hashed secret)
        client.setClientSecret(rawClientSecret);
        return client;
    }

    /**
     * Validates a client's credentials.
     *
     * @param clientId     the client's ID
     * @param clientSecret the client's secret
     * @return true if credentials are valid, false otherwise
     */
    public boolean validateClient(String clientId, String clientSecret) {
        logger.info("Validating client credentials for client ID: {}", clientId);

        return clientRepository.findByClientId(clientId)
                .map(client -> {
                    boolean isValid = passwordEncoder.matches(clientSecret, client.getClientSecret());
                    if (!isValid) {
                        logger.warn("Client secret validation failed for client ID: {}", clientId);
                    }
                    return isValid;
                })
                .orElseGet(() -> {
                    logger.warn("Client not found for validation. ID: {}", clientId);
                    return false;
                });
    }

    /**
     * Retrieves a client by client ID.
     *
     * @param clientId the client's ID
     * @return the client
     * @throws NotFoundException if the client does not exist
     */
    public ClientDTO getClient(String clientId) {
        logger.info("Fetching client by ID: {}", clientId);

        return clientRepository.findByClientId(clientId)
                .map(ClientDTO::new)
                .orElseThrow(() -> {
                    logger.warn("Client not found with ID: {}", clientId);
                    return new NotFoundException("Client not found");
                });
    }

    /**
     * Generates a secure random client secret.
     *
     * @return a base64 encoded secure random string
     */
    private String generateSecureRandomSecret() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] secretBytes = new byte[32];
        secureRandom.nextBytes(secretBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(secretBytes);
    }

    /**
     * Updates client details.
     *
     * @param clientId the client's ID
     * @param newName  the new name for the client
     * @return the updated client
     * @throws NotFoundException if the client does not exist
     */
    public Client updateClient(String clientId, String newName) {
        logger.info("Updating client details for client ID: {}", clientId);

        Client client = clientRepository.findByClientId(clientId)
                .orElseThrow(() -> {
                    logger.warn("Client not found for update. ID: {}", clientId);
                    return new NotFoundException("Client not found");
                });

        client.setName(newName);
        clientRepository.save(client);

        logger.info("Client updated successfully: {}", clientId);
        return client;
    }

    /**
     * Regenerates the client secret.
     *
     * @param clientId the client's ID
     * @return the new raw client secret (not hashed)
     * @throws NotFoundException if the client does not exist
     */
    public String regenerateClientSecret(String clientId) {
        logger.info("Regenerating client secret for client ID: {}", clientId);

        Client client = clientRepository.findByClientId(clientId)
                .orElseThrow(() -> {
                    logger.warn("Client not found for secret regeneration. ID: {}", clientId);
                    return new NotFoundException("Client not found");
                });

        String newRawSecret = generateSecureRandomSecret();
        client.setClientSecret(passwordEncoder.encode(newRawSecret));
        clientRepository.save(client);

        logger.info("Client secret regenerated successfully: {}", clientId);
        return newRawSecret;
    }

    /**
     * Disables or enables a client instead of deleting it.
     *
     * @param clientId the client's ID
     * @param disabled whether to disable (true) or enable (false) the client
     * @throws NotFoundException if the client does not exist
     */
    public void updateClientDisabledStatus(String clientId, boolean disabled) {
        logger.info("Updating disabled status for client ID: {} to {}", clientId, disabled);

        Client client = clientRepository.findByClientId(clientId)
                .orElseThrow(() -> {
                    logger.warn("Client not found when updating disabled status. ID: {}", clientId);
                    return new NotFoundException("Client not found");
                });

        client.setDisabled(disabled);
        clientRepository.save(client);

        logger.info("Client {} disabled status updated to {}", clientId, disabled);
    }

    /**
     * Retrieves all clients.
     *
     * @return a list of ClientDTOs
     */
    public List<ClientDTO> getAllClients() {
        logger.info("Fetching all registered clients");

        List<Client> clients = clientRepository.findAll();
        logger.info("Total clients found: {}", clients.size());

        return clients.stream()
                .map(client -> new ClientDTO(client))
                .toList();
    }

}
