package com.iam.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO for creating a new user, including validation constraints.")
public class NewUserDTO {

    @Schema(description = "Unique username for the user", example = "john_doe")
    @NotBlank
    private String username;

    @Schema(description = "Email address of the user", example = "john.doe@example.com")
    @Email
    @NotBlank
    private String email;

    @Schema(description = "First name of the user", example = "John")
    @NotBlank
    private String firstName;

    @Schema(description = "Last name of the user", example = "Doe")
    @NotBlank
    private String lastName;

    @Schema(description = "Password for the user, must be at least 8 characters", example = "StrongPass123")
    @NotBlank
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;
}
