package com.iam.dto;

import java.time.LocalDateTime;

import com.iam.models.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Data Transfer Object for User details.")
public class UserDTO {

    @Schema(description = "Unique identifier of the user", example = "292551982516801536")
    private Long id;

    @Schema(description = "Username of the user", example = "john_doe")
    private String username;

    @Schema(description = "Email address of the user", example = "john.doe@example.com")
    private String email;

    @Schema(description = "First name of the user", example = "John")
    private String firstName;

    @Schema(description = "Last name of the user", example = "Doe")
    private String lastName;

    @Schema(description = "Indicates whether the user account is disabled", example = "false")
    private boolean disabled;

    @Schema(description = "Timestamp when the user was created", example = "2024-03-14T10:15:30")
    private LocalDateTime createdAt;

    @Schema(description = "Timestamp when the user was last updated", example = "2024-03-14T12:30:45")
    private LocalDateTime updatedAt;

    // Constructor to map entity to DTO
    public UserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.disabled = user.isDisabled();
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();
    }
}
