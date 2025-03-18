package com.iam.controllers;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.generic.dto.SuccessErrorResponseDTO;
import com.iam.dto.ClientDTO;
import com.iam.dto.ClientStatusUpdateDTO;
import com.iam.dto.ClientValidationDTO;
import com.iam.dto.NewClientDTO;
import com.iam.models.Client;
import com.iam.services.ClientService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.ws.rs.InternalServerErrorException;

@RestController
@RequestMapping("/api/v1/clients")
@Tag(name = "Client Management", description = "Endpoints for managing API clients")
public class ClientController {
    private static final Logger logger = LoggerFactory.getLogger(ClientController.class);

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @Operation(summary = "Register a New Client", description = "Creates a new client and returns its credentials.", responses = {
            @ApiResponse(responseCode = "200", description = "Client registered successfully", content = @Content(schema = @Schema(implementation = Client.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping
    public ResponseEntity<?> registerClient(@Valid @RequestBody NewClientDTO request) {
        try {
            Client client = clientService.registerClient(request.getName());
            return ResponseEntity.ok(Map.of(
                    "clientId", client.getClientId(),
                    "clientSecret", client.getClientSecret()));
        } catch (Exception e) {
            logger.error("Error registering client: {}", e.getMessage(), e);
            throw new InternalServerErrorException("Error registering client.");
        }
    }

    @Operation(summary = "Validate Client Credentials", description = "Validates the provided client ID and secret.", responses = {
            @ApiResponse(responseCode = "200", description = "Validation result returned"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @PostMapping("/validate")
    public ResponseEntity<?> validateClient(@Valid @RequestBody ClientValidationDTO request) {
        boolean isValid = clientService.validateClient(request.getClientId(), request.getClientSecret());
        return ResponseEntity.ok(Map.of("valid", isValid));
    }

    @Operation(summary = "Get Client by ID", description = "Fetches a client using its unique client ID.", responses = {
            @ApiResponse(responseCode = "200", description = "Client found", content = @Content(schema = @Schema(implementation = Client.class))),
            @ApiResponse(responseCode = "404", description = "Client not found")
    })
    @GetMapping("/{clientId}")
    public ResponseEntity<?> getClient(
            @Parameter(description = "Client ID", required = true) @PathVariable String clientId) {
        ClientDTO client = clientService.getClient(clientId);
        return ResponseEntity.ok(client);
    }

    @Operation(summary = "Update Client Status", description = "Enable or disable a client instead of deleting it.", responses = {
            @ApiResponse(responseCode = "200", description = "Client status updated successfully", content = @Content(schema = @Schema(implementation = SuccessErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Client not found")
    })
    @PatchMapping("/{clientId}/disable")
    public ResponseEntity<SuccessErrorResponseDTO> updateClientDisabledStatus(
            @Parameter(description = "Client ID", required = true) @PathVariable String clientId,
            @Valid @RequestBody ClientStatusUpdateDTO request) {
        logger.info("Calling updateClientDisabledStatus endpoint");

        clientService.updateClientDisabledStatus(clientId, request.isDisabled());
        String message = request.isDisabled() ? "Client status successfully disabled."
                : "Client status successfully enabled.";

        return ResponseEntity.ok(new SuccessErrorResponseDTO(HttpStatus.OK, true, message));
    }

    @Operation(summary = "Get All Clients", description = "Retrieves a list of all registered clients.", responses = {
            @ApiResponse(responseCode = "200", description = "List of clients", content = @Content(schema = @Schema(implementation = ClientDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<ClientDTO>> getAllClients() {
        logger.info("Calling getAllClients endpoint");

        List<ClientDTO> clients = clientService.getAllClients();
        return ResponseEntity.ok(clients);
    }
}
