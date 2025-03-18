package com.iam.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.iam.models.UserNotificationSetting;
import com.iam.services.UserNotificationSettingService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RequestMapping("/api/v1/users/{userId}/settings/notifications")
@Tag(name = "User Notification Settings", description = "Manage user notification preferences")
public class UserNotificationSettingController {
    private static final Logger logger = LoggerFactory.getLogger(UserNotificationSettingController.class);

    private final UserNotificationSettingService service;

    @Autowired
    public UserNotificationSettingController(UserNotificationSettingService service) {
        this.service = service;
    }

    @Operation(summary = "Get user notification settings")
    @GetMapping
    public ResponseEntity<List<UserNotificationSetting>> getUserNotificationSettings(
            @PathVariable Long userId) {
        return ResponseEntity.ok(service.getUserSettings(userId));
    }

    @Operation(summary = "Update notification settings for a user")
    @PutMapping("/{channel}")
    public ResponseEntity<String> updateNotificationSetting(
            @PathVariable Long userId,
            @PathVariable String channel,
            @RequestParam boolean enabled) {

        service.updateUserNotificationSetting(userId, channel, enabled);
        return ResponseEntity.ok("Notification setting updated");
    }
}
