package com.iam.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iam.models.UserNotificationSetting;
import com.iam.repositories.UserNotificationSettingRepository;

@Service
public class UserNotificationSettingService {
    private static final Logger logger = LoggerFactory.getLogger(UserNotificationSettingService.class);

    private final UserNotificationSettingRepository userNotificationSettingRepository;

    @Autowired
    public UserNotificationSettingService(UserNotificationSettingRepository userNotificationSettingRepository) {
        this.userNotificationSettingRepository = userNotificationSettingRepository;
    }

    public List<UserNotificationSetting> getUserSettings(Long userId) {
        return userNotificationSettingRepository.findByUserId(userId);
    }

    public void updateUserNotificationSetting(Long userId, String channel, boolean enabled) {
        UserNotificationSetting setting = userNotificationSettingRepository.findByUserIdAndChannel(userId, channel)
                .orElse(new UserNotificationSetting());

        setting.setUserId(userId);
        setting.setChannel(channel);
        setting.setEnabled(enabled);

        userNotificationSettingRepository.save(setting);
    }
}
