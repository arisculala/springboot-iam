package com.iam.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientValidationDTO {
    @NotBlank(message = "Client ID is required")
    private String clientId;

    @NotBlank(message = "Client Secret is required")
    private String clientSecret;
}
