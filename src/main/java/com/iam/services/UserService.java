package com.iam.services;

import com.generic.exceptions.BadRequestException;
import com.generic.exceptions.ConflictException;
import com.generic.exceptions.NotFoundException;
import com.iam.dto.NewUserDTO;
import com.iam.dto.UserDTO;
import com.iam.models.User;
import com.iam.repositories.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service class for managing users.
 */
@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Creates a new user.
     */
    public UserDTO createUser(NewUserDTO newUserDTO) {
        logger.info("Attempting to create user with email: {}", newUserDTO.getEmail());

        if (userRepository.existsByEmail(newUserDTO.getEmail())) {
            throw new ConflictException("User with email " + newUserDTO.getEmail() + " already exists.");
        }

        if (userRepository.existsByUsername(newUserDTO.getUsername())) {
            throw new ConflictException("User with username " + newUserDTO.getUsername() + " already exists.");
        }

        User user = new User(
                newUserDTO.getUsername(),
                newUserDTO.getEmail(),
                passwordEncoder.encode(newUserDTO.getPassword()),
                newUserDTO.getFirstName(),
                newUserDTO.getLastName());

        userRepository.save(user);
        logger.info("User created successfully: {}", user.getId());

        return new UserDTO(user);
    }

    /**
     * Retrieves a user by their ID.
     */
    public UserDTO getUserById(Long userId) {
        logger.info("Fetching user by ID: {}", userId);

        return userRepository.findById(userId)
                .map(UserDTO::new)
                .orElseThrow(() -> {
                    logger.warn("User not found with ID: {}", userId);
                    return new NotFoundException("User not found");
                });
    }

    /**
     * Retrieves a user by their email.
     */
    public UserDTO getUserByEmail(String email) {
        logger.info("Fetching user by email: {}", email);

        return userRepository.findByEmail(email)
                .map(UserDTO::new)
                .orElseThrow(() -> {
                    logger.warn("User not found with email: {}", email);
                    return new NotFoundException("User not found");
                });
    }

    /**
     * Retrieves a user by their username.
     */
    public UserDTO getUserByUsername(String username) {
        logger.info("Fetching user by username: {}", username);

        return userRepository.findByUsername(username)
                .map(UserDTO::new)
                .orElseThrow(() -> {
                    logger.warn("User not found with username: {}", username);
                    return new NotFoundException("User not found");
                });
    }

    /**
     * Updates a user's disabled status.
     */
    public void updateUserDisabledStatus(Long userId, boolean disabled) {
        logger.info("Updating user disabled status. User ID: {}, Disabled: {}", userId, disabled);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.warn("User not found when updating disabled status. ID: {}", userId);
                    return new NotFoundException("User not found");
                });

        user.setDisabled(disabled);
        userRepository.save(user);
        logger.info("User {} disabled status updated to {}", userId, disabled);
    }

    /**
     * Updates a user's password.
     */
    public void updateUserPassword(Long userId, String oldPassword, String newPassword, String reenterNewPassword) {
        logger.info("Updating password for user ID: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.warn("User not found when updating password. ID: {}", userId);
                    return new NotFoundException("User not found");
                });

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BadRequestException("Old password is incorrect");
        }

        if (!newPassword.equals(reenterNewPassword)) {
            throw new BadRequestException("New password and re-entered password do not match");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        logger.info("Password updated successfully for user ID: {}", userId);
    }
}
