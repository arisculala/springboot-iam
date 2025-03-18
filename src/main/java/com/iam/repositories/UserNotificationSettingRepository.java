package com.iam.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iam.models.UserNotificationSetting;

public interface UserNotificationSettingRepository extends JpaRepository<UserNotificationSetting, Long> {
    List<UserNotificationSetting> findByUserId(Long userId);

    Optional<UserNotificationSetting> findByUserIdAndChannel(Long userId, String channel);
}
