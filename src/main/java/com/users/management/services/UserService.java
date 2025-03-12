package com.users.management.services;

import com.generic.utility.exceptions.BadRequestException;
import com.generic.utility.exceptions.ConflictException;
import com.generic.utility.exceptions.NotFoundException;
import com.users.management.dto.UserDTO;
import com.users.management.models.User;
import com.users.management.repositories.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDTO getUserById(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.map(
                u -> new UserDTO(u.getId(), u.getUsername(), u.getEmail(), null, u.getFirstName(), u.getLastName()))
                .orElse(null);
    }

    public UserDTO getUserByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.map(
                u -> new UserDTO(u.getId(), u.getUsername(), u.getEmail(), null, u.getFirstName(), u.getLastName()))
                .orElse(null);
    }

    public UserDTO getUserByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.map(
                u -> new UserDTO(u.getId(), u.getUsername(), u.getEmail(), null, u.getFirstName(), u.getLastName()))
                .orElse(null);
    }

    public UserDTO createUser(String username, String email, String password, String firstname, String lastName) {
        if (userRepository.existsByEmail(email)) {
            throw new ConflictException("User with email " + email + " already exists.");
        }

        if (userRepository.existsByUsername(username)) {
            throw new ConflictException("User with username " + username + " already exists.");
        }

        User user = new User(username, email, passwordEncoder.encode(password), firstname, lastName);
        userRepository.save(user);

        return new UserDTO(user.getId(), user.getUsername(), user.getEmail(), null, password, password);
    }

    public void updateUserDisabledStatus(Long userId, boolean disabled) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        user.setDisabled(disabled);
        userRepository.save(user);
    }

    public void updateUserPassword(Long userId, String oldPassword, String newPassword, String reenterNewPassword) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));

        // check if the old password is correct
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BadRequestException("Old password is incorrect");
        }

        // check if the new passwords match
        if (!newPassword.equals(reenterNewPassword)) {
            throw new BadRequestException("New password and re-entered password do not match");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}
