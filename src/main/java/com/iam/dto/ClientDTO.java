package com.iam.dto;

import java.time.LocalDateTime;

import com.iam.models.Client;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Data Transfer Object for Client details.")
public class ClientDTO {

    @Schema(description = "Name of the client", example = "MyClientApp")
    private String name;

    @Schema(description = "Unique identifier for the client", example = "client-12345")
    private String clientId;

    @Schema(description = "Secret key for client authentication", example = "s3cr3tKey@123")
    private String clientSecret;

    @Schema(description = "Indicates whether the client account is disabled", example = "false")
    private boolean disabled;

    @Schema(description = "Timestamp when the client was created", example = "2024-03-14T10:15:30")
    private LocalDateTime createdAt;

    @Schema(description = "Timestamp when the client was last updated", example = "2024-03-14T12:30:45")
    private LocalDateTime updatedAt;

    // Constructor to map entity to DTO
    public ClientDTO(Client client) {
        this.name = client.getName();
        this.clientId = client.getClientId();
        this.clientSecret = client.getClientSecret();
        this.disabled = client.isDisabled();
        this.createdAt = client.getCreatedAt();
        this.updatedAt = client.getUpdatedAt();
    }
}
