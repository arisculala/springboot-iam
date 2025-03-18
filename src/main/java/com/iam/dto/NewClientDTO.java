package com.iam.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO for creating a new client, including validation constraints.")
public class NewClientDTO {

    @Schema(description = "Unique name for the client", example = "MyClientApp")
    @NotBlank
    private String name;
}
